package com.btwl.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.btwl.eduservice.client.VodClient;
import com.btwl.eduservice.entity.EduVideo;
import com.btwl.eduservice.mapper.EduVideoMapper;
import com.btwl.eduservice.service.EduVideoService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements
    EduVideoService {

  //注入vodClient
  @Resource
  private VodClient vodClient;

  //1 根据课程id删除小节
  @Override
  public void removeVideoByCourseId(String courseId) {
    //1 根据课程id查询课程所有的视频id
    QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
    wrapperVideo.eq("course_id", courseId);
    wrapperVideo.select("video_source_id");
    List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

    // List<EduVideo>变成List<String>
    List<String> videoIds = new ArrayList<>();
    for (EduVideo eduVideo : eduVideoList) {
      String videoSourceId = eduVideo.getVideoSourceId();
      if (!StringUtils.isEmpty(videoSourceId)) {
        //放到videoIds集合里面
        videoIds.add(videoSourceId);
      }
    }

    //根据多个视频id删除多个视频
    if (videoIds.size() > 0) {
      vodClient.deleteBatch(videoIds);
    }

    QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
    wrapper.eq("course_id", courseId);
    baseMapper.delete(wrapper);
  }
}
