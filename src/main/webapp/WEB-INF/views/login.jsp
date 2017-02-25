<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>用户登录</title>
    </head>
    <body>
    	<h1>用户登录</h1>
    	${message }
        <fieldset>
       <!--  当一组表单元素放到 <fieldset> 标签内时，浏览器会以特殊方式来显示它们，
        它们可能有特殊的边界、3D 效果，或者甚至可创建一个子表单来处理这些元素。 -->
        	<legend>用户登录</legend>
        	<form action="${ctx }/loginin.do" method="post">
        		用户名:<input name="username" type="text"/><br/>
        		密&nbsp;&nbsp;码:<input name="password" type="password"/><br/>
        		<input name="submit" type="submit" value="登录"/>
        	</form>
        </fieldset>
    </body>
</html>
