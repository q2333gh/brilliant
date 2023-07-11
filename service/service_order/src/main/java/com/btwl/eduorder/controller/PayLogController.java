package com.btwl.eduorder.controller;


import com.btwl.commonutils.Ret;
import com.btwl.eduorder.service.PayLogService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-03-13
 */
@RestController
@RequestMapping("/eduorder/paylog")
@CrossOrigin
public class PayLogController {

  @Autowired
  private PayLogService payLogService;

  /**
   *   //生成微信支付二维码接口
   *   //参数是订单号

   */
  @GetMapping("createNative/{orderNo}")
  public Ret createNative(@PathVariable String orderNo) {
    //返回信息，包含二维码地址，还有其他需要的信息
    Map map = payLogService.createNative(orderNo);
    System.out.println("****返回二维码map集合:" + map);
    return Ret.ok().data(map);
  }

  //查询订单支付状态
  //参数：订单号，根据订单号查询 支付状态
  @GetMapping("queryPayStatus/{orderNo}")
  public Ret queryPayStatus(@PathVariable String orderNo) {
    Map<String, String> map = payLogService.queryPayStatus(orderNo);
    System.out.println("*****查询订单状态map集合:" + map);
    if (map == null) {
      return Ret.error().message("支付出错了");
    }
    //如果返回map里面不为空，通过map获取订单状态
    if (map.get("trade_state").equals("SUCCESS")) {//支付成功
      //添加记录到支付表，更新订单表订单状态
      payLogService.updateOrdersStatus(map);
      return Ret.ok().message("支付成功");
    }
    return Ret.ok().code(25000).message("支付中");

  }
}

