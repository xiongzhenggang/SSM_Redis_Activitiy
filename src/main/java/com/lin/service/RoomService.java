/**
 * 
 */
package com.lin.service;

import com.xzg.domain.User;

/**
 * @author hasee
 * @TIME 2017年3月20日
 * 注意类的隐藏和实例创建
 */
public interface RoomService {

	 
    int insert(User user)throws Exception;
    
    User selectByPrimaryKey(Integer userId)throws Exception;
    
}
