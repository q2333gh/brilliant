package com.btwl.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.btwl.oss.service.OssService;
import com.btwl.oss.utils.ConstantPropertiesUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OssServiceImpl implements OssService {

  private static String uuid32length() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * @return "yyyy/MM/dd"
   */
  private static String today_YMD() {
    return new DateTime().toString("yyyy/MM/dd");
  }

  private static String setURL(String endpoint, String bucketName, String fileName) {
    return "https://" + bucketName + "." + endpoint + "/" + fileName;
  }

  //上传头像到oss
  @Override
  public String uploadFileAvatar(MultipartFile file) throws IOException {
    // 工具类获取值
    String endpoint = ConstantPropertiesUtils.END_POIND;
    String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
    String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
    String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

    InputStream inputStream = null;
    OSS ossClient = null;
    try {
      // 创建OSS实例。
      ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

      //获取上传文件输入流 todo : 是否可以用java NIO 多路复用这个 inputSteam?减少销毁和重启
      inputStream = file.getInputStream();
      //获取文件名称
      String fileName = file.getOriginalFilename();

      //1 在文件名称里面添加随机唯一的值 todo: UUID 不是顺序增长的,对存储可能不是最高效取得,如snowflake可能快一些
      fileName = uuid32length() + fileName;           // yuy76t5rew01.jpg

      //拼接
      //  2019/11/12/ewtqr313401.jpg
      fileName = today_YMD() + "/" + fileName;
      //todo 做了三次字符串拼接, 是否用StringBuilder或者StringBuffer更快?

      //调用oss方法实现上传
      //第一个参数  Bucket名称
      //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
      //第三个参数  上传文件输入流
      ossClient.putObject(bucketName, fileName, inputStream);

      inputStream.close();
      ossClient.shutdown();

      //把上传之后文件路径返回
      //需要把上传到阿里云oss路径手动拼接出来
      //  https://edu-brilliant-1010.oss-cn-beijing.aliyuncs.com/01.jpg
      return setURL(endpoint, bucketName, fileName);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
//            todo : will this cause double free  pointer ? how to check ?
//            inputStream.close();
//            ossClient.shutdown();
    }
  }

  //todo will the java compiler *optimize* this 3 simple function?
  //since its not complicated,to make the stack call less ?

  //todo what is the necessary optimize?
  //how to measure the overall time ? quantitative approach :
  // scale: a business scale: from user click GUI to return data ?
  // divide the business scale to analyze : networking  time ? DB-IO time ? java time ?
  //  some tools may help: wireshark? java-flame-graph-profiler?
}
