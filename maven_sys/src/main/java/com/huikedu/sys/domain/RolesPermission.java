package com.huikedu.sys.domain;

import java.io.Serializable;

public class RolesPermission implements Serializable {

    private static final  long serialVersionUID=1L;

    private Long roleId;//角色id
    private Long permissionId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}
