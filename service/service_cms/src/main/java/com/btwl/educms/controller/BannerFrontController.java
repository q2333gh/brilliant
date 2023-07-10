package com.btwl.educms.controller;


import com.btwl.commonutils.Ret;
import com.btwl.educms.entity.CrmBanner;
import com.btwl.educms.service.CrmBannerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前台bannber显示
 * </p>
 *
 * @author testjava
 * @since 2020-03-07
 */
@RestController
@RequestMapping("/educms/bannerfront")
@CrossOrigin
public class BannerFrontController {

  @Autowired
  private CrmBannerService bannerService;

  //查询所有banner
  @GetMapping("getAllBanner")
  public Ret getAllBanner() {
    List<CrmBanner> list = bannerService.selectAllBanner();
    return Ret.ok().data("list", list);
  }
}

