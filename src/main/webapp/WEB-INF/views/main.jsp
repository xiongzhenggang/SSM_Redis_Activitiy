<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Spring Activiti App</title>
</head>
<frameset rows="100,*" frameborder="1" border="1"><!-- cols（列）定义框架集中行的数目和尺寸,rows(水平)定义框架集中列的数目和尺寸 -->
	<frame src="${ctx }/top.jsp" name="top">
	<frameset cols="280,*" frameborder="1" border="1">
		<frame src="${ctx }/left.jsp" name="left">
		<frame src="${ctx }/work.jsp" name="main" id="main">
	</frameset>
</frameset>
</html>
