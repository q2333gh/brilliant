package com.btwl.eduservice.controller.front;

import static com.btwl.commonutils.JwtUtils.getMemberIdByJwtToken;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.btwl.commonutils.Ret;
import com.btwl.commonutils.ordervo.CourseWebVoOrder;
import com.btwl.eduservice.client.OrdersClient;
import com.btwl.eduservice.entity.EduCourse;
import com.btwl.eduservice.entity.chapter.ChapterVo;
import com.btwl.eduservice.entity.frontvo.CourseFrontVo;
import com.btwl.eduservice.entity.frontvo.CourseWebVo;
import com.btwl.eduservice.service.EduChapterService;
import com.btwl.eduservice.service.EduCourseService;
//import jakarta.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/eduservice/coursefront")
@CrossOrigin
public class CourseFrontController {

  @Autowired
  private EduCourseService courseService;

  @Autowired
  private EduChapterService chapterService;

  @Autowired
  private OrdersClient ordersClient;

  //1 条件查询带分页查询课程
  @PostMapping("getFrontCourseList/{page}/{limit}")
  public Ret getFrontCourseList(@PathVariable long page, @PathVariable long limit,
      @RequestBody(required = false) CourseFrontVo courseFrontVo) {
    Page<EduCourse> pageCourse = new Page<>(page, limit);
    Map<String, Object> map = courseService.getCourseFrontList(pageCourse, courseFrontVo);
    //返回分页所有数据
    return Ret.ok().data(map);
  }

  //2 课程详情的方法
  @GetMapping("getFrontCourseInfo/{courseId}")
  public Ret getFrontCourseInfo(
      @PathVariable String courseId,
      HttpServletRequest request) {
    //根据课程id，编写sql语句查询课程信息
    CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
    //根据课程id查询章节和小节
    List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);
    //根据课程id和用户id查询当前课程是否已经支付过了
    boolean buyCourse = ordersClient.isBuyCourse(courseId, getMemberIdByJwtToken(request));
    return Ret.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList)
        .data("isBuy", buyCourse);
  }

  //根据课程id查询课程信息
  @PostMapping("getCourseInfoOrder/{id}")
  public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id) {
    CourseWebVo courseInfo = courseService.getBaseCourseInfo(id);
    CourseWebVoOrder courseWebVoOrder = new CourseWebVoOrder();
    BeanUtils.copyProperties(courseInfo, courseWebVoOrder);
    return courseWebVoOrder;
  }
}












