package org.activiti.web.simple.webapp.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.web.simple.webapp.model.Leave;
import org.activiti.web.simple.webapp.service.LeaveService;
import org.activiti.web.simple.webapp.service.LeaveWorkFlowService;
import org.activiti.web.simple.webapp.util.Variable;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.xzg.domain.User;

@Controller
@RequestMapping("/leave")
public class LeaveController {
	@Resource(name="leaveServiceImpl")
	private LeaveService leaveService;
	
	@Resource(name="leaveWorkFlowServiceImpl")
	private LeaveWorkFlowService leaveWorkFlowService;
	
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
	
	@Resource//(name="formService")
	private FormService formService;
	
	@Resource(name="repositoryService")
	private RepositoryService repositoryService;
	
	
	@RequestMapping(value="/form.do",method={RequestMethod.GET})
	public String toleaveForm(){
		return "views/leave/leaveform";
	}
	
	/**
	 * 收集表单信息。启动工作流
	 * @param leave 自动绑定表单提交过来的数据
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/start.do",method={RequestMethod.POST})
	public String startWorkFlow(Leave leave,HttpSession session,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		Map<String, Object> variables=new HashMap<String, Object>();
		//设定邮件发送人和邮件收件人
		variables.put("from", "184675420@qq.com");
		com.xzg.domain.User user=(com.xzg.domain.User) session.getAttribute("loginuser");
		if(user!=null){
			//关联用户id
			leave.setUserId(String.valueOf(user.getUserId()));	
			leave.setApplyTime(Calendar.getInstance().getTime());
			variables.put("to", user.getEmail());
			//先持久化请假实体
			leaveService.save(leave);
			//与业务绑定(将请假实例的id与流程实例绑定),通过请假单据的主键id号关联本流程
			String businessKey=leave.getId().toString();
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(String.valueOf(user.getUserId()));
			try {
				ProcessInstance processInstance = leaveWorkFlowService.startWorkflow("leave",businessKey,variables);
				//processInstance.getProcessDefinitionKey();
				/*//1  获取任务对象
        			Task task  =  taskService.createTaskQuery().taskId(taskId).singleResult();
        			2  通过任务对象获取流程实例
        			ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
        			3 通过流程实例获取“业务键”
        			String businessKey = pi.getBusinessKey();
				 * */
				leave.setProcessInstance(processInstance);
				leave.setProcessInstanceId(processInstance.getId());
				redirectAttributes.addFlashAttribute("message", "流程已启动，流程ID：" + processInstance.getId());
			} catch (ActivitiException e) {
				if (e.getMessage().indexOf("no processes deployed with key") != -1) {
					redirectAttributes.addFlashAttribute("message", "没有部署流程");
					return "redirect:/workflow/toupload.do";
				} else {
					redirectAttributes.addFlashAttribute("message", "系统内部错误！");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", "系统内部错误！");
			}
		}else{
			//跳转的未controller是为了避免，之前设置的ViewResolver将其渲染成login.jsp，所以要加前缀froward（转发）redirect（重定向）
			//在重定向时，为了保存某些需要的数据，使用redirectAttributes.addFlashAttribute（）方法保存
			return "redirect:/login.do	";//跳转到登录界面
		}
		return "redirect:/leave/form.do"; //跳转到原来的页面
	}
	/**
	 * 根据用户Id查询待办任务列表
	 * @param userid
	 * @return
	 */
	@RequestMapping(value="/task/list.do",method={RequestMethod.GET})
	public ModelAndView findTask(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		ModelAndView modelAndView=new ModelAndView("views/leave/tasklist");
		com.xzg.domain.User user=(com.xzg.domain.User) session.getAttribute("loginuser");
		List<Leave> tasklist = leaveWorkFlowService.findUserTask(String.valueOf(user.getUserId()),"leave");
		//2017-01-23如果该用户没有任务，去查看该用户所担当的角色的任务
		if(tasklist.size()<=0){
			List<Group> datas = identityService.createGroupQuery().groupMember(String.valueOf(user.getUserId())).list();//该用户所在的组
			//List<User> datas = identityService.createUserQuery().MemberOfGroup(group.getId()).list();//该组下的用户
			tasklist  = leaveWorkFlowService.findGroupTask(String.valueOf(user.getUserId()), datas, "leave");
		}
		modelAndView.addObject("tasklist", tasklist);
		return modelAndView;
	}
	/**2017-02-06新增
	 * 查询流程实例
	 * @param processDefinitionKey //流程定义key
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="process/all/leave/list.do",method={RequestMethod.GET})
	public ModelAndView findAllProcessInstaces(){
		ModelAndView modelAndView=new ModelAndView("views/leave/all_processinstance");
		//获取所有定义的流程
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		modelAndView.addObject("processInstaces", list);
		return modelAndView;
	}
	
	/**
	 * 根据流程定义的key查询运行中的流程实例
	 * @param processDefinitionKey //流程定义key
	 * @param request
	 * @param response
	 * @return
	 */
	//只部署了leave作为key的流程，所以以下查询都key均为leave
	@RequestMapping(value="/process/running/{processDefinitionKey}/list.do",method={RequestMethod.GET})
	public ModelAndView findRunningProcessInstaces(@PathVariable("processDefinitionKey")String processDefinitionKey,HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView=new ModelAndView("views/leave/run-proc");
		List<Leave> runningProcessInstaces = leaveWorkFlowService.findRunningProcessInstaces(processDefinitionKey);
		modelAndView.addObject("runprocess", runningProcessInstaces);
		return modelAndView;
	}
	/**
	 * 查询已结束的流程实例
	 * @param processDefinitionKey
	 * @return
	 */
	@RequestMapping(value="/process/finished/{processDefinitionKey}/list.do",method={RequestMethod.GET})
	public ModelAndView findFinishedProcessInstaces(@PathVariable("processDefinitionKey")String processDefinitionKey,HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView=new ModelAndView("views/leave/end-proc");
		List<Leave> finishedProcessInstaces = leaveWorkFlowService.findFinishedProcessInstaces(processDefinitionKey);
		modelAndView.addObject("endprocess", finishedProcessInstaces);
		return modelAndView;
	}
	/**
	 * 根据任务Id签收任务
	 * @param userid
	 * @return
	 */
	@RequestMapping(value="/task/{taskId}/claim.do",method={RequestMethod.GET})
	public String claimTask(@PathVariable("taskId")String taskId,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes,HttpSession session){
		User user=(User) session.getAttribute("loginuser");
		taskService.claim(taskId, String.valueOf(user.getUserId()));//签收
		redirectAttributes.addFlashAttribute("message", "任务签收成功!");
		return "redirect:/leave/task/list.do";//跳转到待办任务列表
	}
	/**
	 * 根据任务Id完成任务
	 * @param userid
	 * @return
	 */
	@RequestMapping(value="/task/{taskId}/complete.do",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String completeTask(@PathVariable("taskId")String taskId,Variable variable,RedirectAttributes redirectAttributes){
		String message="";
		try {
			//Variable自定义存储数据的bean
			Map<String, Object> variables = variable.getVariableMap();
			taskService.complete(taskId,variables);
			message="success";
		} catch (Exception e) {
			message="error";
		}
		return message ;//返回办理信息到viewform
	}
	/**
	 * 获取请假实体
	 * @param id id
	 * @return
	 */
	@RequestMapping(value="/detail/{id}/leave.do",method={RequestMethod.GET})
	public @ResponseBody Leave getLeaveById(@PathVariable("id")String id){
		Leave leave = leaveService.findById(id);
		return leave;
	}
	/**
	 * 根据请假id和任务id获取实体
	 * @param id
	 * @param taskId
	 * @return
	 */
	//办理任务
	@RequestMapping(value="/detail/leave/{taskId}.do",method={RequestMethod.GET})
	public ModelAndView getLeaveWithVars(@PathVariable("taskId") String taskId){
		ModelAndView modelAndView=new ModelAndView("views/leave/viewform");
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();//获取任务
		ExecutionEntity executionEntity=(ExecutionEntity) runtimeService.createExecutionQuery().executionId(task.getExecutionId()).processInstanceId(task.getProcessInstanceId()).singleResult();
		/*ExecutionEntity实现了接口ActivityExecution，ActivityExecution是流程运行管理接口。ExecutionEntity提供的功能如下：
		 * 启动、结束、销毁流程。对流程元素ActivityImpl的管理（添加删除修改父节点、实例ID、任务等）。
		*ExecutionEntity里几个重要的方法：initialize()里面初始化task、job、variable、event。start()是流程启动方法
		 * performOperation()是流程执行方法，流程的推动都调用performOperation()。
		 */
		//获取当前正在执行的节点
		String activityId = executionEntity.getActivityId();
		String processInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String businessKey = processInstance.getBusinessKey();
		Leave leave = leaveService.findById(businessKey);
		Map<String, Object> variables = taskService.getVariables(taskId);
		leave.setVariables(variables);
		modelAndView.addObject("taskId", taskId);
		modelAndView.addObject("leave", leave);
		modelAndView.addObject("activityId", activityId);
		return modelAndView;
	}
	/*2017-01-22增加在springmvc中如果表单属性的类型是日期型时，从页面绑定字符串数据会出错 */
	//SimpleDateFormat日期格式与页面日期格式要一致！ 对应的controller中增加属性编辑器
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}	
}
