package com.huikedu.sys.controller;

import com.huikedu.sys.domain.AdminUser;
import com.huikedu.sys.domain.AdminUserRoles;
import com.huikedu.sys.domain.Role;
import com.huikedu.sys.mapper.AdminUserMapper;
import com.huikedu.sys.mapper.Imaper;
import com.huikedu.sys.services.AdminUserService;
import com.huikedu.sys.services.AdminUserServiceImpl;
import com.huikedu.sys.services.IBaseService;
import com.huikedu.sys.services.RoleService;
import com.huikedu.sys.utils.ShiroUtils;
import com.sun.mail.imap.protocol.ID;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("adminUser")
public class AdminUserController {
    @Autowired
    AdminUserService adminUserService;

    @Autowired
    RoleService roleService;

    @Autowired
    AdminUserMapper adminUserMapper ;

    @Autowired
    Imaper imaper;

    @RequiresPermissions("adminUser:list")
    @RequestMapping("dolist.do")
    public String dolist(HttpServletRequest request, @RequestParam(value = "currPage", required = false) Long currPage, @RequestParam(value = "pageSize", required = false) Long pageSize) {
        Long recoredRows = adminUserService.selectCount();
        if (currPage == null) {
            currPage = 1L; //
        }
        if (pageSize == null) {
            pageSize = 15L; // 初始记录条数
        }
        Long totalPage = recoredRows % pageSize == 0 ? recoredRows / pageSize : recoredRows / pageSize + 1;
        Long startRecord = (currPage - 1) * pageSize;
        //查询当前页的区间记录
        List<AdminUser> adminUserlist = adminUserService.selectByPage(startRecord, pageSize); // 10 条记录
        request.setAttribute("adminUserList", adminUserlist);
        request.setAttribute("currPage", currPage);// 当前面
        request.setAttribute("pageSize", pageSize);//每页显示的记录数
        request.setAttribute("count", recoredRows);//总记录条数
        request.setAttribute("totalPage", totalPage);//总页数

        /*     for (AdminUser adminUser : adminUserlist) {*/
        /*         System.out.println(adminUser);*/
        /*     }*/
        //返回 视图资源名称
        return "adminUser/list";
    }

    @RequiresPermissions("adminUser:create")
    @RequestMapping("toadd.do")
    public String toadd(HttpServletRequest request) {
        //查询所有角色信息
        List rolelist = roleService.getallRole();

        request.setAttribute("roleList", rolelist);
        //添加用户，查询用户列表
        return "adminUser/add";
    }


    //处理添加的数据
    @RequiresPermissions("adminUser:create")
    @RequestMapping(value = "doadd.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> add(HttpServletRequest request, RedirectAttributes attr,
                                   @RequestParam(value = "account", required = false)
                                           String account,
                                   @RequestParam(value = "password", required = false)
                                           String password,
                                   @RequestParam(value = "checkPID")
                                           String checkPID) {


        boolean b = adminUserService.addAdminUserRole(account, password, checkPID);

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

    /**
     *@Description:添加用户时验证是否已存在
     */
    @RequiresPermissions("adminUser:create")
    @RequestMapping(value="checkUserName.do",method=RequestMethod.POST)
    @ResponseBody
    public Map<String,Object>checkUserName(@RequestParam(value="account",required=false)
                                          String account){
        Map<String, Object> resultMap = new HashMap<>();
        //调用service层的验证重名方法
        boolean check = adminUserService.checkname(account);

        if(check){
            resultMap.put("status",200);
            resultMap.put("message",true);
        }else{
            resultMap.put("status",204);
            resultMap.put("message",false);
        }
        return resultMap;
    }

    /*删除用户*/
    @RequiresPermissions("adminUser:delete")
    @RequestMapping("delete.do")
    public String todelete(HttpServletRequest request,
     @RequestParam(value="id",required=true)
             String id) {

        //假删除选中角色信息
        int i = adminUserService.deleteAdmin(id);
        if (i>0){
            System.out.println("删除成功！");
        }
        //返回用户列表
        return "redirect:dolist.do";
    }

    /*禁用或启用*/
    @RequiresPermissions("adminUser:updateDisabled")
    @RequestMapping("updateDisabled.do")
    public String updateDisabled(HttpServletRequest request,
                           @RequestParam(value="id",required=true)
                                   String id) {
        System.out.println(id);

        //查询当前状态用户是否是启用或禁用

        AdminUser ad = new AdminUser();
        ad.setId(Long.parseLong(id));

        List<AdminUser> select = adminUserService.select(ad);
        for (AdminUser adminUser : select) {
            boolean isDisabled = adminUser.getIsDisabled();
            System.out.println(isDisabled);

            if (isDisabled){
                //如果是true，改为false
                ad.setIsDisabled(false);
                adminUserService.updateisDisabled(ad);
            }
            else {
                //如果是false，改为true
                ad.setIsDisabled(true);
                adminUserService.updateisDisabled(ad);
            }
        }
        //返回用户列表
        return "redirect:dolist.do";
    }

    /*原密码验证*/
    @RequiresPermissions("adminUser:updatePassWord")
    @RequestMapping("checkpassword.do")
    @ResponseBody
    public Map<String,Object>checkUserName(@RequestParam(value="userID",required=true)
                                           String id,
                                           @RequestParam(value="password",required=true)
                                           String password
                                           ){
        Map<String, Object> resultMap = new HashMap<>();
        //调用service层的验证原密码是否正确

        System.out.println(id+password);
        AdminUser adminUser = new AdminUser();
        adminUser.setId(Long.parseLong(id));
        adminUser.setPassword(ShiroUtils.getStrByMD5(password));

        List<AdminUser> select = adminUserService.select(adminUser);

        if (select.size()==1){
            System.out.println("密码一致");
            resultMap.put("status",200);
            resultMap.put("message",true);
        }else{
            System.out.println("密码不一致");
            resultMap.put("status",204);
            resultMap.put("message",false);
        }
        return resultMap;
    }

    /*更新密码*/
    @RequiresPermissions("adminUser:updatePassWord")
    @RequestMapping("update.do")
    @ResponseBody
    public  Map<String, Object> todelete(HttpServletRequest request,
                           @RequestParam(value="userID",required=true)
                           String id,
                           @RequestParam(value="newpassword",required=true)
                           String password
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        AdminUser adminUser = new AdminUser();
        adminUser.setId(Long.parseLong(id));
        adminUser.setPassword(ShiroUtils.getStrByMD5(password));

        int i = adminUserService.updatePassword(adminUser);

        //返回用户列表

        if (i>0){
            resultMap.put("status",200);
            resultMap.put("message","修改成功！");
        }else{
            resultMap.put("status",204);
            resultMap.put("message","修改失败！");
        }
        return resultMap;
    }

    /**
     *@Description:通过用户的id得到用户的的信息
     */
    @RequiresPermissions("adminUser:updateAdminUserRole")
    @RequestMapping(value="getAdminUserInfo.do")

    public  String getAdminUserInfo(HttpServletRequest request, @RequestParam(value="id",required=true)
            String adminUserId){

        Map<String, Object> resultMap = new HashMap<>();
        //查询出对应用户id的角色信息
        AdminUserRoles adminUserRoles = new AdminUserRoles();
        adminUserRoles.setAdminUserId(Long.parseLong(adminUserId));
        List aduRoles = adminUserService.selectUserRoles(adminUserRoles);

        //查询出对应用户的用户名和密码
        AdminUser adminUser = new AdminUser();
        adminUser.setId(Long.parseLong(adminUserId));
        List adminUserList = adminUserMapper.select(adminUser);

        //查询出所有角色信息列表
        List allrolelist = roleService.getallRole();

        request.setAttribute("AdminUserRolesList",aduRoles);
        request.setAttribute("adminUserList",adminUserList);
        request.setAttribute("allrolelist",allrolelist);
        request.setAttribute("adminUserId",adminUserId);
        //测试
        /*resultMap.put("AdminUserRolesList",aduRoles);
        resultMap.put("adminUserList",adminUserList);
        resultMap.put("allrolelist",allrolelist);*/
        //resultMap "adminUser/update"
        return "adminUser/update";
    }

    /*更新用户角色表
    * 1：根据id删除原有的数据
    * 2：根据id重新添加
    * */
    @RequiresPermissions("adminUser:updateAdminUserRole")
    @RequestMapping("updateAdminUserRole.do")
    public String updateAdminUserRole(HttpServletRequest request,
      @RequestParam(value="adminUserId",required=true)
                                         String adminUserId,
      @RequestParam(value="roleId",required=false)
                                         String roleId[]) throws Exception {
        if (roleId == null || "".equals(roleId)) {
            return "redirect:dolist.do";
        }
        if (adminUserId == null || "".equals(adminUserId)) {
            throw new Exception("当前用户ID为空");
        } else{
                adminUserService.updateAdminUserRolesubmit(adminUserId, roleId);
                //返回用户列表
                return "redirect:dolist.do";
            }
        }
}
