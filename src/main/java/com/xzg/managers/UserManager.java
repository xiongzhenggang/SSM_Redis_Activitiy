/**
 * 
 */
package com.xzg.managers;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xzg.dao.UserMapper;
import com.xzg.domain.Group;
import com.xzg.domain.User;

/**
 * @author hasee
 * @TIME 2016年12月27日
 * 注意类的隐藏和实例创建
 */
@Service(value ="userManager")
public class UserManager implements UserMapper{
	/* (non-Javadoc)
	 * @see com.lin.dao.UserMapper#getUserById(int)
	 */
	@Resource
	private UserMapper	 mapper;
	public User getUserById(long id) {
		// TODO Auto-generated method stub
			 //以下为执行的方法
		User user = mapper.getUserById(id);
		return  user;
	}
	/* (non-Javadoc)
	 * @see com.lin.dao.UserMapper#getGroupList(com.lin.domain.User)
	 */
	public List<Group> getGroupList(long userId) {
		List<Group> listGroup = mapper.getGroupList(userId);
		return listGroup;
	}

	/* (non-Javadoc)
	 * @see com.lin.dao.UserMapper#deleteUserById(int)
	 */
	public int deleteUserById(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.lin.dao.UserMapper#getGroupByGroupId(java.lang.String)
	 */
	public int saveUser(User user) {
		int result = 0;
		result = mapper.saveUser(user);	
		return result;
	}
}
