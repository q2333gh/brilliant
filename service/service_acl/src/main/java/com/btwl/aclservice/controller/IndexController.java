package com.btwl.aclservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.btwl.aclservice.service.IndexService;
import com.btwl.commonutils.Ret;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/acl/index")
//@CrossOrigin
public class IndexController {

  @Autowired
  private IndexService indexService;

  /**
   * 根据token获取用户信息
   */
  @GetMapping("info")
  public Ret info() {
    //获取当前登录用户用户名
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    Map<String, Object> userInfo = indexService.getUserInfo(username);
    return Ret.ok().data(userInfo);
  }

  /**
   * 获取菜单
   *
   * @return
   */
  @GetMapping("menu")
  public Ret getMenu() {
    //获取当前登录用户用户名
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    List<JSONObject> permissionList = indexService.getMenu(username);
    return Ret.ok().data("permissionList", permissionList);
  }

  @PostMapping("logout")
  public Ret logout() {
    return Ret.ok();
  }

}
