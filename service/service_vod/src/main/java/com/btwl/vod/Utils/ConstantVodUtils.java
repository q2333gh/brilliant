package com.btwl.vod.Utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantVodUtils implements InitializingBean {

  public static String ACCESS_KEY_SECRET;
  public static String ACCESS_KEY_ID;
  @Value("${aliyun.vod.file.keyid}")
  private String keyid;
  @Value("${aliyun.vod.file.keysecret}")
  private String keysecret;

  @Override
  public void afterPropertiesSet() throws Exception {
    ACCESS_KEY_ID = keyid;
    ACCESS_KEY_SECRET = keysecret;
  }
}
