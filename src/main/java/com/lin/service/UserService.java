package com.lin.service;  
  
import com.xzg.domain.User;
  
/** 
 * 功能概要：UserService接口类 
 *  
 */  
public interface UserService {  
    User selectUserById(Integer userId);  
    int updateUser(User user);
}  