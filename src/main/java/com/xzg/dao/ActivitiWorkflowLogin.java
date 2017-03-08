/**
 * 
 */
package com.xzg.dao;

import java.util.List;

import net.sf.ehcache.store.AuthoritativeTier;

import org.apache.ibatis.annotations.Param;

import com.xzg.domain.Authority;
import com.xzg.domain.Group;
import com.xzg.domain.Node;
import com.xzg.domain.User;
/**
 * @author xzg
 * @TIME 2017年2月12日
 * 注意类的隐藏和实例创建
 */
public interface ActivitiWorkflowLogin {
	  /**
     * 验证登录,mybits传递多个参数时，有三种方式1、如下。2，where user_name = #{0} and user_area=#{1}#{0}代表接收的是dao层中的第一个参数，
     * #{1}代表dao层中第二参数，更多参数一致往后加即可。3，采用Map传多参数.Public User selectUser(Map paramMap);
     * where user_name = #{userName，jdbcType=VARCHAR} and user_area=#{userArea,jdbcType=VARCHAR}
     */
	public int login(@Param("userId")String userId, @Param("password")String password) throws Exception;
	/**
	 * 获取用户详细信息验证
	 */
	public User getUserInfo(String userId);
	/**
	 * 根据用户id查询用户所在的组
	 */
	public List<Group> getUserOfGroup(String userId);

	/**
	 * 根据groupId查询组详细信息
	 */
	public Group getGroupInfo(String roleId);
	/**
	 * 列出组内的所有用户
	 */
	public List<User> memberOfGroup(String roleId) ;

/**
 * 列出所有用户
 */
public List<User> getListUser() ;
/**
 * 列出所有角色
 */
public List<Group> getListGroup() ;
/*根据用户id删除*/
public void deleteUserById();
/*根据角色id删除*/
public void deleteGroupById();
/*修改用户*/
public void updateUserByid(User user);
/*修改组*/
public void updateGroupByid(Group group);

//z_tree树形结构展示角色和权限
public List<Node> treeList(User user);
//所有权限展示
public List<Authority> authorityList();
//修改功能
public void updateAuthorityById(Authority authority);
//根据actiongId查询
public Authority selectAuthorityById(String authorityId);
}


