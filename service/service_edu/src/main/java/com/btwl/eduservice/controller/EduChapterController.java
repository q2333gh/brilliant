package com.btwl.eduservice.controller;


import com.btwl.commonutils.Ret;
import com.btwl.eduservice.entity.EduChapter;
import com.btwl.eduservice.entity.chapter.ChapterVo;
import com.btwl.eduservice.service.EduChapterService;
import jakarta.annotation.Resource;
import java.util.List;
//import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@RestController
@RequestMapping("/eduservice/chapter")
@CrossOrigin
public class EduChapterController {


  @Resource
  private EduChapterService chapterService;

  //课程大纲列表,根据课程id进行查询
  @GetMapping("getChapterVideo/{courseId}")
  public Ret getChapterVideo(@PathVariable String courseId) {
    List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
    return Ret.ok().data("allChapterVideo", list);
  }

  //添加章节
  @PostMapping("addChapter")
  public Ret addChapter(@RequestBody EduChapter eduChapter) {
    chapterService.save(eduChapter);
    return Ret.ok();
  }

  //根据章节id查询
  @GetMapping("getChapterInfo/{chapterId}")
  public Ret getChapterInfo(@PathVariable String chapterId) {
    EduChapter eduChapter = chapterService.getById(chapterId);
    return Ret.ok().data("chapter", eduChapter);
  }

  //修改章节
  @PostMapping("updateChapter")
  public Ret updateChapter(@RequestBody EduChapter eduChapter) {
    chapterService.updateById(eduChapter);
    return Ret.ok();
  }

  //删除的方法
  @DeleteMapping("{chapterId}")
  public Ret deleteChapter(@PathVariable String chapterId) {
    boolean flag = chapterService.deleteChapter(chapterId);
    if (flag) {
      return Ret.ok();
    } else {
      return Ret.error();
    }

  }
}

