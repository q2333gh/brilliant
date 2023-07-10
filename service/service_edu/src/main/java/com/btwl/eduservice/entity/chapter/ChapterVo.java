package com.btwl.eduservice.entity.chapter;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * chapter header msg id,title,children
 */
@Data
public class ChapterVo {

  private String id;

  private String title;

  //表示小节
  private List<VideoVo> children = new ArrayList<>();
}
