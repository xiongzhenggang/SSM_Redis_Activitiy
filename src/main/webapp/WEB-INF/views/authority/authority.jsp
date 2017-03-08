<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>组管理</title>
<link href="${ctx }/style/style.css" type="text/css" rel="stylesheet">
<script src="${ctx }/js/jquery-1.7.1.js" type="text/javascript"></script>
</head>
<body>
<h1>权限管理</h1>
<h3>${message }</h3>
<div id="showUpdateAuthority">
<table border="1"  id="tab" style="border-collapse: collapse;border-style:solid;" width="100%">
	<thead>
		<tr>
			<th>权限编号</th>
			<th>权限名称</th>
			<th>url</th>
		</tr>
	</thead>
	<c:forEach var="authority" items="${authoritys}">
		<tr align="center">
			<td>${authority.actionId }</td>
			<td>${authority.actionName }</td>
			<td>${authority.url }</td>
			<td>
			<!-- 在使用a标签使用onclick调用函数的时候，传递的参数需要定义类型（number不做处理，string需要‘引号’）所以这里加上单引号处理 -->
			<a href="#" onclick="showUpdate('${authority.actionId}')"   target="main" >修改</a>
				<a href="${ctx}/deleteGroupById/${authority.actionId}.do" target="main">删除</a>
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
    	var url ='${ctx}/showUpdateAuthorityById.do';
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
    		var result="<form action='${ctx }/updateAuthorityById.do'  method='post'>";
    		result+="权限编号：<input id='actionId' name='actionId'  type='text' readonly='true' value='"+nullToString(data.authority.actionId)+"' /><br/>";
    		result+="权限名称：<input id='actionName' name ='actionName'  type='text' value='"+nullToString(data.authority.actionName)+"' /><br/>";
    		result+="动作地址：<input id='url' name ='url'  type='text' value='"+nullToString(data.authority.url)+"' /><br/>";
    		result+='<br/><input type="submit" id="update"  value="确定修改"/></form>';
    		 $("#showUpdateAuthority").append(result);
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