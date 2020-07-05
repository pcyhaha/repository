package com.huikedu.sys.domain;

import java.io.Serializable;

public class AdminUserRoles implements Serializable {

    private static final  long serialVersionUID=1L;

    private Long adminUserId;//用户角色
    private Long roleId;

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
