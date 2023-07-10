package com.btwl.aclservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.btwl.aclservice.entity.Role;
import com.btwl.aclservice.service.RoleService;
import com.btwl.commonutils.Ret;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/role")
//@CrossOrigin
public class RoleController {

  @Autowired
  private RoleService roleService;

  @ApiOperation(value = "获取角色分页列表")
  @GetMapping("{page}/{limit}")
  public Ret index(
      @ApiParam(name = "page", value = "当前页码", required = true)
      @PathVariable Long page,

      @ApiParam(name = "limit", value = "每页记录数", required = true)
      @PathVariable Long limit,
      Role role) {
    Page<Role> pageParam = new Page<>(page, limit);
    QueryWrapper<Role> wrapper = new QueryWrapper<>();
    if (!StringUtils.isEmpty(role.getRoleName())) {
      wrapper.like("role_name", role.getRoleName());
    }
    roleService.page(pageParam, wrapper);
    return Ret.ok().data("items", pageParam.getRecords()).data("total", pageParam.getTotal());
  }

  @ApiOperation(value = "获取角色")
  @GetMapping("get/{id}")
  public Ret get(@PathVariable String id) {
    Role role = roleService.getById(id);
    return Ret.ok().data("item", role);
  }

  @ApiOperation(value = "新增角色")
  @PostMapping("save")
  public Ret save(@RequestBody Role role) {
    roleService.save(role);
    return Ret.ok();
  }

  @ApiOperation(value = "修改角色")
  @PutMapping("update")
  public Ret updateById(@RequestBody Role role) {
    roleService.updateById(role);
    return Ret.ok();
  }

  @ApiOperation(value = "删除角色")
  @DeleteMapping("remove/{id}")
  public Ret remove(@PathVariable String id) {
    roleService.removeById(id);
    return Ret.ok();
  }

  @ApiOperation(value = "根据id列表删除角色")
  @DeleteMapping("batchRemove")
  public Ret batchRemove(@RequestBody List<String> idList) {
    roleService.removeByIds(idList);
    return Ret.ok();
  }
}

