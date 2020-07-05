package com.huikedu.sys.services;

import com.huikedu.sys.domain.Permission;
import com.huikedu.sys.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PermissionService")
public class PermissionServiceImpl extends IBaseSeviceImpl implements PermissionService{

    @Autowired
    private PermissionMapper permissionMapper;
    Map<String,Long> maps = new HashMap<>();

    /*查询所有的权限*/
    @Override
    public List<Permission> getAllPermission() {
        return permissionMapper.getAllPermission();
    }

    @Override
    public List<Permission> selectByPage(Long currPage, Long pageSize) {
        maps.put("currPage", currPage);
        maps.put("pageSize",pageSize);
        return permissionMapper.selectByPage(maps);
    }

    @Override
    public Long selectCount() {
        return permissionMapper.selectCount();
    }

    /*删除权限*/
    @Override
    public int deletePermissions(String id) {
        return permissionMapper.delete(Long.parseLong(id));
    }

    /*添加权限*/
    @Override
    public boolean addPermission(String permission, String description) {
        /*添加角色*/
        try {
            Permission permission1 = new Permission();
            permission1.setPermission(permission);
            permission1.setDescription(description);
            permissionMapper.insert(permission1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*修改权限*/
    @Override
    public int updatePermission(Permission permission) {
        return permissionMapper.update(permission);
    }
}
