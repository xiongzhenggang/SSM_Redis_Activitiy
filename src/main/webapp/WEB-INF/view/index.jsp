<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html> 
<head>
 <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
    <!--文件兼容性模式-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--标记用于指定用户是否可以缩放Web页面，如果可以，那么缩放到的最大和最小缩放比例是什么-->
    
     <title>使用ssm框架开启工作流</title>
      <!-- <script type="text/javascript"  src="/js/jQuery/jquery-2.1.3.min.js"></script> -->
     <%--  <script type="text/javascript"  src='<c:url value="/js/jQuery/jquery-2.1.3.min.js"></c:url>'></script> --%>
      <script type="text/javascript"  src="js/jQuery/jquery-2.1.3.min.js"></script>  
      <!-- 利用SpringMVC资源映射来映射在目录下的静态资源  <mvc:resources mapping="/js/**" location="/js/" /> -->
           
     <style type="text/css">
     
     a{
     font-size: 20px;
     }
table{ 
width:600px; 
height: 200px; 
} 
table, table td, table th{ 
border:1px solid black; 
border-collapse: collapse; 
} 
table td{ 
width:50%; 
height: 30px; 
} 
thead th{ 
background-color:#87CEFA; 
} 
tbody th{ 
background-color:#FFFACD; 
} 
     </style>
    <!--  <script type="text/javascript">
   //在页面加载时候，就使td节点具有click点击能力
     $(document).ready(function(){
         var tdNods = $("td");
         tdNods.click(tdClick);
     });
     //td的点击事件
     function tdClick(){
         //将td的文本内容保存
         var td = $(this);
         var tdText = td.text();
         //将td的内容清空
         td.empty();
         //新建一个输入框
         var input = $("<input>");
         //将保存的文本内容赋值给输入框
         input.attr("value",tdText);
         //将输入框添加到td中
         td.append(input);
         //给输入框注册事件，当失去焦点时就可以将文本保存起来
         input.blur(function(){
             //将输入框的文本保存
             var input = $(this);
             var inputText = input.val();
             //将td的内容，即输入框去掉,然后给td赋值
             var td = input.parent("td");
             td.html(inputText);
             //让td重新拥有点击事件
             td.click(tdClick);
         });
         input.keyup(function(event){
             //1.获取当前用户按下的键值
                   //解决不同浏览器获得事件对象的差异,
                  // IE用自动提供window.event，而其他浏览器必须显示的提供，即在方法参数中加上event
                var myEvent = event || window.event;
                var keyCode = myEvent.keyCode;
                //2.判断是否是ESC键按下
                if(keyCode == 27){
                    //将input输入框的值还原成修改之前的值
                    input.val(tdText);
                }
         });
         //将输入框中的文本高亮选中
         //将jquery对象转化为DOM对象
         var inputDom = input.get(0);
         inputDom.select();
         //将td的点击事件移除
         td.unbind("click");
     };
     </script> -->
     <!-- <script type="text/javascript">
     $(function(){
    	    $('#reg01').click(function(){	
    	    		 //Javascript 取得table 中TD的值 
    	    		$.ajax({
    	           	    type:"POST",       
    	           	    url:"http://localhost:8080/ssm_project/updateUser",  /* 这里就是action名+要执行的action中的函数 */
    	           	 	contentType: "application/json; charset=utf-8",
    	           	    data:JSON.stringify(GetJsonData()),  //url后面要传送的参数    
    	           	 	async: true,
    	          	    dataType : "json",
    	           	    success:function(data){ 
    	           	    	//请求成功会由这个方法处理，其中请求成功返回值由msg接收
    	           	         //成功跳转到订单处理页面，失败则调回原页面
    	          	    	 if (message > 0) {
    	                         alert("请求已提交！");
    	                     }
    	           	     } ,
    	           	  error: function (message) {
    	           		  var val=GetJsonData();
    	           		  alert(val.userEmail);
    	              }
    	           	  });
    			});
 	});
     function GetJsonData(){
    	 var VAL=document.getElementById("table1");
    	 var json = {
    		        "userId": VAL.rows[1].cells[0].innerHTML ,
    		        "userName":VAL.rows[1].cells[1] .innerHTML ,
    		        "userPassword": VAL.rows[1].cells[2].innerHTML ,
    		        "userEmail":VAL.rows[1].cells[3].innerHTML 
    		    };
    		    return json;
     }
     </script> -->
     <script type="text/javascript">
     $(function(){
         $("#btn01").click(function(){
        /*      $.post("test.do",{name:$("#userName").val()},
            		 function(data){
                 alert(data);
             }); */
             $.ajax({
	           	    type:"POST",       
	           	    url:"test.do",  /* 这里就是action名+要执行的action中的函数 */
	           	 	contentType: "application/json; charset=utf-8",
	           	    data:JSON.stringify({name:$("#userName").val()}),  //url后面要传送的参数    
	           	 	async: true,
	          	    dataType : "json",
	           	    success:function(data){ 
	           	    	//请求成功会由这个方法处理，其中请求成功返回值由msg接收
	           	         //成功跳转到订单处理页面，失败则调回原页面
	                         alert(data.name);
	                     }
         });
     });
     });
     </script>
     
</head>
<body>  
<h2>Hello World!</h2> 
<div>
<form action="${pageContext.request.contextPath}/updateUser.do" class="workflow"  method="post"> <%-- action="${pageContext.request.contextPath}/updateUser" --%>
  <table class="tab" id="table1">
                        <thead>
                        <tr>
                            <th>id号</th>
                            <th>姓名</th>
                            <th>密码</th>
                            <th>邮箱</th>
                        </tr>
                        </thead>
                        <tr>
                        <td><input type="text" name="userId" readonly="true"  value="${user.userId}"/> </td>
                        <td> <input type="text" name="userName" id="userName" value=" ${user.userName}"/></td>
                        <td><input type="text" name="password" value=" ${user.password}"/> </td>
                        <td> <input type="text" name="email" value=" ${user.email}"/></td>
                        </tr >
                        </table>
                         <div class="submit">
                                <input type="submit" id="reg01"  class="button_submit" value="确定修改"/>
                                   <input type="button" id="btn01"  class="btn01" value="测试ajax"/>
                           </div>
                           <a  href="leaveoff/leavestart.jsp">请假流程开始</a>
</form>
</div>
</body>  
</html> 