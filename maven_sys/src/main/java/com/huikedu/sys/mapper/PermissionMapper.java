package com.huikedu.sys.mapper;

import com.huikedu.sys.domain.Permission;
import com.huikedu.sys.domain.Role;

import java.util.List;
import java.util.Map;

public interface PermissionMapper extends Imaper<Permission>{

    /*查询所有的权限*/
    List<Permission> getAllPermission();

    /*实现分页查询的方法*/
    List<Permission> selectByPage(Map<String, Long> maps);

    /*查询总记录条数*/
    Long selectCount();


}
