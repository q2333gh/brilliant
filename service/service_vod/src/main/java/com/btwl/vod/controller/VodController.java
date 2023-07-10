package com.btwl.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.btwl.commonutils.Ret;
import com.btwl.servicebase.exceptionhandler.BrilliantCustomException;
import com.btwl.vod.Utils.ConstantVodUtils;
import com.btwl.vod.Utils.InitVodCilent;
import com.btwl.vod.service.VodService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * todo abstract the business model:
 * the whole business goal is for user to upload,and play video.
 * so, we have multiple impl; cloud-oss is just a impl
 * we can also use platform ,some SaaS, such as youtube , blibli.
 *      maybe blibli dont have SDK, but can still use net-spyder to simulate the steps to do upload,play,
 *      and some how embed a play-GUI to website .
 *          now-days opensource vod lib: SRS <a href="https://github.com/ossrs/srs">link</a>
 *          SRS: Simple-RTMP-Server(RTMP: Real-Time Messaging Protocol,base on TCP)
 */
@RestController
@RequestMapping("/eduvod/video")
@CrossOrigin
public class VodController {

  @Resource
  private VodService vodService;

  //上传视频到阿里云
  @PostMapping("uploadAlyiVideo")
  public Ret uploadAlyiVideo(MultipartFile file) {
    //返回上传视频id
    String videoId = vodService.uploadVideoAly(file);
    return Ret.ok().data("videoId", videoId);
  }

  //根据视频id删除阿里云视频
  @DeleteMapping("removeAlyVideo/{id}")
  public Ret removeAlyVideo(@PathVariable String id) {
    try {
      //初始化对象
      DefaultAcsClient client = InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,
          ConstantVodUtils.ACCESS_KEY_SECRET);
      //创建删除视频request对象
      DeleteVideoRequest request = new DeleteVideoRequest();
      //向request设置视频id
      request.setVideoIds(id);
      //调用初始化对象的方法实现删除
      client.getAcsResponse(request);
      return Ret.ok();
    } catch (Exception e) {
      e.printStackTrace();
      throw new BrilliantCustomException(20001, "删除视频失败");
    }
  }

  //删除多个阿里云视频的方法
  //参数多个视频id  List videoIdList
  @DeleteMapping("delete-batch")
  public Ret deleteBatch(@RequestParam("videoIdList") List<String> videoIdList) {
    vodService.removeMoreAlyVideo(videoIdList);
    return Ret.ok();
  }

  //根据视频id获取视频凭证
  @GetMapping("getPlayAuth/{id}")
  public Ret getPlayAuth(@PathVariable String id) {
    try {
      //创建初始化对象
      DefaultAcsClient client =
          InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,
              ConstantVodUtils.ACCESS_KEY_SECRET);
      //创建获取凭证request和response对象
      GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
      //向request设置视频id
      request.setVideoId(id);
      //调用方法得到凭证
      GetVideoPlayAuthResponse response = client.getAcsResponse(request);
      String playAuth = response.getPlayAuth();
      return Ret.ok().data("playAuth", playAuth);
    } catch (Exception e) {
      throw new BrilliantCustomException(20001, "获取凭证失败");
    }
  }
}
