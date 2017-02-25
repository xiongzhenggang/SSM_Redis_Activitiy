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
</head>
<body>
<f:view>
	<div>
		<FORM action="">
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
                        <td><LABEL>员工姓名：</LABEL><input type="text" name="username"   /> </td>
                        <td><LABEL>请假天数：</LABEL> <input type="text" name="leaveday" /></td>
                        <td><LABEL>请假原因：</LABEL><input type="text" name="reason" /> </td>
                        <td><LABEL>离去日期：</LABEL> <input type="text" name="leavedate"/></td>
                        </tr >
                        </table>
                         <div class="submit">
                                <input type="submit" id="reg01"  class="button_submit" value="确定修改"/>
                                   <input type="button" id="btn01"  class="btn01" value="测试ajax"/>
                           </div>
		</FORM>
	</div>
</f:view>
</body>
</html>
</jsp:root>