//package com.btwl.servicebase;
//
//import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
//import org.springframework.context.annotation.Bean;
//
//
//public class MybatisPlusConfig {
//
//  @Bean
//  public PerformanceInterceptor performanceInterceptor() {
//    PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
//    performanceInterceptor.setMaxTime(100);//ms
//    performanceInterceptor.setFormat(true);
//    return performanceInterceptor;
//  }
//}