package com.btwl.aclservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.btwl.aclservice.entity.RolePermission;
import com.btwl.aclservice.mapper.RolePermissionMapper;
import com.btwl.aclservice.service.RolePermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class RolePermissionServiceImpl extends
    ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}
