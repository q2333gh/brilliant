//高版本MP不再需要以下配置,自动生效
//package com.btwl.eduservice.config;
//
//import com.baomidou.mybatisplus.core.injector.ISqlInjector;
//import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@EnableTransactionManagement
//@Configuration
//@MapperScan("com.btwl.eduservice.mapper")
//public class EduConfig {
//
//  /**
//   * 逻辑删除插件
//   */
//  @Bean
//  public ISqlInjector sqlInjector() {
//    return new LogicSqlInjector();
//  }
//
//  /**
//   * 分页插件
//   */
//  @Bean
//  public PaginationInterceptor paginationInterceptor() {
//    return new PaginationInterceptor();
//  }
//}
