package com.btwl.aclservice.controller;


import com.btwl.aclservice.entity.Permission;
import com.btwl.aclservice.service.PermissionService;
import com.btwl.commonutils.Ret;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 权限 菜单管理
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/admin/acl/permission")
//@CrossOrigin
public class PermissionController {

  @Autowired
  private PermissionService permissionService;

  //获取全部菜单
  @ApiOperation(value = "查询所有菜单")
  @GetMapping
  public Ret indexAllPermission() {
    List<Permission> list = permissionService.queryAllMenu();
    return Ret.ok().data("children", list);
  }

  @ApiOperation(value = "递归删除菜单")
  @DeleteMapping("remove/{id}")
  public Ret remove(@PathVariable String id) {
    permissionService.removeChildById(id);
    return Ret.ok();
  }

  @ApiOperation(value = "给角色分配权限")
  @PostMapping("/doAssign")
  public Ret doAssign(String roleId, String[] permissionId) {
    permissionService.saveRolePermissionRealtionShipbrilliant(roleId, permissionId);
    return Ret.ok();
  }

  @ApiOperation(value = "根据角色获取菜单")
  @GetMapping("toAssign/{roleId}")
  public Ret toAssign(@PathVariable String roleId) {
    List<Permission> list = permissionService.selectAllMenu(roleId);
    return Ret.ok().data("children", list);
  }


  @ApiOperation(value = "新增菜单")
  @PostMapping("save")
  public Ret save(@RequestBody Permission permission) {
    permissionService.save(permission);
    return Ret.ok();
  }

  @ApiOperation(value = "修改菜单")
  @PutMapping("update")
  public Ret updateById(@RequestBody Permission permission) {
    permissionService.updateById(permission);
    return Ret.ok();
  }

}

