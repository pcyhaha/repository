package com.huikedu.sys.mapper;

import com.huikedu.sys.domain.Role;
import com.huikedu.sys.domain.RolesPermission;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends Imaper<Role>{
    List<Role> getallRole();

    /*
    实现分页查询的方法
   */
    List<Role> selectByPage(Map<String, Long> maps);

    /*
    查询总记录条数
    */
    Long selectCount();

    /*添加关联表信息*/
    boolean addRolePermission(RolesPermission permission);

    /*查询关联表*/
    List<RolesPermission> getRolesPermission(RolesPermission rolesPermission);

    /*删除中间表*/
    boolean delRolePermission(RolesPermission rolesPermission);
}
