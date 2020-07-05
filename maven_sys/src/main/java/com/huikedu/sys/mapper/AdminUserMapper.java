package com.huikedu.sys.mapper;

import com.huikedu.sys.domain.AdminUser;
import com.huikedu.sys.domain.AdminUserRoles;

import java.util.List;
import java.util.Map;

public interface AdminUserMapper  extends  Imaper{

    /*
    实现分页查询的方法
     */
    List<AdminUser> selectByPage(Map<String, Long> maps);

    /*
    查询总记录条数
     */
    Long selectCount();

    /*添加用户角色信息*/
    boolean addAdminUserRole(AdminUserRoles adminUserRoles);

    /* 通过用户id和角色id查询出对应用户的角色信息*/
    List<AdminUserRoles> selectUserRoles(AdminUserRoles adminUserRoles);

    /*启用或禁用*/
    int updateisDisabled(AdminUser adminUser);

    boolean delAdminUserRole(AdminUserRoles adminUserRoles);

    /**
     * 查询账号
     */
    AdminUser QueryUserByName(String account);

    /*
       查询当前用户的角色
        */
    List<String> findRolesById(Long adminUserId);
    /**
     * 查询当前用户的所有权限
     */
    List<String> findPermissionsById(Long adminUserId);
}
