package com.huikedu.sys.services;

/*
 用户业务组件
 */

import com.huikedu.sys.domain.AdminUser;
import com.huikedu.sys.domain.AdminUserRoles;
import com.huikedu.sys.mapper.AdminUserMapper;
import com.huikedu.sys.mapper.Imaper;
import com.huikedu.sys.utils.ShiroUtils;
import org.omg.CORBA.LongHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("adminUserService")
public class AdminUserServiceImpl extends  IBaseSeviceImpl implements  AdminUserService {

    @Autowired
    AdminUserMapper adminUserMapper;

    Map<String,Long> maps = new HashMap<>();

    @Override
    public List<AdminUser> selectByPage(Long currPage, Long pageSize) {
            maps.put("currPage", currPage);
            maps.put("pageSize",pageSize);
            return adminUserMapper.selectByPage(maps);
    }

    @Override
    public Long selectCount() {
        return adminUserMapper.selectCount();
    }

    @Override
    public boolean addAdminUserRole(String account, String password, String checkpid) {
        try {
            AdminUser adu = new AdminUser();
            adu.setAccount(account);
            adu.setPassword(ShiroUtils.getStrByMD5(password));
            adu.setPasswordSalt(ShiroUtils.getStrByMD5(password));
            adu.setIsDeleted(false);
            adu.setIsDisabled(false);
            // 得到刚保存的Adminuser对象的Id
            Map<String, Object> resultMap = new HashMap<>();
            adminUserMapper.insert(adu);

            //**迭代前台传入的角色ID，通过,拼加的字符串，在后台拆分出来就是单个角色的ID*//*
            if (checkpid != null && checkpid != "") {
                String[] checkPid = checkpid.split(",");
                for (String check : checkPid) {
                    AdminUserRoles adminUserRole = new AdminUserRoles();
                    adminUserRole.setAdminUserId(adu.getId());
                    adminUserRole.setRoleId(Long.parseLong(check));
                    adminUserMapper.addAdminUserRole(adminUserRole);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public boolean checkname(String account) {

        AdminUser adminUser = new AdminUser();
        adminUser.setAccount(account);
        List adminUserList = adminUserMapper.select(adminUser);

        if (adminUserList == null || adminUserList.size()==0){
            // 不存在重复
            return  true;
        }
        //存在重复
        return false;
    }

    @Override
    public List<AdminUserRoles> selectUserRoles(AdminUserRoles adminUserRoles) {
        return adminUserMapper.selectUserRoles(adminUserRoles);
    }


    /*删除用户*/
    @Override
    public int deleteAdmin(String id) {
        return adminUserMapper.delete(Long.parseLong(id));
    }



    /*修改之前先查询出用户的信息和角色*/

    /*启用或禁用*/
    @Override
    public List select(AdminUser adminUser) {
        return adminUserMapper.select(adminUser);
    }

    /*改变启用或禁用*/
    @Override
    public int updateisDisabled(AdminUser adminUser) {
        return adminUserMapper.updateisDisabled(adminUser);
    }

    /*修改密码*/
    @Override
    public int updatePassword(AdminUser adminUser) {
        return adminUserMapper.update(adminUser);
    }

    /*验证原密码是否正确*/
    @Override
    public List checkPassword(AdminUser adminUser) {
        return adminUserMapper.select(adminUser);
    }

    @Override
    public boolean updateAdminUserRolesubmit(String adminUserId, String roleId[]) {

        AdminUserRoles adminUserRoles = new AdminUserRoles();
        adminUserRoles.setAdminUserId(Long.parseLong(adminUserId));
        //删除中间表
        boolean b = adminUserMapper.delAdminUserRole(adminUserRoles);
        if(b){
            System.out.println("删除成功！");
        }
        if(roleId !=null && !"".equals(roleId)){
                System.out.println("开始添加");
                //3、重新添加
                for (String roleid : roleId) {
                    AdminUserRoles adminUserrole = new AdminUserRoles();//用户角色中间表对象
                    adminUserrole.setAdminUserId(Long.parseLong(adminUserId));
                    adminUserrole.setRoleId(Long.parseLong(roleid));
                    adminUserMapper.addAdminUserRole(adminUserrole);
                }
        }
        return true;
    }


    @Override
    public AdminUser QueryUserByName(String username) {
        return adminUserMapper.QueryUserByName(username);
    }

    @Override
    public List<String> findRolesByUserName(String username) {
        AdminUser  adminUser =  adminUserMapper.QueryUserByName(username);
        if (adminUser == null ){
            return Collections.EMPTY_LIST;
        }
        return adminUserMapper.findRolesById(adminUser.getId());
    }

    @Override
    public List<String> findPermissionsByUserName(String username) {
        AdminUser  adminUser =  adminUserMapper.QueryUserByName(username);
        if (adminUser == null ){
            return Collections.EMPTY_LIST;
        }
        return adminUserMapper.findPermissionsById(adminUser.getId());
    }
}
