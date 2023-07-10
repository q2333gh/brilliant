package com.btwl.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.btwl.commonutils.Ret;
import com.btwl.educms.entity.CrmBanner;
import com.btwl.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
 * 后台banner管理接口
 * </p>
 *
 * @author testjava
 * @since 2020-03-07
 */
@RestController
@RequestMapping("/educms/banneradmin")
@CrossOrigin
public class BannerAdminController {

  @Autowired
  private CrmBannerService bannerService;

  //1 分页查询banner
  @GetMapping("pageBanner/{page}/{limit}")
  public Ret pageBanner(@PathVariable long page, @PathVariable long limit) {
    Page<CrmBanner> pageBanner = new Page<>(page, limit);
    bannerService.page(pageBanner, null);
    return Ret.ok().data("items", pageBanner.getRecords()).data("total", pageBanner.getTotal());
  }

  //2 添加banner
  @PostMapping("addBanner")
  public Ret addBanner(@RequestBody CrmBanner crmBanner) {
    bannerService.save(crmBanner);
    return Ret.ok();
  }

  @ApiOperation(value = "获取Banner")
  @GetMapping("get/{id}")
  public Ret get(@PathVariable String id) {
    CrmBanner banner = bannerService.getById(id);
    return Ret.ok().data("item", banner);
  }

  @ApiOperation(value = "修改Banner")
  @PutMapping("update")
  public Ret updateById(@RequestBody CrmBanner banner) {
    bannerService.updateById(banner);
    return Ret.ok();
  }

  @ApiOperation(value = "删除Banner")
  @DeleteMapping("remove/{id}")
  public Ret remove(@PathVariable String id) {
    bannerService.removeById(id);
    return Ret.ok();
  }
}

