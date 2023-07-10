package com.btwl.eduservice.controller;


import com.btwl.commonutils.Ret;
import com.btwl.eduservice.client.VodClient;
import com.btwl.eduservice.entity.EduVideo;
import com.btwl.eduservice.service.EduVideoService;
import com.btwl.servicebase.exceptionhandler.BrilliantCustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@RestController
@RequestMapping("/eduservice/video")
@CrossOrigin
public class EduVideoController {

  @Autowired
  private EduVideoService videoService;

  //注入vodClient
  @Autowired
  private VodClient vodClient;

  //添加小节
  @PostMapping("addVideo")
  public Ret addVideo(@RequestBody EduVideo eduVideo) {
    videoService.save(eduVideo);
    return Ret.ok();
  }

  //删除小节，删除对应阿里云视频
  @DeleteMapping("{id}")
  public Ret deleteVideo(@PathVariable String id) {
    //根据小节id获取视频id，调用方法实现视频删除
    EduVideo eduVideo = videoService.getById(id);
    String videoSourceId = eduVideo.getVideoSourceId();
    //判断小节里面是否有视频id
    if (!StringUtils.isEmpty(videoSourceId)) {
      //根据视频id，远程调用实现视频删除
      Ret result = vodClient.removeAlyVideo(videoSourceId);
      if (result.getCode() == 20001) {
        throw new BrilliantCustomException(20001, "删除视频失败，熔断器...");
      }
    }
    //删除小节
    videoService.removeById(id);
    return Ret.ok();
  }

  //修改小节 TODO

}

