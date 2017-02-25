<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html> 
<head>
 <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <!--文件兼容性模式-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--标记用于指定用户是否可以缩放Web页面，如果可以，那么缩放到的最大和最小缩放比例是什么-->
      <script  src="js/jQuery/jquery-2.1.3.min.js"  type="text/javascript"></script> 
      <script src="js/ztree/jquery.ztree.core.min.js" type="text/javascript"></script>
		<script src="js/ztree/jquery.ztree.excheck.min.js" type="text/javascript"></script>
		<link href="js/ztree/zTreeStyle.css"  type="text/css" rel="stylesheet">
      <!-- 利用SpringMVC资源映射来映射在目录下的静态资源  <mvc:resources mapping="/js/**" location="/js/" /> -->
        <%--    <script src="${ctx }/js/jQuery/jquery-2.1.3.min.js" type="text/javascript"></script>
		<script src="${ctx }/js/ztree/jquery.ztree.core.min.js" type="text/javascript"></script>
		<script src="${ctx }/js/ztree/jquery.ztree.excheck.min.js" type="text/javascript"></script> --%>
		<script src="js/ztree_init.js" type="text/javascript"></script>
</head>
<body>
<!-- <script type="text/javascript">
/**
 * 页面初始化
 */
$(document).ready(function(){
	 var url="${ctx}/treeList.do";
  onLoadZTree(url);
}); -->
</script>
     <style type="text/css">
     a{
     font-size: 20px;
     }
     </style>
     <script type="text/javascript">
     $(function(){
         $("#reg01").click(function(){
            $.get("showUser.do",function(data){
            	  window.location.href = "showUser.do";  
            });
     });
     });
     </script>
</head>
<body>
<!-- <div class="zTreeDemoBackgroundleft">
    <ul id="user_tree" class="ztree" overflow-y: scroll;height: 100px;"></ul>
  </div>   -->
<div>
                         <!-- <div class="submit">
                                <input type="button" id="reg01"  class="button_submit" value="展示员工信息"/>
                           </div> -->
                           <a  id= ""  href="leavestart.do">测试</a> <!-- 访问web-inf下的资源无法直接访问，通过servlet转发 -->
                           <a  id= "login"  href="login.do">工作流登录入口</a>
</div>
<script>

</script>
</body>  
</html> 