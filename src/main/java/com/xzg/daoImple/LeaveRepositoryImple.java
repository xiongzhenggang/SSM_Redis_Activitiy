/**
 * 
 */
package com.xzg.daoImple;

import javax.annotation.Resource;

import org.activiti.web.simple.webapp.model.Leave;
import org.springframework.stereotype.Service;

import com.xzg.dao.LeaveRepository;

/**
 * @author hasee
 * @TIME 2017年1月20日
 * 注意类的隐藏和实例创建
 */
@Service("leaveRepositoryImple")
public class LeaveRepositoryImple implements LeaveRepository {

		@Resource
		private LeaveRepository leaveRepository;
	public void save(Leave leave) {
		// TODO Auto-generated method stub
		leaveRepository.save(leave);
	}
	/* (non-Javadoc)
	 * @see com.lin.dao.LeaveRepository#findOne(long)
	 */
	public Leave findOne(long id) {
		// TODO Auto-generated method stub
		return leaveRepository.findOne(id);
	}

}
