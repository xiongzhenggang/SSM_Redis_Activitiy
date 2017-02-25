/**
 * 
 */
package com.xzg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzg.domain.Group;
import com.xzg.domain.User;

/**
 * @author hasee
 * @TIME 2016年12月9日
 * 注意类的隐藏和实例创建
 */
public interface GroupMapper {
	 // (Author) selectOne("selectAuthor",5);  
	public	 User getUserById(@Param("userId")long userId);   // (List<Author>) selectList(“selectAuthors”) 
	//通过用户查询角色
	public 	List<Group> getGroupList(@Param("userId")long userId);
	//操作组
	public 	Group getGroupById(String roleId);
	//删除组操作
	public 	void deleteGroupByid(String roleId);
	//保存組信息
	public 	int saveGroup(Group group);
	
}
