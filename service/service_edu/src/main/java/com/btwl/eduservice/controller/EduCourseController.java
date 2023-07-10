package com.btwl.eduservice.controller;


import com.btwl.commonutils.Ret;
import com.btwl.eduservice.entity.EduCourse;
import com.btwl.eduservice.entity.vo.CourseInfoVo;
import com.btwl.eduservice.entity.vo.CoursePublishVo;
import com.btwl.eduservice.service.EduCourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eduservice/course")
@CrossOrigin
public class EduCourseController {

  @Autowired
  private EduCourseService courseService;

  //课程列表 基本实现
  //TODO  完善条件查询带分页
  @GetMapping
  public Ret getCourseList() {
    List<EduCourse> list = courseService.list(null);
    return Ret.ok().data("list", list);
  }

  //添加课程基本信息的方法
  @PostMapping("addCourseInfo")
  public Ret addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
    //返回添加之后课程id，为了后面添加大纲使用
    String id = courseService.saveCourseInfo(courseInfoVo);
    return Ret.ok().data("courseId", id);
  }

  //根据课程id查询课程基本信息
  @GetMapping("getCourseInfo/{courseId}")
  public Ret getCourseInfo(@PathVariable String courseId) {
    CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
    return Ret.ok().data("courseInfoVo", courseInfoVo);
  }

  //修改课程信息
  @PostMapping("updateCourseInfo")
  public Ret updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
    courseService.updateCourseInfo(courseInfoVo);
    return Ret.ok();
  }

  //根据课程id查询课程确认信息
  @GetMapping("getPublishCourseInfo/{id}")
  public Ret getPublishCourseInfo(@PathVariable String id) {
    CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
    return Ret.ok().data("publishCourse", coursePublishVo);
  }

  //课程最终发布
  //修改课程状态
  @PostMapping("publishCourse/{id}")
  public Ret publishCourse(@PathVariable String id) {
    EduCourse eduCourse = new EduCourse();
    eduCourse.setId(id);
    eduCourse.setStatus("Normal");//设置课程发布状态
    courseService.updateById(eduCourse);
    return Ret.ok();
  }

  //删除课程
  @DeleteMapping("{courseId}")
  public Ret deleteCourse(@PathVariable String courseId) {
    courseService.removeCourse(courseId);
    return Ret.ok();
  }

}

