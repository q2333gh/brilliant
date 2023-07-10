package com.btwl.demo;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestJava2Mysql {


  @Value("${testdata}")
  public String td;

  @Test
  public void run() {

  }
}

