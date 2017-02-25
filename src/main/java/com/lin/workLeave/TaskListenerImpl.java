/**
 * 
 */
package com.lin.workLeave;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @author hasee
 * @TIME 2016年12月26日
 * 注意类的隐藏和实例创建
 */
public class TaskListenerImpl implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * 执行监听器实现的步骤和任务监听差不多，但是执行监听器要实现的接口是ExecutionLintener
	 * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
	 */
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		//指定组任务  
        delegateTask.addCandidateUser("孙悟空");  
        delegateTask.addCandidateUser("猪八戒");  
	}

}
