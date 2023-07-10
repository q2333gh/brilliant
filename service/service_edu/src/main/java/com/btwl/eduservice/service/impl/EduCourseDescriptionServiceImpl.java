package com.btwl.eduservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.btwl.eduservice.entity.EduCourseDescription;
import com.btwl.eduservice.mapper.EduCourseDescriptionMapper;
import com.btwl.eduservice.service.EduCourseDescriptionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Service
public class EduCourseDescriptionServiceImpl extends
    ServiceImpl<EduCourseDescriptionMapper, EduCourseDescription> implements
    EduCourseDescriptionService {

}
