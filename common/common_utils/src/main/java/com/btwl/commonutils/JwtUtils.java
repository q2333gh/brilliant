package com.btwl.commonutils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;


public class JwtUtils {

  public static final long EXPIRE = 1000 * 60 * 60 * 24; //token过期时间
  public static final String APP_SECRET =
      "ukc8BDbRigUDaY6pZFfWus2jZWLPHO"; //秘钥

  //生成token字符串的方法
  public static String getJwtToken(String id, String nickname) {

    return Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setHeaderParam("alg", "HS256")

        .setSubject("brilliant-user")
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))

        .claim("id", id)  //设置token主体部分 ，存储用户信息
        .claim("nickname", nickname)

        .signWith(SignatureAlgorithm.HS256, APP_SECRET)
        .compact();
  }

  /**
   * 判断token是否存在与有效
   */
  public static boolean checkToken(String jwtToken) {
    if (StringUtils.isEmpty(jwtToken)) {
      return false;
    }
    try {
      Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 判断token是否存在与有效
   */
  public static boolean checkToken(HttpServletRequest request) {
    try {
      String jwtToken = request.getHeader("token");
      if (StringUtils.isEmpty(jwtToken)) {
        return false;
      }
      Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 根据token字符串获取会员id
   */
  public static String getMemberIdByJwtToken(HttpServletRequest request) {
    String jwtToken = request.getHeader("token");
    if (StringUtils.isEmpty(jwtToken)) {
      return "";
    }
    Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
    Claims claims = claimsJws.getBody();
    return (String) claims.get("id");
  }
}
