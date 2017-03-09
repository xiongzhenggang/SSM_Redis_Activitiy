<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }"></c:set>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="${ctx }/js/jquery-1.7.1.js" type="text/javascript"></script>
      <script type="text/javascript">
      $(function() {
    		$("#submit").click(function(e){
    			debugger;
    			var cn_xzg= get_cookie("cn.xzg");
    			if(cn_xzg== null||typeof(cn_xzg) == "undefined"||cn_xzg==""){
    				firm();
    			}
    			});
    	});
   function firm() {
	   var url ='${ctx}/saveCookie.do';
	   var username= $("input[id='username']").val();
	   var password= $("input[id='password']").val();
          //利用对话框返回的值 （true 或者 false）  
          if (confirm("确定保存密码？")) {  
        	 //使用ajax异步处理
        	 $.ajax({
    		type:"POST", 
    		url:url,  /* 这里就是action名+要执行的action中的函数 */
    		contentType: "application/json; charset=utf-8",
    		data:JSON.stringify({username:username,password:password}),  //url后面要传送的参数    
    		async: true,
    		dataType : "json",
    		success:function(data){
    			//
    			}
    		});
          }
          else {  
          }  
      } 
   function get_cookie(Name) {
	   var search = Name + "="//查询检索的值
	   var returnvalue = "";//返回值
	   if (document.cookie.length > 0) {
	     sd = document.cookie.indexOf(search);
	     if (sd!= -1) {
	        sd += search.length;
	        end = document.cookie.indexOf(";", sd);
	        if (end == -1)
	         end = document.cookie.length;
	         //unescape() 函数可对通过 escape() 编码的字符串进行解码。
	        returnvalue=unescape(document.cookie.substring(sd, end))
	      }
	   } 
	   return returnvalue;
	}
      </script>
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
        		用户名:<input name="username" id="username" type="text"/><br/>
        		密&nbsp;&nbsp;码:<input name="password"  id="password"  type="password"/><br/>
        		<input name="submit" id="submit" type="submit" value="登录"/>
        	</form>
        </fieldset>
    </body>
</html>
