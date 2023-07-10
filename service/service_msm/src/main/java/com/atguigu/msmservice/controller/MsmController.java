package com.btwl.msmservice.controller;

import com.btwl.commonutils.Ret;
import com.btwl.msmservice.service.MsmService;
import com.btwl.msmservice.utils.RandomUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

  @Autowired
  private MsmService msmService;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  //发送短信的方法
  @GetMapping("send/{phone}")
  public Ret sendMsm(@PathVariable String phone) {
    //1 从redis获取验证码，如果获取到直接返回
    String code = redisTemplate.opsForValue().get(phone);
    if (!StringUtils.isEmpty(code)) {
      return Ret.ok();
    }
    //2 如果redis获取 不到，进行阿里云发送
    //生成随机值，传递阿里云进行发送
    code = RandomUtil.getFourBitRandom();
    Map<String, Object> param = new HashMap<>();
    param.put("code", code);
    //调用service发送短信的方法
    boolean isSend = msmService.send(param, phone);
    if (isSend) {
      //发送成功，把发送成功验证码放到redis里面
      //设置有效时间
      redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
      return Ret.ok();
    } else {
      return Ret.error().message("短信发送失败");
    }
  }
}
