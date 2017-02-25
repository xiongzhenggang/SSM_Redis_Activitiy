package com.lin.service;  
  
import org.apache.log4j.Logger;  
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;  
  


import com.lin.baseTest.SpringTestCase;  
import com.xzg.domain.User;
  
/** 
 * 功能概要：UserService单元测试 
 *  
 */  
public class UserServiceTest extends SpringTestCase {  
    @Autowired  
    private UserService userService;  
    Logger logger = Logger.getLogger(UserServiceTest.class);  
      
    @Test  
    public void selectUserByIdTest(){  
        User user = userService.selectUserById(10);  
        logger.debug("查找结果" + user);  
    }  
}  