package com.btwl.eduservice.client;

import com.btwl.commonutils.Ret;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VodFileDegradeFeignClient implements VodClient {

  //出错之后会执行
  @Override
  public Ret removeAlyVideo(String id) {
    return Ret.error().message("删除视频出错了");
  }

  @Override
  public Ret deleteBatch(List<String> videoIdList) {
    return Ret.error().message("删除多个视频出错了");
  }
}
