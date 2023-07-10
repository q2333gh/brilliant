package com.btwl.educenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.btwl.educenter.entity.UcenterMember;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-03-09
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

  //查询某一天注册人数
  Integer countRegisterDay(String day);
}
