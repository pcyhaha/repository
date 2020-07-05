package com.huikedu.sys.services;

import com.huikedu.sys.domain.Permission;

import java.util.List;

public interface PermissionService extends IBaseService{
    /*查询所有的权限*/
    List<Permission> getAllPermission();

    /*查询指定区间的记录*/
    List<Permission> selectByPage(Long currPage, Long pageSize );

    /*查询总记录数*/
    Long selectCount();

    /*删除权限*/
    int deletePermissions(String id);

    /*添加权限*/
    boolean addPermission(String permission, String description);

    /*修改权限信息*/
    int updatePermission(Permission permission);
}
