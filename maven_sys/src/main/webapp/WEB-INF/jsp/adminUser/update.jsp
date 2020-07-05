<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/header.jsp" %>

<title>分配角色</title>
</head>
<body>
<div class="pd-20">
    <form action="updateAdminUserRole.do">
        <input type="hidden" name="adminUserId" value="${adminUserId}" />
        <table class="table table-border table-bordered table-bg table-hover" >
		<!-- 此处要修改的其它用户信息-->
            <thead>
                <tr>
                    <th>选中</th>
                    <th>角色名称</th>
                    <th>角色描述</th>
                </tr>
            </thead>
            <!-- 此处写角色选中。-->
            <tbody>
                        <c:forEach var="list" items="${allrolelist}">
                        <tr>
                            <td>
                                <input type="checkbox" name="roleId" value="${list.id}"
                            <c:forEach var="adminrole" items="${AdminUserRolesList}">
                                        <c:if test="${list.id==adminrole.roleId}">checked</c:if>
                            </c:forEach>
                                />
                            </td>
                            <td>${list.name}</td>
                            <td>${list.description}</td>
                        </tr>

                 </c:forEach>

            </tbody>
        </table>
        <br>
        <input class="btn btn-primary radius" type="submit" value="分配角色" />
        <input class="btn btn-default radius" type="button" value="关闭" onclick="location.href='dolist.do'" style="margin-left: 30px;" />
    </form>
</div>
</body>
</html>