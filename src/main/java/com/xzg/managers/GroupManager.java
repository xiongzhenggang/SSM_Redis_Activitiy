/**
 * 
 */
package com.xzg.managers;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xzg.dao.GroupMapper;
import com.xzg.domain.Group;
import com.xzg.domain.User;

/**
 * @author hasee
 * @TIME 2016年12月27日
 * 注意类的隐藏和实例创建
 */
@Service(value ="groupManager")
public class GroupManager implements GroupMapper{
	@Resource
	private GroupMapper	 mapper;
	public Group getGroupById(String roleId) {
		// TODO Auto-generated method stub
		Group	 group = mapper.getGroupById(roleId);
		return group;
	}
	/* (non-Javadoc)
	 * @see com.lin.dao.UserMapper#saveGroup(com.lin.domain.Group)
	 */
	public int saveGroup(Group group) {
		// TODO Auto-generated method stub
		int result = 0;
			result = mapper.saveGroup(group);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.lin.dao.UserMapper#getUserById(long)
	 */
	public User getUserById(long userId) {
		if(userId ==  0L){
			return null;	
		}
		User	user = mapper.getUserById(userId);
		return user;
	}

	public List<Group> getGroupList(long userId) {
		// TODO Auto-generated method stub
			 List<Group> 	listGroup = mapper.getGroupList(userId);
		return listGroup;
	}
	/* (non-Javadoc)
	 * @see com.lin.dao.GroupMapper#deleteGroupByid(java.lang.String)
	 */
	public void deleteGroupByid(String roleId) {
		// TODO Auto-generated method stub
		
	}
}
