package com.btwl.staservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.btwl.commonutils.Ret;
import com.btwl.staservice.client.UcenterClient;
import com.btwl.staservice.entity.StatisticsDaily;
import com.btwl.staservice.mapper.StatisticsDailyMapper;
import com.btwl.staservice.service.StatisticsDailyService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-14
 */
@Service
public class StatisticsDailyServiceImpl extends
    ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

  @Autowired
  private UcenterClient ucenterClient;

  @Override
  public void registerCount(String day) {

    //添加记录之前删除表相同日期的数据
    QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
    wrapper.eq("date_calculated", day);
    baseMapper.delete(wrapper);

    //远程调用得到某一天注册人数
    Ret registerRet = ucenterClient.countRegister(day);//remote call
    Integer countRegister = (Integer) registerRet.getData().get("countRegister");

    //把获取数据添加数据库，统计分析表里面
    StatisticsDaily sta = setStatis(day, countRegister);
    baseMapper.insert(sta);
  }

  private static StatisticsDaily setStatis(String day, Integer countRegister) {
    StatisticsDaily sta = new StatisticsDaily();
    sta.setRegisterNum(countRegister); //注册人数
    sta.setDateCalculated(day);//统计日期

    sta.setVideoViewNum(RandomUtils.nextInt(100, 200));
    sta.setLoginNum(RandomUtils.nextInt(100, 200));
    sta.setCourseNum(RandomUtils.nextInt(100, 200));
    return sta;
  }

  //图表显示，返回两部分数据，日期json数组，数量json数组
  @Override
  public Map<String, Object> getShowData(String type, String begin, String end) {
    //根据条件查询对应数据
    QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
    wrapper.between("date_calculated", begin, end);
    wrapper.select("date_calculated", type);
    List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

    //因为返回有两部分数据：日期 和 日期对应数量
    //前端要求数组json结构，对应后端java代码是list集合
    //创建两个list集合，一个日期list，一个数量list
    List<String> date_calculatedList = new ArrayList<>();
    List<Integer> numDataList = new ArrayList<>();

    //遍历查询所有数据list集合，进行封装
    for (StatisticsDaily daily : staList) {
      //封装日期list集合
      date_calculatedList.add(daily.getDateCalculated());
      //封装对应数量
      switch (type) {
        case "login_num":
          numDataList.add(daily.getLoginNum());
          break;
        case "register_num":
          numDataList.add(daily.getRegisterNum());
          break;
        case "video_view_num":
          numDataList.add(daily.getVideoViewNum());
          break;
        case "course_num":
          numDataList.add(daily.getCourseNum());
          break;
        default:
          break;
      }
    }
    //把封装之后两个list集合放到map集合，进行返回
    Map<String, Object> map = new HashMap<>();
    map.put("date_calculatedList", date_calculatedList);
    map.put("numDataList", numDataList);
    return map;
  }
}
