package com.btwl.aclservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.btwl.aclservice.entity.User;
import com.btwl.aclservice.service.RoleService;
import com.btwl.aclservice.service.UserService;
import com.btwl.commonutils.MD5;
import com.btwl.commonutils.Ret;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/user")
//@CrossOrigin
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private RoleService roleService;

  @ApiOperation(value = "获取管理用户分页列表")
  @GetMapping("{page}/{limit}")
  public Ret index(
      @ApiParam(name = "page", value = "当前页码", required = true)
      @PathVariable Long page,

      @ApiParam(name = "limit", value = "每页记录数", required = true)
      @PathVariable Long limit,

      @ApiParam(name = "courseQuery", value = "查询对象", required = false)
      User userQueryVo) {
    Page<User> pageParam = new Page<>(page, limit);
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    if (!StringUtils.isEmpty(userQueryVo.getUsername())) {
      wrapper.like("username", userQueryVo.getUsername());
    }

    IPage<User> pageModel = userService.page(pageParam, wrapper);
    return Ret.ok().data("items", pageModel.getRecords()).data("total", pageModel.getTotal());
  }

  @ApiOperation(value = "新增管理用户")
  @PostMapping("save")
  public Ret save(@RequestBody User user) {
    user.setPassword(MD5.encrypt(user.getPassword()));
    userService.save(user);
    return Ret.ok();
  }

  @ApiOperation(value = "修改管理用户")
  @PutMapping("update")
  public Ret updateById(@RequestBody User user) {
    userService.updateById(user);
    return Ret.ok();
  }

  @ApiOperation(value = "删除管理用户")
  @DeleteMapping("remove/{id}")
  public Ret remove(@PathVariable String id) {
    userService.removeById(id);
    return Ret.ok();
  }

  @ApiOperation(value = "根据id列表删除管理用户")
  @DeleteMapping("batchRemove")
  public Ret batchRemove(@RequestBody List<String> idList) {
    userService.removeByIds(idList);
    return Ret.ok();
  }

  @ApiOperation(value = "根据用户获取角色数据")
  @GetMapping("/toAssign/{userId}")
  public Ret toAssign(@PathVariable String userId) {
    Map<String, Object> roleMap = roleService.findRoleByUserId(userId);
    return Ret.ok().data(roleMap);
  }

  @ApiOperation(value = "根据用户分配角色")
  @PostMapping("/doAssign")
  public Ret doAssign(@RequestParam String userId, @RequestParam String[] roleId) {
    roleService.saveUserRoleRealtionShip(userId, roleId);
    return Ret.ok();
  }
}

