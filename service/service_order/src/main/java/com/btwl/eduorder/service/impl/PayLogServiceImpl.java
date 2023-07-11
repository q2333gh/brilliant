package com.btwl.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.btwl.eduorder.entity.Order;
import com.btwl.eduorder.entity.PayLog;
import com.btwl.eduorder.mapper.PayLogMapper;
import com.btwl.eduorder.service.OrderService;
import com.btwl.eduorder.service.PayLogService;
import com.btwl.eduorder.utils.HttpClient;
import com.btwl.servicebase.exceptionhandler.BrilliantCustomException;
import com.github.wxpay.sdk.WXPayUtil;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-13
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

  @Autowired
  private OrderService orderService;

  //生成微信支付二维码接口
  @Override
  public Map createNative(String orderNo) {
    try {
      //1 根据订单号查询订单信息
      Order order = getOne(orderNo);

      //2 使用map设置生成二维码需要参数
      Map<String, String> params = setParams(orderNo, order);

      //3 发送httpclient请求，传递参数xml格式，微信支付提供的固定的地址
      HttpClient client = wxHttp();
      //设置xml格式的参数
      client.setXmlParam(WXPayUtil.generateSignedXml(params, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
      client.setHttps(true);
      //执行post请求发送
      client.post();

      //4 得到发送请求返回结果
      //返回内容，是使用xml格式返回
      String xml = client.getContent();

      //把xml格式转换map集合，把map集合返回
      Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

      return getMap(orderNo, order, resultMap);
    } catch (Exception e) {
      throw new BrilliantCustomException(20001, "生成二维码失败");
    }

  }

  //查询订单支付状态
  @Override
  public Map<String, String> queryPayStatus(String orderNo) {
    try {
      //1、封装参数
      Map<String, String> m = setMap(orderNo);

      //2 发送httpclient
      HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
      client.setXmlParam(WXPayUtil.generateSignedXml(m, "T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
      client.setHttps(true);
      client.post();

      //3 得到请求返回内容
      String xml = client.getContent();
      //6、转成Map再返回
      return WXPayUtil.xmlToMap(xml);
    } catch (Exception e) {
      return null;
    }
  }

  private static Map<String, String> setMap(String orderNo) {
    Map<String,String> m = new HashMap<>();
    m.put("appid", "wx74862e0dfcf69954");
    m.put("mch_id", "1558950191");
    m.put("out_trade_no", orderNo);
    m.put("nonce_str", WXPayUtil.generateNonceStr());
    return m;
  }

  //添加支付记录和更新订单状态
  @Override
  public void updateOrdersStatus(Map<String, String> map) {
    //从map获取订单号
    String orderNo = map.get("out_trade_no");
    //根据订单号查询订单信息
    Order order = getOne(orderNo);

    //更新订单表订单状态
    if (order.getStatus() == 1) {
      return;
    }
    order.setStatus(1);//1代表已经支付
    orderService.updateById(order);

    //向支付表添加支付记录
    PayLog payLog = setPayLog(map, orderNo, order);


    baseMapper.insert(payLog);
  }

  private static PayLog setPayLog(Map<String, String> map, String orderNo, Order order) {
    PayLog payLog = new PayLog();
    payLog.setOrderNo(orderNo);  //订单号
    payLog.setPayTime(new Date()); //订单完成时间
    payLog.setPayType(1);//支付类型 1微信
    payLog.setTotalFee(order.getTotalFee());//总金额(分)

    payLog.setTradeState(map.get("trade_state"));//支付状态
    payLog.setTransactionId(map.get("transaction_id")); //流水号
    payLog.setAttr(JSONObject.toJSONString(map));
    return payLog;
  }

  /**
   * MP-get one

   */
  private Order getOne(String orderNo) {
    QueryWrapper<Order> wrapper = new QueryWrapper<>();
    wrapper.eq("order_no", orderNo);
    return orderService.getOne(wrapper);
  }

  private static Map<String, Serializable> getMap(String orderNo, Order order,
      Map<String, String> resultMap) {
    Map<String, Serializable> map = new HashMap<>();
    map.put("out_trade_no", orderNo);
    map.put("course_id", order.getCourseId());
    map.put("total_fee", order.getTotalFee());
    map.put("result_code", resultMap.get("result_code"));  //返回二维码操作状态码
    map.put("code_url", resultMap.get("code_url"));        //二维码地址
    return map;
  }

  private static HttpClient wxHttp() {
    return new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
  }

  private static Map<String, String> setParams(String orderNo, Order order) {
    Map<String, String> m = new HashMap<String, String>();
    m.put("appid", "wx74862e0dfcf69954");
    m.put("mch_id", "1558950191");
    m.put("nonce_str", WXPayUtil.generateNonceStr());
    m.put("body", order.getCourseTitle()); //课程标题
    m.put("out_trade_no", orderNo); //订单号
    m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
    m.put("spbill_create_ip", "127.0.0.1");
    m.put("notify_url", "http://brilliant.shop/api/order/weixinPay/weixinNotify\n");
    m.put("trade_type", "NATIVE");
    return m;
  }


}
