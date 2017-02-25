/**
 * 
 */
package com.lin.service;  
  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
  


import com.xzg.dao.UserDao;
import com.xzg.domain.User;
  
/** 
 * 功能概要：UserService实现类 
 */  
@Service  
public class UserServiceImpl implements UserService{  
    @Autowired  
    private UserDao userDao;  
  
    public User selectUserById(Integer userId) {  
        return userDao.selectUserById(userId);  
    }

	/* (non-Javadoc)
	 * @see com.lin.service.UserService#updateUser(com.lin.domain.User)
	 */
	public int updateUser(User user) {
		// TODO Auto-generated method stub
		
		return userDao.updateUser(user);
	}  
  
} 
