/**
 * 
 */
package com.xzg.workFlow.util;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import com.xzg.domain.Group;
import com.xzg.domain.User;

/**
 * @author hasee
 * @TIME 2016年12月27日
 * 注意类的隐藏和实例创建
 */
public class ActivitiUtils {  
    
    public static UserEntity  toActivitiUser(User bUser){ 
        UserEntity userEntity = new UserEntity();  
        userEntity.setId(bUser.getUserId().toString());  
        userEntity.setFirstName(bUser.getUserName());  
        userEntity.setLastName(bUser.getUserName());  
        userEntity.setPassword(bUser.getPassword());  
        userEntity.setEmail(bUser.getEmail());  
        userEntity.setRevision(1);  
        return userEntity;  
    }  
      
    public static GroupEntity  toActivitiGroup(Group bGroup){  
        GroupEntity groupEntity = new GroupEntity();  
        groupEntity.setRevision(1);  
        groupEntity.setType("assignment"); 
        groupEntity.setId(bGroup.getRoleId());  
        groupEntity.setName(bGroup.getRoleName() );  
        return groupEntity;  
    }  
      
    public static List<org.activiti.engine.identity.Group> toActivitiGroups(List<Group> bGroups){  
          
        List<org.activiti.engine.identity.Group> groupEntitys = new ArrayList<org.activiti.engine.identity.Group>();  
          
        for (Group bGroup : bGroups) {  
            GroupEntity groupEntity = toActivitiGroup(bGroup);  
            groupEntitys.add(groupEntity);  
        }  
        return groupEntitys;  
    }  
}  
