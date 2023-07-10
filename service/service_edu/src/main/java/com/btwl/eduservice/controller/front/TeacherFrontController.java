package com.btwl.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.btwl.commonutils.Ret;
import com.btwl.eduservice.entity.EduCourse;
import com.btwl.eduservice.entity.EduTeacher;
import com.btwl.eduservice.service.EduCourseService;
import com.btwl.eduservice.service.EduTeacherService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eduservice/teacherfront")
@CrossOrigin
public class TeacherFrontController {

  @Autowired
  private EduTeacherService teacherService;

  @Autowired
  private EduCourseService courseService;

  //1 分页查询讲师的方法
  @PostMapping("getTeacherFrontList/{page}/{limit}")
  public Ret getTeacherFrontList(@PathVariable long page, @PathVariable long limit) {
    Page<EduTeacher> pageTeacher = new Page<>(page, limit);
    Map<String, Object> map = teacherService.getTeacherFrontList(pageTeacher);
    //返回分页所有数据
    return Ret.ok().data(map);
  }

  //2 讲师详情的功能
  @GetMapping("getTeacherFrontInfo/{teacherId}")
  public Ret getTeacherFrontInfo(@PathVariable String teacherId) {
    //1 根据讲师id查询讲师基本信息
    EduTeacher eduTeacher = teacherService.getById(teacherId);
    //2 根据讲师id查询所讲课程
    QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
    wrapper.eq("teacher_id", teacherId);
    List<EduCourse> courseList = courseService.list(wrapper);
    return Ret.ok().data("teacher", eduTeacher).data("courseList", courseList);
  }
}












