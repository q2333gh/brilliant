package com.btwl.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.btwl.commonutils.Ret;
import com.btwl.eduservice.entity.EduCourse;
import com.btwl.eduservice.entity.EduTeacher;
import com.btwl.eduservice.service.EduCourseService;
import com.btwl.eduservice.service.EduTeacherService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eduservice/indexfront")
@CrossOrigin
public class IndexFrontController {

  @Autowired
  private EduCourseService courseService;

  @Autowired
  private EduTeacherService teacherService;

  //查询前8条热门课程，查询前4条名师
  @GetMapping("index")
  public Ret index() {
    //查询前8条热门课程
    QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
    wrapper.orderByDesc("id");
    wrapper.last("limit 8");
    List<EduCourse> eduList = courseService.list(wrapper);

    //查询前4条名师
    QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
    wrapperTeacher.orderByDesc("id");
    wrapperTeacher.last("limit 4");
    List<EduTeacher> teacherList = teacherService.list(wrapperTeacher);

    return Ret.ok().data("eduList", eduList).data("teacherList", teacherList);
  }

}
