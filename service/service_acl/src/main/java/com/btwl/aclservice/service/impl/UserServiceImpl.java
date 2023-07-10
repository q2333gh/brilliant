package com.btwl.aclservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.btwl.aclservice.entity.User;
import com.btwl.aclservice.mapper.UserMapper;
import com.btwl.aclservice.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Override
  public User selectByUsername(String username) {
    return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
  }
}
