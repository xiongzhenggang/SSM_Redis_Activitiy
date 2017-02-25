<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html" version="2.0">
    <jsp:directive.page language="java"
        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <jsp:text>
        <![CDATA[ <?xml version="1.0" encoding="UTF-8" ?> ]]>
    </jsp:text>
    <jsp:text>
        <![CDATA[ <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> ]]>
    </jsp:text>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>请假开始</title>

<style type="text/css">
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
       <script type="text/javascript"  src="js/jQuery/jquery-2.1.3.min.js"></script>  
       <script type="text/javascript">
     $(function(){
         $("#reg01").click(function(){
        /*      $.post("test.do",{name:$("#userName").val()},
            		 function(data){
                 alert(data);
             }); */
             $.ajax({
	           	    type:"POST",       
	           	    url:"activiti/startWorkflow.do",  /* 这里就是action名+要执行的action中的函数 */
	           	 	contentType: "application/json; charset=utf-8",
	           	    data:$('#startForm').serialize(),  //url后面要传送的参数    
	           	 	async: false,
	          	    dataType : "json",
	          	  error: function(request) {
	                    alert("提交失败！");
	                },
	           	    success:function(data){ 
	                         alert("已进入流程");
	                     }
         });
     });
     });
     </script>
</head>
<body>
<f:view>
	<div>
		<FORM id= "startForm" action="/activiti/">
		   		<LABEL>员工编号：</LABEL> <input type="text" name="userId" style="height:25px"  /><br/>
             	 <LABEL>员工姓名：</LABEL><input type="text" name="userName"  style="height:25px" /> <br/>
                 <LABEL>请假天数：</LABEL> <input type="text" name="howlong" style="height:25px" /><br/>
                 <LABEL>角色编号：</LABEL> <input type="text" name="leaveday" style="height:25px" /><br/>
                 <LABEL>角色名：</LABEL> <input type="text" name="leaveday" style="height:25px"/><br/>
              	 <LABEL>请假原因：</LABEL><input type="text" name="reason" style="height:25px;width: 50px"/><br/>
              	 <LABEL>离去日期：</LABEL> <input type="date" name="leavedate" style="height:25px"/><br/>
              	 <LABEL>请假状态：</LABEL> <input type="text" name="leaveState" style="height:25px" readonly="readonly"/><br/>
                         <div class="submit">
                                <input type="submit" id="reg01"  class="button_submit" value="确定提交申请"/> 
                           </div>
		</FORM>
	</div>
</f:view>
</body>
</html>
</jsp:root>