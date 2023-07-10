package com.btwl.commonutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ResponseUtil {

  public static void out(HttpServletResponse response, Ret ret) {
    ObjectMapper mapper = new ObjectMapper();
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    try {
      mapper.writeValue(response.getWriter(), ret);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
