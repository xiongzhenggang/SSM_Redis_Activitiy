package org.activiti.web.simple.webapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.web.simple.webapp.model.Leave;
import org.activiti.web.simple.webapp.service.LeaveService;
import org.activiti.web.simple.webapp.service.LeaveWorkFlowService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service("leaveWorkFlowServiceImpl")
@Transactional(propagation=Propagation.REQUIRED)
public class LeaveWorkFlowServiceImpl implements LeaveWorkFlowService {
	
	
	@Resource(name="leaveServiceImpl")
	private LeaveService leaveService;
	
	@Resource(name="identityService")
	private IdentityService identityService;
	
	@Resource(name="runtimeService")
	private RuntimeService runtimeService;
	
	@Resource(name="historyService")
	private HistoryService historyService;
	
	@Resource(name="taskService")
	private TaskService taskService;
	
	@Resource(name="managementService")
	private ManagementService managementService;
	
	@Resource//(name="formService")需要在xml配置
	private FormService formService;
	
	@Resource(name="repositoryService")
	private RepositoryService repositoryService;
	
	/**
	 * 启动工作流
	 */
	public ProcessInstance startWorkflow(String key,String businessKey,Map<String, Object> variables) {
		//根据流程定义的key启动工作流
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey, variables);
		return processInstance;
	}

	
	/**
	 * 根据用户Id查询待办任务列表
	 * @param userid 用户id
	 * @param processDefinitionKey 流程定义的key
	 * @return
	 */
	//问题时这里仅仅是当前用户的id来查询，如果流程定义下一任任务为组的话是无查询结果的
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Leave> findUserTask(String userid,String processDefinitionKey){
		//存放当前用户的所有任务
		List<Task> tasks=new ArrayList<Task>();
		//存放所有請假單
		List<Leave> leaves=new ArrayList<Leave>();
		
		//根据当前用户的id查询代办任务列表(已经签收),可以查询taskService.claim(task.getId(), "fozzie"); 认领后的任务
		List<Task> taskAssignees = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).taskAssignee(userid)
				.orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		//根据当前用户id查询未签收的任务列表，未被认领的任务
		List<Task> taskCandidates = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey)
				.taskCandidateUser(userid).orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		tasks.addAll(taskAssignees);//添加已签收准备执行的任务(已经分配到任务的人)
		tasks.addAll(taskCandidates);//添加还未签收的任务(任务的候选者)
		
		/*//	重新编写，某角色下的所有任务展示而非之前限定leave流程
		List<com.lin.domain.Group> gs = mapper.getGroupList(Long.valueOf(userid));
		 tasks = taskService.createTaskQuery().taskCandidateGroup("deptLeader").list();*/
		
		//遍历所有的任务列表,关联实体
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			//根据流程实例id查询流程实例
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
																			.processInstanceId(processInstanceId).singleResult();
			//获取业务id
			String businessKey=processInstance.getBusinessKey();
			//查询请假实体
			Leave leave = leaveService.findById(businessKey);
			//设置属性
			leave.setTask(task);
			leave.setProcessInstance(processInstance);
			leave.setProcessInstanceId(processInstance.getId());
			leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			leaves.add(leave);
		}
		return leaves;
	}
	//增加由用户查询所在的组任务
	//问题时这里仅仅是当前用户的id来查询，如果流程定义下一任任务为组的话是无查询结果的
		@Transactional(propagation=Propagation.REQUIRED)
		public List<Leave> findGroupTask(String userid,List<Group> datas,String processDefinitionKey){
			//存放当前用户的所有任务
			List<Task> tasks=new ArrayList<Task>();
			//存放所有請假單
			List<Leave> leaves=new ArrayList<Leave>();
			List<Task>  gtasks = null;
			for(Group group:datas){
				gtasks = taskService.createTaskQuery().taskCandidateGroup(group.getId()).list();//获取用户组“T_MAN”所有的任务
				tasks.addAll(gtasks);
			}
			//遍历所有的任务列表,关联实体
			for (Task task : tasks) {
				String processInstanceId = task.getProcessInstanceId();
				//根据流程实例id查询流程实例
				ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
																				.processInstanceId(processInstanceId).singleResult();
				//获取业务id
				String businessKey=processInstance.getBusinessKey();
				//查询请假实体
				Leave leave = leaveService.findById(businessKey);
				//设置属性
				leave.setTask(task);
				leave.setProcessInstance(processInstance);
				leave.setProcessInstanceId(processInstance.getId());
				leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
				leaves.add(leave);
			}
			return leaves;
		}
	/**
	 * 查询运行中的流程实例
	 * @param processDefinitionKey 流程定义的key
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Leave> findRunningProcessInstaces(String processDefinitionKey){
		List<Leave> leaves=new ArrayList<Leave>();
		//runtimeService.createProcessInstanceQuery().processDefinitionKey(USERTASK_PROCESS)可知USERTASK_PROCESS为
		//	runtimeService.startProcessInstanceByKey(USERTASK_PROCESS, BUSINESS_KEY,variables);
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()//Number of process instances
																					.processDefinitionKey(processDefinitionKey).list();
		//关联业务实体
		for (ProcessInstance processInstance : processInstances) {
			String businessKey = processInstance.getBusinessKey();
			Leave leave = leaveService.findById(businessKey);
			leave.setProcessInstance(processInstance);
			leave.setProcessInstanceId(processInstance.getId());
			leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			//设置当前任务信息
			//根据流程实例id,按照任务创建时间降序排列,查询一条任务信息
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskCreateTime().desc().listPage(0, 1);
			leave.setTask(tasks.get(0));
			leaves.add(leave);
		}
		return leaves;
	}
	
	/**
	 * 查询已结束的流程实例
	 * @param processDefinitionKey
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public List<Leave> findFinishedProcessInstaces(String processDefinitionKey){
		List<Leave> leaves=new ArrayList<Leave>();
		//根据流程定义的key查询已经结束的流程实例(HistoricProcessInstance)
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().processDefinitionKey(processDefinitionKey).list();
		//关联业务实体
		for (HistoricProcessInstance historicProcessInstance : list) {
			String businessKey = historicProcessInstance.getBusinessKey();
			Leave leave = leaveService.findById(businessKey);
			leave.setHistoricProcessInstance(historicProcessInstance);
			leave.setProcessDefinition(getProcessDefinition(historicProcessInstance.getProcessDefinitionId()));
			leaves.add(leave);
		}
		return leaves;
	}
	/**
	 * 根据流程定义Id查询流程定义
	 */
	public ProcessDefinition getProcessDefinition(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}
	/**
	 * 根据任务Id查询任务
	 * 
	 */
	public TaskEntity findTaskById(String taskId) throws Exception {
		TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();  
        if (task == null) {  
            throw new Exception("任务实例未找到!");  
        }  
        return task; 
	}
}
