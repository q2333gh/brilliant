package com.btwl.aclservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色权限
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("acl_role_permission")
@ApiModel(value = "RolePermission对象", description = "角色权限")
public class RolePermission implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private String id;

  private String roleId;

  private String permissionId;

  @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
  private Boolean isDeleted;

  @ApiModelProperty(value = "创建时间")
  private Date gmtCreate;

  @ApiModelProperty(value = "更新时间")
  private Date gmtModified;


}
