/**
 * 
 */
package com.xzg.dao;

import java.util.List;

import com.xzg.domain.Node;
import com.xzg.domain.User;

/**
 * @author hasee
 *
 */
public interface UserDao {  
    /** 
     *  
     * @author linbingwen 
     * @since 2015年9月28日 
     * @param userId 
     * @return 
     */  
    public User selectUserById(Integer userId); 
    public int updateUser(User user);
    public User selectLeaveUser(User user);
    //z_tree树形结构展示
    public List<Node> treeList();
    
    
    
    
}