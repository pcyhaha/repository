package com.huikedu.sys.services;


import com.huikedu.sys.domain.Role;
import com.huikedu.sys.domain.RolesPermission;

import java.util.List;

public interface RoleService extends IBaseService{
    List<Role> getallRole();

    /*
   查询指定区间的记录
    */
    List<Role> selectByPage(Long currPage, Long pageSize );

    /*
    查询总记录数
     */
    Long selectCount();

    /*删除角色*/
    int deleteRoles(String id);

    /*添加角色*/
    boolean addRole(String roleName, String description, String checkpid);

    /*修改角色信息*/
    int updateRole(Role role);

    List<Role> getRoleByRoleID(Role role);

    /*查询关联表*/
    List<RolesPermission> getRolesPermission(RolesPermission rolesPermission);

    /*更新中间表*/
    boolean updateRolesubmit(String roleId, String[] permissionId);
}
