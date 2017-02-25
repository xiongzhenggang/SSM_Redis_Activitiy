/**
 * 
 */
package com.xzg.controller;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xzg.dao.UserDao;
import com.xzg.domain.User;
/**
 * @author hasee
 *@TIME2016年12月1日
 */
/* 三、SpringMVC常用注解
@Controller
　负责注册一个bean 到spring 上下文中
@RequestMapping
　注解为控制器指定可以处理哪些 URL 请求
@RequestBody
该注解用于读取Request请求的body部分数据，使用系统默认配置的HttpMessageConverter进行解析，然后把相应的数据绑定到要返回的对象上 ,
再把HttpMessageConverter返回的对象数据绑定到 controller中方法的参数上
@ResponseBody
该注解用于将Controller的方法返回的对象，通过适当的HttpMessageConverter转换为指定格式后，写入到Response对象的body数据区
@ModelAttribute 　　　
在方法定义上使用 @ModelAttribute 注解：Spring MVC 在调用目标处理方法前，会先逐个调用在方法级上标注了@ModelAttribute 的方法
在方法的入参前使用 @ModelAttribute 注解：可以从隐含对象中获取隐含的模型数据中获取对象，再将请求参数 –绑定到对象中，再传入入参将方法入参对象添加到模型中 
@RequestParam　
在处理方法入参处使用 @RequestParam 可以把请求参 数传递给请求方法
@PathVariable
绑定 URL 占位符到入参
@ExceptionHandler
注解到方法上，出现异常时会执行该方法
@ControllerAdvice
使一个Contoller成为全局的异常处理类，
类中用@ExceptionHandler方法注解的方法可以处理所有Controller发生的异常*/
@Controller  
public class UserController {  
    @Resource  
    private UserDao userDao; 
    @RequestMapping(value="/showUser.do",method = RequestMethod.GET) 
    @ResponseBody  
    public ModelAndView getIndex(){      
        ModelAndView mav = new ModelAndView("view/index");   
      User  user = userDao.selectUserById(10000);  
        mav.addObject("user", user);   
        return mav;    
    }
    @RequestMapping( "/updateUser.do")
    @ResponseBody  //在springMVC中提供了JSON响应的支持
    public ModelAndView showUdateUser(User user){
    	 System.out.println(user);
       @SuppressWarnings("unused")
	int a = userDao.updateUser(user); 
     return   getIndex();
    } 
    @RequestMapping("/test.do")
    @ResponseBody  //在springMVC中提供了JSON响应的支持
    public Map<String,Object>  test2(@RequestBody String name){
   /* 	model是将数据添加到request域。Springmvc中有一个叫做redirectattributes的类可以添加flash attribute，
    	这个就是将数据存入session。主要是解决redirect问题的*/
    	Map<String,Object> mapout=new HashMap<String, Object>();
    	mapout.put("id", 16);
    	mapout.put("name",name.trim());
    	return mapout;
    }
    @RequestMapping(value="/leavestart.do",method = RequestMethod.GET)
    public ModelAndView getLeaveOff(){      
        ModelAndView mav = new ModelAndView("leaveoff/leavestart");   
        return mav;    
    }
}  


