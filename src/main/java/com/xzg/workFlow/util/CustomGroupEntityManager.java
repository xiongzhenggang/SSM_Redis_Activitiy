/**
 * 
 */
package com.xzg.workFlow.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.GroupQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.GroupEntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.xzg.dao.GroupMapper;

/**
 * @author hasee
 * @TIME 2016年12月26日
 * 注意类的隐藏和实例创建
 */
//使用自定义用户角色一般只需覆盖findGroupById和findGroupsByUser方法
@Service
public class CustomGroupEntityManager extends GroupEntityManager {
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory  
            .getLog(CustomUserEntityManager.class);
	
	 @Resource(name="groupManager")
	 	private GroupMapper	 mapper;
	 //activiti5.20中源码GroupEntityManager类中已经不存在findGroupById方法
	 public GroupEntity findGroupById(final String groupCode) {
        if (groupCode == null)
            return null;
        try {
            com.xzg.domain.Group bGroup = mapper.getGroupById(groupCode);
            GroupEntity e = new GroupEntity();
            e.setRevision(1);
            // activiti有3种预定义的组类型：security-role、assignment、user  
            // 如果使用Activiti  
            // Explorer，需要security-role才能看到manage页签，需要assignment才能claim任务  
            e.setType("assignment");
            e.setId(bGroup.getRoleId());
            e.setName(bGroup.getRoleName());
            return e;
        } catch (EmptyResultDataAccessException e) {
        	
            return null;
        }
    }

    @Override
    public List<Group> findGroupsByUser(final String userCode) {
        if (userCode == null)
            return null;
       com.xzg.domain.User user = mapper.getUserById(Long.valueOf(userCode));
        List<com.xzg.domain.Group> bGroupList = mapper.getGroupList(user.getUserId());
        List<Group> gs = new ArrayList<Group>();
        GroupEntity g;
        for (com.xzg.domain.Group bGroup : bGroupList) {
            g = new GroupEntity();
            g.setRevision(1);
            g.setType("assignment");
            g.setId(bGroup.getRoleId());
            g.setName(bGroup.getRoleName());
            gs.add(g);
        }
        return gs;
    }

	public void insertGroup(Group group) {
		throw new RuntimeException("not implement method.");//新增用户在业务逻辑中处理不使用activiti
	}

	@Override
    public List<Group> findGroupByQueryCriteria(GroupQueryImpl query, Page page) {
		return  super.findGroupByQueryCriteria(query, page);
      //  throw new RuntimeException("not implement method.");
    }

    @Override
    public long findGroupCountByQueryCriteria(GroupQueryImpl query) {
    	return super.findGroupCountByQueryCriteria(query);
    }
}
