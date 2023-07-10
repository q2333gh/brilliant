package com.btwl.eduservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.btwl.eduservice.entity.EduSubject;
import com.btwl.eduservice.entity.subject.OneSubject;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-02-29
 */
public interface EduSubjectService extends IService<EduSubject> {

  //添加课程分类
  void saveSubject(MultipartFile file, EduSubjectService subjectService);

  //课程分类列表（树形）
  List<OneSubject> getAllOneTwoSubject();
}
