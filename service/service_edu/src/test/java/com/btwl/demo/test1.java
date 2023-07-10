package com.btwl.demo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@Component
@SpringBootApplication
public class test1 {

  private static String name;

  @Value("${testdata.tt}")
  public String s1;

  @Test
  public void out() {
    System.out.println(s1);
  }
}
