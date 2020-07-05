package com.huikedu.sys.services;


import com.huikedu.sys.domain.Role;
import com.huikedu.sys.domain.RolesPermission;
import com.huikedu.sys.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roleService")
public class RoleServiceImpl extends IBaseSeviceImpl implements RoleService{

    @Autowired
    private RoleMapper roleMapper;
    Map<String,Long> maps = new HashMap<>();

    public List<Role> getallRole(){
        return roleMapper.getallRole();
    }

    @Override
    public List<Role> selectByPage(Long currPage, Long pageSize) {
        maps.put("currPage", currPage);
        maps.put("pageSize",pageSize);
        return roleMapper.selectByPage(maps);
    }

    @Override
    public Long selectCount() {
        return roleMapper.selectCount();
    }

    @Override
    public int deleteRoles(String id) {
        return roleMapper.delete(Long.parseLong(id));
    }

    @Override
    public boolean addRole(String roleName, String description, String checkpid) {

        try {
            /*添加角色*/
            Role role = new Role();
            role.setName(roleName);
            role.setDescription(description);
            roleMapper.insert(role);

            /*添加关联表*/
            if (checkpid != null && checkpid != "") {
                String[] checkPid = checkpid.split(",");
                for (String check : checkPid) {
                    RolesPermission rolesPermission = new RolesPermission();
                    rolesPermission.setRoleId(role.getId());
                    rolesPermission.setPermissionId(Long.parseLong(check));
                    roleMapper.addRolePermission(rolesPermission);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int updateRole(Role role) {
        return  roleMapper.update(role);
    }

    @Override
    public List<Role> getRoleByRoleID(Role role) {
        return null;
    }

    @Override
    public List<RolesPermission> getRolesPermission(RolesPermission rolesPermission) {
        return roleMapper.getRolesPermission(rolesPermission);
    }

    @Override
    public boolean updateRolesubmit(String roleId, String[] permissionId) {
        RolesPermission rolesPermission = new RolesPermission();
        rolesPermission.setRoleId(Long.parseLong(roleId));
        //删除中间表
        boolean b = roleMapper.delRolePermission(rolesPermission);
        if(b){
            System.out.println("删除成功！");
            }
        if(roleId !=null && !"".equals(roleId)){
            System.out.println("开始添加");
            //3、重新添加
            for (String p : permissionId) {
                System.out.println("角色id"+roleId);
                System.out.println("checkid"+p);
                RolesPermission rop = new RolesPermission();
                rop.setRoleId(Long.parseLong(roleId));
                rop.setPermissionId(Long.parseLong(p));
                roleMapper.addRolePermission(rop);
                System.out.println("添加成功");
            }
        }   return true;
    }
}
