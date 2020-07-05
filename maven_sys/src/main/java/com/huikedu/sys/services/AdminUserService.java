package com.huikedu.sys.services;

import com.huikedu.sys.domain.AdminUser;
import com.huikedu.sys.domain.AdminUserRoles;

import java.util.List;

public interface AdminUserService extends  IBaseService {

    /*
    查询指定区间的记录
     */
    List<AdminUser> selectByPage(Long currPage, Long pageSize );

        /*
        查询总记录数
         */
    Long selectCount();

    /*添加用户角色信息*/
    boolean addAdminUserRole(String account, String password, String checkpid);

    /*重名验证*/
    boolean checkname(String account);

    /* 通过用户id和角色id查询出对应用户的角色信息*/
    List<AdminUserRoles> selectUserRoles(AdminUserRoles adminUserRoles);

    /*删除用户*/
    int deleteAdmin(String id);

    /*启用或禁用前查询*/
    List<AdminUser> select(AdminUser adminUser);

    /*启用或禁用*/
    int updateisDisabled(AdminUser adminUser);

    /*修改密码*/
    int updatePassword(AdminUser adminUser);

    List checkPassword(AdminUser adminUser);

    boolean updateAdminUserRolesubmit(String adminUserId, String roleId[]);

    /**
     *查询出当前用户对象
     */
    AdminUser QueryUserByName(String username );

    /**
     * 查询当前账号的所有角色
     */
    List<String> findRolesByUserName(String username);

    /**
     * 查询当前账号的所有权限
     */
    List<String> findPermissionsByUserName(String username);
}
