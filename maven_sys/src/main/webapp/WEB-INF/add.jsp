<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/header.jsp" %>

<title>添加权限</title>
</head>
<body>
<div class="pd-20">
    <form action="" class="form form-horizontal">
    
        <div class="row cl">
            <label class="form-label col-2">权限名称</label>
            <div class="formControls col-5">
                <input type="text" class="input-text" name="prName" id="ADDpermission"/>
            </div>
            <div class="col-5"></div>
        </div>

        <div class="row cl">
            <label class="form-label col-2">描&nbsp;&nbsp;述</label>
            <div class="formControls col-5">
                <textarea name="description" class="textarea" id="ADDdescription"></textarea>
            </div>
            <div class="col-5"></div>
        </div>

        <div class="row cl">
            <div class="col-9 col-offset-2">           	
                <input class="btn btn-primary radius" type="button" value="添加"  onclick="addPermission()"/>
                <input class="btn btn-default radius" type="button" value="关闭" onclick="location.href='getAllPermissions'" style="margin-left: 30px;" />
            </div>
        </div>
    </form>
</div>
</body>
</script>
<script type="text/javascript" src="<%=ctxPath %>/js/permission.js"></script>
</html>