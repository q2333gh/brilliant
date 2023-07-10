package com.btwl.staservice.controller;


import com.btwl.commonutils.Ret;
import com.btwl.staservice.service.StatisticsDailyService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-03-14
 */
@RestController
@RequestMapping("/staservice/sta")
@CrossOrigin
public class StatisticsDailyController {

  @Autowired
  private StatisticsDailyService staService;

  //统计某一天注册人数,生成统计数据
  @PostMapping("registerCount/{day}")
  public Ret registerCount(@PathVariable String day) {
    staService.registerCount(day);
    return Ret.ok();
  }

  //图表显示，返回两部分数据，日期json数组，数量json数组
  @GetMapping("showData/{type}/{begin}/{end}")
  public Ret showData(@PathVariable String type, @PathVariable String begin,
      @PathVariable String end) {
    Map<String, Object> map = staService.getShowData(type, begin, end);
    return Ret.ok().data(map);
  }
}

