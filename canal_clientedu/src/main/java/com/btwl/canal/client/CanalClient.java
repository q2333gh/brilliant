package com.btwl.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Resource;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Component;

/**
 * 持续从MySQL读取新的插入新数据写传给前端通知
 *
 * todo  是增量更新的吗,而不是查询全部然后重复写入很多已经存在的
 */
@Component
public class CanalClient {

  //sql队列
  private final Queue<String> SQL_QUEUE = new ConcurrentLinkedQueue<>();

  @Resource
  private DataSource dataSource;

  /**
   * canal入库方法
   */
  public void run() {

    CanalConnector connector = CanalConnectors.newSingleConnector(
        new InetSocketAddress("192.168.44.132",
            11111), "example", "", "");
    int batchSize = 1000;
    try {
      connector.connect();
      connector.subscribe(".*\\..*");
      connector.rollback();
      try {
        while (true) {
          //尝试从master那边拉去数据batchSize条记录，有多少取多少
          Message message = connector.getWithoutAck(batchSize);
          long batchId = message.getId();
          int size = message.getEntries().size();
          if (batchId == -1 || size == 0) {
            Thread.sleep(1000);
          } else {
            handleData(message.getEntries());
          }
          connector.ack(batchId);

          //当队列里面堆积的sql大于一定数值的时候就模拟执行
          if (SQL_QUEUE.size() >= 1) {
            executeQueueSql();
          }
        }
      } catch (InterruptedException | InvalidProtocolBufferException e) {
        e.printStackTrace();
      }
    } finally {
      connector.disconnect();
    }
  }

  /**
   * 模拟执行队列里面的sql语句
   */
  public void executeQueueSql() {
    int size = SQL_QUEUE.size();
    for (int i = 0; i < size; i++) {
      String sql = SQL_QUEUE.poll();
      System.out.println("[sql]----> " + sql);

      assert sql != null;
      this.execute(sql);
    }
  }


  /**
   * 入库
   *
   */
  public void execute(String sql) {
    Connection con = null;
    try {
      if (null == sql) {
        return;
      }
      con = dataSource.getConnection();
      QueryRunner qr = new QueryRunner();
      int row = qr.execute(con, sql);
      System.out.println("update: " + row);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DbUtils.closeQuietly(con);
    }
  }

  /**
   * 数据处理
   *

   */
  private void handleData(List<Entry> entries) throws InvalidProtocolBufferException {
    for (Entry entry : entries) {
      if (EntryType.ROWDATA == entry.getEntryType()) {
        RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
        EventType eventType = rowChange.getEventType();
        if (eventType == EventType.DELETE) {
          saveDeleteSql(entry);
        } else if (eventType == EventType.UPDATE) {
          saveUpdateSql(entry);
        } else if (eventType == EventType.INSERT) {
          saveInsertSql(entry);
        }
      }
    }
  }

  /**
   * 保存更新语句
   *

   */
  private void saveUpdateSql(Entry entry) {
    try {
      RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
      List<RowData> rowDataList = rowChange.getRowDatasList();
      sqlPrep(entry, rowDataList);
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  private void sqlPrep(Entry entry, List<RowData> rowDataList) {
    for (RowData rowData : rowDataList) {
      List<Column> newColumnList = rowData.getAfterColumnsList();
      StringBuilder sql = new StringBuilder("update " + entry.getHeader().getTableName() + " set ");
      for (int i = 0; i < newColumnList.size(); i++) {
        sql.append(" ").append(newColumnList.get(i).getName()).append(" = '")
            .append(newColumnList.get(i).getValue()).append("'");
        if (i != newColumnList.size() - 1) {
          sql.append(",");
        }
      }
      sql.append(" where ");
      List<Column> oldColumnList = rowData.getBeforeColumnsList();
      for (Column column : oldColumnList) {
        if (column.getIsKey()) {
          //暂时只支持单一主键
          sql.append(column.getName()).append("=").append(column.getValue());
          break;
        }
      }
      SQL_QUEUE.add(sql.toString());
    }
  }

  /**
   * 保存删除语句
   *

   */
  private void saveDeleteSql(Entry entry) {
    try {
      RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
      List<RowData> rowDataList = rowChange.getRowDatasList();
      deletePrep(entry, rowDataList);
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

  private void deletePrep(Entry entry, List<RowData> rowDataList) {
    for (RowData rowData : rowDataList) {
      List<Column> columnList = rowData.getBeforeColumnsList();
      StringBuilder sql = new StringBuilder(
          "delete from " + entry.getHeader().getTableName() + " where ");
      for (Column column : columnList) {
        if (column.getIsKey()) {
          //暂时只支持单一主键
          sql.append(column.getName()).append("=").append(column.getValue());
          break;
        }
      }
      SQL_QUEUE.add(sql.toString());
    }
  }

  /**
   * 保存插入语句
   *

   */
  private void saveInsertSql(Entry entry) {
    try {
      RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
      List<RowData> rowDatasList = rowChange.getRowDatasList();
      for (RowData rowData : rowDatasList) {
        List<Column> columnList = rowData.getAfterColumnsList();
        StringBuilder sql = new StringBuilder(
            "insert into " + entry.getHeader().getTableName() + " (");
        for (int i = 0; i < columnList.size(); i++) {
          sql.append(columnList.get(i).getName());
          if (i != columnList.size() - 1) {
            sql.append(",");
          }
        }
        sql.append(") VALUES (");
        for (int i = 0; i < columnList.size(); i++) {
          sql.append("'").append(columnList.get(i).getValue()).append("'");
          if (i != columnList.size() - 1) {
            sql.append(",");
          }
        }
        sql.append(")");
        SQL_QUEUE.add(sql.toString());
      }
    } catch (InvalidProtocolBufferException e) {
      e.printStackTrace();
    }
  }

}
