package com.btwl.oss.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {

  //上传头像到oss
  String uploadFileAvatar(MultipartFile file) throws IOException;
}
