/**
 * 
 */
package com.lin.workLeave;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @author hasee
 * @TIME 2016年12月26日
 * 注意类的隐藏和实例创建
 */
public class TestEndListener  implements ExecutionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.ExecutionListener#notify(org.activiti.engine.delegate.DelegateExecution)
	 */
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		execution.getVariable("");
		System.out.println("测试监听类是否执行流程最后在这里结束！！");
	}

}
