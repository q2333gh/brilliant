package com.btwl.eduservice.entity.subject;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

//一级分类
@Data
public class OneSubject {

  private String id;
  private String title;

  //一个一级分类有多个二级分类
  private List<TwoSubject> children = new ArrayList<>();
}
