/**
 * 
 */
package com.xzg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hasee
 * @TIME 2017年1月5日
 * 注意类的隐藏和实例创建
 */
@Controller
@RequestMapping("/emp")
public class TestAop {
@RequestMapping("/findemp.do")
public String find(){
	System.out.println("查询员工信息发送到页面");
	return "emp/emp_list.jsp";
}
}
