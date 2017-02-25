<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户管理</title>
</head>
<link href="${ctx }/style/style.css" type="text/css" rel="stylesheet">
<script src="${ctx }/js/jquery-1.7.1.js" type="text/javascript"></script>
<body>
<h1>用户管理</h1>
<h3>${message }</h3>
<div id="showUpdateUser">
<table id="tab" border="1" style="border-collapse: collapse;border-style:solid;" width="100%">
	<thead>
		<tr>
			<th>用户名编号</th>
			<th>用户姓名</th>
			<th>所在角色</th>
			<th>邮箱地址</th>
			<th>操作</th>
		</tr>
	</thead>
	<c:forEach var="user" items="${listuser }">
		<tr align="center">
			<td>${user.userId }</td>
			<td>${user.userName }</td>
			<td>${user.roleName }</td>
			<td>${user.email }</td>
			<td>
				<a href="#"  onclick="showUpdate(${user.userId })"  target="main" >修改</a>
				<a href="${ctx}/${user.userId }/deleteUserById.do" target="main">删除</a>
			</td>
		</tr>
	</c:forEach>
</table>
</div>
</body>
<script type="text/javascript">
$(function() {
	//根据activitiId显示相应的信息
	$("#update").click(function(){
		$("#fm").hide();
		$("#tab").show();
		
		});
});
function showUpdate(obj){
    	var id = obj ;
    	var url ='${ctx}/showUpdateUserById.do';
    	$.ajax({
    		type:"POST",       
    		url:url,  /* 这里就是action名+要执行的action中的函数 */
    		contentType: "application/json; charset=utf-8",
    		data:JSON.stringify({id:id}),  //url后面要传送的参数    
    		async: true,
    		dataType : "json",
    		success:function(data){
    			if(data != null){
    				$("#tab").hide();
    			}
    			/* $(data).each(function(i,element){
    	      		var result = "";
    	   			result += "<tr>";
    	      		 result+="<td><input type='radio' value="+element.allocatecarNumber+" name='selectedAllocateCarNumber'></td>";
    	      		result +="<td>"+element.allocatecarNumber+"</td>";
    	      		result +="<td>"+element.shipmentSitution+"</td>";
    	      		result +="<td>"+element.shipmentDay+"</td>";
    	      		result +="<td>"+element.carCode+"</td>";
    	      		result +="<td>"+element.carTypeName+"</td>";
    	      		result +="<td>"+element.deliveryName+"</td>";
    	      		result +="<td>"+element.amount+"</td>";
    	      		result+="</tr>";
    	      		$("#tr").append(result);
    	      	})  */
    		var result="<form action='${ctx }/updateUserById.do' id='fm'  method='post'>";
    		result+='用户编号：<input id="userId" name="userId"  type="text" readonly="true" value="'+nullToString(data.user.userId)+'" /><br/>';
    		result+='用户姓名：<input id="username" name ="userName"  type="text" value="'+nullToString(data.user.userName)+'"/><br/>';
    		result+='用户邮箱：<input id="email"  name="email" type="text" value="'+ nullToString(data.user.email)+'"/>' ;
    		result+='<br/><input type="submit" id="update"  value="确定修改"/></form>';
    		 $("#showUpdateUser").append(result);
    			}
    		});
}
function nullToString(value){
	//typeof 返回的是字符串，有六种可能："number"、"string"、"boolean"、"object"、"function"、"undefined"
	if(value == null||typeof(value) == "undefined"){
		value='';
	}
	return value;
}
</script>
</html>