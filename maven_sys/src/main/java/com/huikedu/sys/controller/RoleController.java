package com.huikedu.sys.controller;

import com.huikedu.sys.domain.Permission;
import com.huikedu.sys.domain.Role;
import com.huikedu.sys.domain.RolesPermission;
import com.huikedu.sys.services.AdminUserService;
import com.huikedu.sys.services.PermissionService;
import com.huikedu.sys.services.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("Role")
public class RoleController {

    @Autowired
    PermissionService permissionService;

    @Autowired
    AdminUserService adminUserService;

    @Autowired
    RoleService roleService;

    @RequiresPermissions("role:list")
    @RequestMapping("ShowRoleList.do")
    public String ShowRoleList(HttpServletRequest request, @RequestParam(value = "currPage", required = false) Long currPage, @RequestParam(value = "pageSize", required = false) Long pageSize) {
        Long recoredRows = roleService.selectCount();
        if (currPage == null) {
            currPage = 1L; //
        }
        if (pageSize == null) {
            pageSize = 10L; // 初始记录条数
        }
        Long totalPage = recoredRows % pageSize == 0 ? recoredRows / pageSize : recoredRows / pageSize + 1;
        Long startRecord = (currPage - 1) * pageSize;
        //查询当前页的区间记录
        List RoleList = roleService.selectByPage(startRecord, pageSize); // 10 条记录
        request.setAttribute("RoleList", RoleList);
        request.setAttribute("currPage", currPage);// 当前面
        request.setAttribute("pageSize", pageSize);//每页显示的记录数
        request.setAttribute("count", recoredRows);//总记录条数
        request.setAttribute("totalPage", totalPage);//总页数

        //返回 视图资源名称
        return "Role/list";
    }

    /*删除角色*/
    @RequiresPermissions("role:delete")
    @RequestMapping("delete.do")
    public String roleDel(HttpServletRequest request,
                           @RequestParam(value="id",required=true)
                                   String id) {

        //假删除选中角色信息
        int i = roleService.deleteRoles(id);
        if (i>0){
            System.out.println("删除成功！");
        }
        //返回用户列表
        return "redirect:ShowRoleList.do";
    }

    /*添加角色时候查询所有的权限返回到add.jsp*/
    @RequiresPermissions("role:createRole")
    @RequestMapping("roleAdd.do")
    public String ToAddJsp(HttpServletRequest request) {
        //查询所有权限信息
        List<Permission> allPermission = permissionService.getAllPermission();
        request.setAttribute("allPermissionList", allPermission);
        return "Role/add";
    }

    //处理添加角色的数据
    @RequiresPermissions("role:createRole")
    @RequestMapping(value ="AddRole.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(HttpServletRequest request,
                                   @RequestParam(value = "roleName", required = false)
                                           String roleName,
                                   @RequestParam(value = "description", required = false)
                                           String description,
                                   @RequestParam(value = "checkPID")
                                               String checkPID) {

        System.out.println(roleName+description+checkPID);

        boolean b = roleService.addRole(roleName, description, checkPID);

        Map<String, Object> resultMap = new HashMap<>();

        if(b){
            resultMap.put("status", 200);
            resultMap.put("message", "添加成功！");
        } else {
            resultMap.put("status", 500);
            resultMap.put("message", "添加失败！");
        }
        return resultMap;
    }

    /*修改角色*/
    @RequiresPermissions("role:updaterole")
    @RequestMapping("updateRole.do")
    @ResponseBody
    public  Map<String, Object> updateRole(HttpServletRequest request,
                          @RequestParam(value="roleID",required=true)
                                     String roleID,
                          @RequestParam(value="roleName",required=true)
                                  String roleName,
                          @RequestParam(value="description",required=true)
                                  String description) {


        Map<String, Object> resultMap = new HashMap<>();

        //修改角色信息
        Role role = new Role();
        role.setId(Long.parseLong(roleID));
        role.setDescription(description);
        role.setName(roleName);

        int i = roleService.updateRole(role);

        if (i>0){
            resultMap.put("status",200);
            resultMap.put("message","修改成功！");
        }else{
            resultMap.put("status",204);
            resultMap.put("message","修改失败！");
        }
        //返回
        return resultMap;
    }

    /*修改权限前列出所有权限*/
    @RequiresPermissions("role:updateResource")
    @RequestMapping(value="goRolePermission")
    public  String getAdminUserInfo(HttpServletRequest request, @RequestParam(value="roleId",required=true)
            String roleId
         ) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();

        if (roleId == null || "".equals(roleId)) {
            throw new Exception("当前角色ID为空");
        }
        else {
            /*通过角色的id，查询对应的权限*/
            /*查询中间表*/
            RolesPermission rolesPermission = new RolesPermission();
            rolesPermission.setRoleId(Long.parseLong(roleId));
            List<RolesPermission> rop= roleService.getRolesPermission(rolesPermission);

            //查询出所有权限列表返回update页面
            List<Permission> allPermissionList = permissionService.getAllPermission();
            request.setAttribute("roleId",roleId);
            request.setAttribute("allPermissionList",allPermissionList);
            request.setAttribute("rop",rop);
        }
        //返回修改页面
        return "Role/update";
    }
    /*修改权限*/
    @RequiresPermissions("role:updateResource")
    @RequestMapping("updatePermission")
    public String updatePermission(HttpServletRequest request,
                                      @RequestParam(value="roleId",required=true)
                                              String roleId,
                                      @RequestParam(value="permissionId",required=false)
                                              String permissionId[]) throws Exception {

        System.out.println(roleId);
        System.out.println(permissionId);
        if (roleId == null || "".equals(roleId)) {
            throw new Exception("当前角色ID为空");
        }
        if(permissionId==null|| "".equals(permissionId)) {
            return "redirect:ShowRoleList.do";
        } else {
            roleService.updateRolesubmit(roleId, permissionId);
            //返回用户列表
            return "redirect:ShowRoleList.do";
        }
    }
}
