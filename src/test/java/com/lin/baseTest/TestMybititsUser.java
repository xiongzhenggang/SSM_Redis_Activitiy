/**
 * 
 */
package com.lin.baseTest;

import com.xzg.domain.User;
import com.xzg.managers.UserManager;

/**
 * @author hasee
 * @TIME 2016年12月28日
 * 注意类的隐藏和实例创建
 */
public class TestMybititsUser {

	public static void main(String[] args){
		UserManager um = new UserManager();
		User u = um.getUserById(10001);
		System.out.println(u.toString());
	}
}
