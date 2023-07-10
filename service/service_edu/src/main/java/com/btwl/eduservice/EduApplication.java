package com.btwl.eduservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient  //nacos注册 , 可能启用了配置中心. 自行检查.
@EnableFeignClients
//@ComponentScan(basePackages = {"com.btwl"})
//包扫描,如Swagger2的@Configuration扫描 ,mapper扫描等
//@Configuration
//@MapperScan(value = "com.btwl.eduservice.mapper.EduVideoMapper")
@MapperScan(value = "com.btwl.eduservice.mapper")
public class EduApplication {

  public static void main(String[] args) {

    SpringApplication.run(EduApplication.class, args);
  }
}
