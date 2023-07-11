package com.btwl.educenter.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantWxUtils implements InitializingBean {  //InitializingBean ,启动JVM即注册这个bean

  public static String WX_OPEN_APP_ID;
  public static String WX_OPEN_APP_SECRET;
  public static String WX_OPEN_REDIRECT_URL;
  @Value("${wx.open.app_id}") // 引用application.yml里面的数据
  private String appId;
  @Value("${wx.open.app_secret}")
  private String appSecret;
  @Value("${wx.open.redirect_url}")
  private String redirectUrl;

  @Override
  public void afterPropertiesSet() throws Exception {
    WX_OPEN_APP_ID = appId;
    WX_OPEN_APP_SECRET = appSecret;
    WX_OPEN_REDIRECT_URL = redirectUrl;
  }
}
