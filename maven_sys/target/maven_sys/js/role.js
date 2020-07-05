	/**跳转分配权限*/
	function goRolePermission(roleId){
		window.location="goRolePermission?roleId="+roleId+"";
	}			
	
	/**打开修改角色窗口*/
	function showUpdateRole(roleName,description,id){
		jQuery("#updateRole").css({"display":"block"});
		jQuery("#roleName").val(roleName);
		jQuery("#description").val(description);
		jQuery("#roleID").val(id);
	}
	function closer(){
		jQuery("#updateRole").css({"display":"none"});
	}

	/**删除管理员用户*/
	function roleDel(id){
		jQuery(".hintl").css({"display":"block"});
		jQuery(".box").css({"display":"block"});
	    
		jQuery(".hintl-in3").click(function(event) {
			jQuery(".hintl").css({"display":"none"});
			jQuery(".box").css({"display":"none"});
			window.location="delete.do?id="+id+"";
		});
		
		jQuery(".hintl-in4").click(function() {
			jQuery(".hintl").css({"display":"none"});
			jQuery(".box").css({"display":"none"});
		});
		
	}	

	/**提交修改角色*/
	function updateRole(){
		var roleName = jQuery('#roleName').val();
		var description = jQuery('#description').val();
		var roleID = jQuery('#roleID').val();
		if(roleName == '') {
			layer.msg('角色名称不能为空！');
			return false;
		}
		     
		var data = {roleName:roleName,description:description,roleID:roleID};
		var load = layer.load();
		jQuery.ajax({
			url:"updateRole.do",
			data:data,
			type:"post",
			dataType:"json",
			success:function(result){
				layer.close(load);
				if(result && result.status != 200){
					layer.msg(result.message,function(){});
			    	return;
			    }else{
			    	layer.msg(result.message);
			    	jQuery("#updateRole").css({"display":"none"});
			    	setTimeout(function(){
		    			window.location.href= result.back_url || "ShowRoleList.do";
	    			},1000)
			    }
			},
			error:function(e){
				console.log(e,e.message);
		        layer.msg('修改失败！',new Function());
			}
		});
	}	

	
	/**分页*/
	function nextPage(currPage,pageSize){	
		window.location="ShowRoleList.do?currPage="+currPage+"&pageSize="+pageSize+"";
	}

	/**每页显示*/
	function showPageSize(){
		var pageSize = document.getElementById("pageSize").value;
		if(pageSize.length>0){
			nextPage(1,pageSize);
		}		
	}
         		
	/**跳转到具体页*/
	function jumpPage(totalPage,pageSize){
		var page = document.getElementById("jumpPage").value;
		var reg = /^[0-9]*$/;
		if (0!=page&&reg.test(page)&&totalPage>=page) {
			nextPage(page,pageSize);
		}else{				
			layer.msg("请输入正确页数!");
		}
	}



	/**添加角色提交*/
	function roleAdd(){
		var roleName = jQuery("#roleName").val();
		var description = jQuery("#description").val();
		if(roleName==null||roleName==""){
			layer.msg('角色名称不能为空！');
			return false;
		}
		if(description==null||description==""){
			layer.msg('描述不能为空！');
			return false;
		}
		var checkPID = [];
		var pids =document.getElementsByName("permissionId");
		for(var i =0;i<pids.length;i++){
			if(pids[i].checked){
				checkPID.push(pids[i].value);
			}
		}
		var cp = checkPID.toString();

		RolesAddSave(roleName,description,cp);
		console.log(roleName,description,cp)
	}

	/**添加角色提交保存*/
	function RolesAddSave(roleName,description,cp){
		var data = {roleName:roleName,description:description,checkPID:cp};
		var load = layer.load();
		jQuery.ajax({
			url:"AddRole.do",
			data:data,
			type:"post",
			dataType:"json",
			beforeSend:function(){
				layer.msg('正在处理...');
			},
			success:function(result){
				layer.close(load);
				if(result && result.status != 200){
					layer.msg(result.message,function(){});
					return;
				}else{
					layer.msg(result.message);
					setTimeout(function(){
						window.location.href= result.back_url || "ShowRoleList.do";
					},1000)
				}
			},
			error:function(e){
				layer.msg('添加提交失败！');
			}
		});
	}

	