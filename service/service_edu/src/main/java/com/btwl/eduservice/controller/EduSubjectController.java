package com.btwl.eduservice.controller;


import com.btwl.commonutils.Ret;
import com.btwl.eduservice.entity.subject.OneSubject;
import com.btwl.eduservice.service.EduSubjectService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-02-29
 */
  @RestController
  @RequestMapping("/eduservice/subject")
@CrossOrigin
public class EduSubjectController {

  @Autowired
  private EduSubjectService subjectService;

  //添加课程分类
  //获取上传过来文件，把文件内容读取出来
  @PostMapping("addSubject")
  public Ret addSubject(MultipartFile file) {
    //上传过来excel文件
    subjectService.saveSubject(file, subjectService);
    return Ret.ok();
  }

  //课程分类列表（树形）
  @GetMapping("getAllSubject")
  public Ret getAllSubject() {
    //list集合泛型是一级分类
    List<OneSubject> list = subjectService.getAllOneTwoSubject();
    return Ret.ok().data("list", list);
  }

}

