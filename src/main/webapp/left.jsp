<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0Strict//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Spring Activiti App</title>
<link href="${ctx }/style/style.css" type="text/css" rel="stylesheet">
<script  src="js/jQuery/jquery-2.1.3.min.js"  type="text/javascript"></script> 
<script src="js/ztree/jquery.ztree.core.min.js" type="text/javascript"></script>
<script src="js/ztree/jquery.ztree.excheck.min.js" type="text/javascript"></script>
<link href="js/ztree/zTreeStyle.css"  type="text/css" rel="stylesheet">
<script src="js/ztree_init.js" type="text/javascript"></script>
<script type="text/javascript">
/**
 * 页面初始化
 */
$(document).ready(function(){
	 var url="${ctx}/treeList.do";
  onLoadZTree(url);
});
</script>
</head>
<body>
<!-- 点击后, 会显示的内容, 将显示到main<iframe>这个页内框架中!  -->
<%-- <a href="${ctx }/userwork.do" target="main">用户管理</a><br/><br/>
<a href="${ctx }/groupwork.do" target="main">组管理</a><br/><br/>
<a href="${ctx }/workflow/toupload.do" target="main">上传流程定义</a><br/><br/>
<a href="${ctx }/workflow/processlist.do" target="main">查看流程定义</a><br/><br/>
<a href="${ctx }/leave/form.do" target="main">填写请假单</a><br/><br/>
<a href="${ctx }/leave/task/list.do" target="main">待办任务列表</a><br/><br/>
<a href="${ctx }/leave/process/running/leave/list.do" target="main">运行中的流程实例</a><br/><br/>
<a href="${ctx }/leave/process/finished/leave/list.do" target="main">已经结束的流程实例</a><br/> --%>
<div class="zTreeDemoBackgroundleft">
    <ul id="user_tree" class="ztree"></ul>
  </div>  
</body>
</html>