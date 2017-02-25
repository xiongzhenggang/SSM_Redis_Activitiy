/**
 * 
 */
package com.lin.workLeave;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author hasee
 * @TIME 2016年12月20日
 * 注意类的隐藏和实例创建
 */
public class UpdateAgree implements JavaDelegate  {

	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)\
	 * 该类中一定不要使用成员变量
	 */
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		//执行数据库中的操作，将请假单状态修改为审批中
		//String var = (String) execution.getVariable("input"); 
		//var = var.toUpperCase();  
		//execution.setVariable("input", var); 
		System.out.println("====================这里是中间流程任务Service TaskUPdateOne！！！============================");
	}

}
