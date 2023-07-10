package com.btwl.eduservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.btwl.eduservice.entity.EduVideo;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
public interface EduVideoService extends IService<EduVideo> {

  //1 根据课程id删除小节
  void removeVideoByCourseId(String courseId);
}
