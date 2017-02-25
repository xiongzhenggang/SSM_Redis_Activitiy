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
public interface UserMapper {
	 // (Author) selectOne("selectAuthor",5);  
	public	 User getUserById(@Param("userId")long userId);   // (List<Author>) selectList(“selectAuthors”) 
	//通过用户查询角色
	public 	List<Group> getGroupList(@Param("userId")long userId);
	//通过id删除用户
	public 	int deleteUserById(int userId); 
//保存用戶信息
	public 	int saveUser(User user);
	
}
