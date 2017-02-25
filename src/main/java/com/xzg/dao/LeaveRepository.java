package com.xzg.dao;

import org.activiti.web.simple.webapp.model.Leave;

/**
 * 使用Spring Data JPA操作请假流程
 * @author scott
 */
public interface LeaveRepository {
	public void save(Leave leave) ;
	public Leave findOne(long id);
}
