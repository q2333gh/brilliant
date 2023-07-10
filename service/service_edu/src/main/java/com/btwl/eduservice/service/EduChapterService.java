package com.btwl.eduservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.btwl.eduservice.entity.EduChapter;
import com.btwl.eduservice.entity.chapter.ChapterVo;
import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
public interface EduChapterService extends IService<EduChapter> {

  //课程大纲列表,根据课程id进行查询
  List<ChapterVo> getChapterVideoByCourseId(String courseId);

  //删除章节的方法
  boolean deleteChapter(String chapterId);

  //2 根据课程id删除章节
  void removeChapterByCourseId(String courseId);
}
