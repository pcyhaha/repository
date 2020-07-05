package com.huikedu.sys.controller;

import com.huikedu.sys.domain.Permission;
import com.huikedu.sys.services.PermissionService;
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
@RequestMapping("Permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    /*权限分页方法*/
    @RequiresPermissions("permission:list")
    @RequestMapping("getAllPermissions")
    public String ShowRoleList(HttpServletRequest request, @RequestParam(value = "currPage", required = false) Long currPage, @RequestParam(value = "pageSize", required = false) Long pageSize) {
        Long recoredRows = permissionService.selectCount();
        if (currPage == null) {
            currPage = 1L; //
        }
        if (pageSize == null) {
            pageSize = 10L; // 初始记录条数
        }
        Long totalPage = recoredRows % pageSize == 0 ? recoredRows / pageSize : recoredRows / pageSize + 1;
        Long startRecord = (currPage - 1) * pageSize;
        //查询当前页的区间记录
        List permissionlist = permissionService.selectByPage(startRecord, pageSize); // 10 条记录
        request.setAttribute("permissionlist", permissionlist);
        request.setAttribute("currPage", currPage);// 当前面
        request.setAttribute("pageSize", pageSize);//每页显示的记录数
        request.setAttribute("count", recoredRows);//总记录条数
        request.setAttribute("totalPage", totalPage);//总页数

        //返回 视图资源名称
        return "permission/list";
    }

    /*删除权限*/
    @RequiresPermissions("permission:delete")
    @RequestMapping("delete.do")
    public String roleDel(HttpServletRequest request,
                          @RequestParam(value="id",required=true)
                                  String id) {

        //假删除选中的权限信息
        int i = permissionService.deletePermissions(id);
        if (i>0){
            System.out.println("删除成功！");
        }
        //返回权限列表
        return "redirect:getAllPermissions";
    }

    /*添加权限*/
    @RequiresPermissions("permission:create")
    @RequestMapping(value ="permissionAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(HttpServletRequest request,
                                   @RequestParam(value = "permission", required = false)
                                           String permission,
                                   @RequestParam(value = "description", required = false)
                                           String description) {

        boolean b = permissionService.addPermission(permission, description);
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


    /*修改权限*/
    @RequiresPermissions("permission:update")
    @RequestMapping("permissionUpdate")
    @ResponseBody
    public  Map<String, Object> updateRole(HttpServletRequest request,
                                           @RequestParam(value="permission",required=true)
                                                   String permission,
                                           @RequestParam(value="description",required=true)
                                                   String description,
                                           @RequestParam(value="permissionID",required=true)
                                                   String permissionID) {


        Map<String, Object> resultMap = new HashMap<>();

        //修改角色信息
        Permission permission1 = new Permission();
        permission1.setId(Long.parseLong(permissionID));
        permission1.setDescription(description);
        permission1.setPermission(permission);

        int i = permissionService.updatePermission(permission1);

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


}
