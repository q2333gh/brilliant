package com.btwl.serurity.security;

import com.btwl.commonutils.ResponseUtil;
import com.btwl.commonutils.Ret;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * <p>
 * 未授权的统一处理方式
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    ResponseUtil.out(response, Ret.error());
  }
}
