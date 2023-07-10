package com.btwl.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.btwl.servicebase.exceptionhandler.BrilliantCustomException;
import com.btwl.vod.Utils.ConstantVodUtils;
import com.btwl.vod.Utils.InitVodCilent;
import com.btwl.vod.service.VodService;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VodServiceImpl implements VodService {

  @Override
  public String uploadVideoAly(MultipartFile file) {
//        vod stands for oss-vod stands for Object Storage Service-Video on Demand
    try {
      //accessKeyId, accessKeySecret
      // 01.03.09.mp4
      String fileName = file.getOriginalFilename();
      //title：上传之后显示名称
      if (fileName == null) {
        return null; //todo: maybe the code  wont even run to here . if null, try snippets already catch IOexception ?
//                this if is just for next statement`s substring cmd not check warning before compile time .
      }
      String title = fileName.substring(0, fileName.lastIndexOf("."));
      InputStream inputStream = file.getInputStream();
      UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID,
          ConstantVodUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);

      UploadVideoImpl uploader = new UploadVideoImpl();
      UploadStreamResponse response = uploader.uploadStream(request);

      String videoId;
      if (response.isSuccess()) {
        videoId = response.getVideoId();
      } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
        videoId = response.getVideoId();
      }
      return videoId;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }

  @Override
  public void removeMoreAlyVideo(List videoIdList) {
    try {
      //初始化对象
      DefaultAcsClient client = InitVodCilent.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,
          ConstantVodUtils.ACCESS_KEY_SECRET);
      //创建删除视频request对象
      DeleteVideoRequest request = new DeleteVideoRequest();

      //videoIdList值转换成 1,2,3
      String videoIds = StringUtils.join(videoIdList.toArray(), ",");

      //向request设置视频id
      request.setVideoIds(videoIds);
      //调用初始化对象的方法实现删除
      client.getAcsResponse(request);
    } catch (Exception e) {
      e.printStackTrace();
      throw new BrilliantCustomException(20001, "删除视频失败");
    }
  }
}
