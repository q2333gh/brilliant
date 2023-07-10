package com.btwl.servicebase.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.util.Date;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    //属性名称，不是字段名称
    this.setFieldValByName("gmtCreate", new Date(), metaObject);
    this.setFieldValByName("gmtModified", new Date(), metaObject);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    this.setFieldValByName("gmtModified", new Date(), metaObject);
  }
}
