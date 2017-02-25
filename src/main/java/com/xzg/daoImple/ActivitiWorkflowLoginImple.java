/**
 * 
 */
package com.xzg.daoImple;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xzg.dao.ActivitiWorkflowLogin;
import com.xzg.domain.Group;
import com.xzg.domain.Node;
import com.xzg.domain.User;

/**
 * @author hasee
 * @TIME 2017年2月13日
 * 注意类的隐藏和实例创建
 */
@Service(value ="ActivitiWorkflowLoginImple")
public class ActivitiWorkflowLoginImple implements ActivitiWorkflowLogin {
	@Resource
	private ActivitiWorkflowLogin activitiWorkflowLogin;
	/* (non-Javadoc)
	 * @see com.lin.dao.ActivitiWorkflowLogin#login(java.lang.String, java.lang.String)
	 */
	@Transactional
	public int login(String userId, String password) throws Exception {
		// TODO Auto-generated method stub
		int result = activitiWorkflowLogin.login(userId, password);
		return result;
	}

	/* (non-Javadoc)
	 * @see com.lin.dao.ActivitiWorkflowLogin#getUserInfo(java.lang.String)
	 */
	@Transactional
	public User getUserInfo(String userid) {
		// TODO Auto-generated method stub
		User user = activitiWorkflowLogin.getUserInfo(userid);
		return user;
	}

	/* (non-Javadoc)
	 * @see com.lin.dao.ActivitiWorkflowLogin#getUserOfGroup(java.lang.String)
	 */
	@Transactional
	public List<Group> getUserOfGroup(String userid) {
		// TODO Auto-generated method stub
		List<Group> lg = activitiWorkflowLogin.getUserOfGroup(userid);
		return lg;
	}

	/* (non-Javadoc)
	 * @see com.lin.dao.ActivitiWorkflowLogin#getGroupInfo(java.lang.String)
	 */
	@Transactional
	public Group getGroupInfo(String groupId) {
		// TODO Auto-generated method stub
		Group group = activitiWorkflowLogin.getGroupInfo(groupId);
		return group;
	}

	/* (non-Javadoc)
	 * @see com.lin.dao.ActivitiWorkflowLogin#memberOfGroup(java.lang.String)
	 */
	public List<User> memberOfGroup(String groupId) {
		// TODO Auto-generated method stub
		List<User> lu =activitiWorkflowLogin.memberOfGroup(groupId);
		return lu;
	}

	public List<User> getListUser() {
		List<User> lu =activitiWorkflowLogin.getListUser();
		return lu;
	};
	
	public List<Group> getListGroup() {
		List<Group> gu =activitiWorkflowLogin.getListGroup();
		return gu;
	};
	@Transactional
	public void deleteUserById(){
		activitiWorkflowLogin.deleteUserById();
	}
	/*根据角色id删除*/
	@Transactional
	public void deleteGroupById(){
		activitiWorkflowLogin.deleteGroupById();
	}
	
	@Transactional
	public void updateUserByid(User user){
		activitiWorkflowLogin.updateUserByid(user);
	}

	@Transactional
	public void updateGroupByid(Group group){
		activitiWorkflowLogin.updateGroupByid(group);
	}
	
	@Transactional
	public List<Node> treeList(User user){
		List<Node> tree =	activitiWorkflowLogin.treeList(user);
		return tree;
	}
}
