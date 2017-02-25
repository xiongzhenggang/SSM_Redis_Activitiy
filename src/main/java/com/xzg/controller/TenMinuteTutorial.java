/**
 * 
 */
package com.xzg.controller;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;

/**
 * @author hasee
 * @TIME 2016年12月19日
 * 注意类的隐藏和实例创建
 */
public class TenMinuteTutorial {
	public static void main(String[] args) {         // 创建 Activiti 流程引擎   
		ProcessEngine processEngine = ProcessEngineConfiguration   
				.createStandaloneProcessEngineConfiguration() 
				.buildProcessEngine(); 
		     // 取得 Activiti 服务  
	RepositoryService repositoryService = processEngine.getRepositoryService();  
	RuntimeService runtimeService = processEngine.getRuntimeService();      
	// 部署流程定义   
	repositoryService.createDeployment()  
	.addClasspathResource("FinancialReportProcess.bpmn20.xml")  
	.deploy();      
	// 启动流程实例  
	String procId = runtimeService.startProcessInstanceByKey("financialReport").getId();  
	// 获得第一个任务   
	TaskService taskService = processEngine.getTaskService();   
	List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("accountancy").list(); 
	for (Task task : tasks) {
		System.out.println("Following task is available for accountancy group: " + task.getName());  
		// 认领任务   
		taskService.claim(task.getId(), "fozzie");   
		}     
	// 查看 Fozzie 现在是否能够获取到该任务   
	tasks = taskService.createTaskQuery().taskAssignee("fozzie").list(); 
	for (Task task : tasks) {  
		System.out.println("Task for fozzie: " + task.getName());      
		// 完成任务
		taskService.complete(task.getId());   
		}     
	System.out.println("Number of tasks for fozzie: "  
		+ taskService.createTaskQuery().taskAssignee("fozzie").count());   
	// 获取 并 认领 第二个任务   
	tasks = taskService.createTaskQuery().taskCandidateGroup("management").list(); 
	for (Task task : tasks) {  
		System.out.println("Following task is available for accountancy group: " + task.getName());  
		taskService.claim(task.getId(), "kermit");  
		}    
	// 完成第二个任务结束结束流程   
	for (Task task : tasks) {  
		taskService.complete(task.getId()); 
		} 

		     // 核实流程是否结束  
	HistoryService historyService = processEngine.getHistoryService(); 
	HistoricProcessInstance historicProcessInstance =   
			historyService.createHistoricProcessInstanceQuery().processInstanceId(procId).singleResult(); 
	System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());  
				} 
		} 
