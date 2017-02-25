package org.activiti.web.simple.webapp.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.web.simple.webapp.service.WorkflowTraceService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/workflow")
public class WorkFlowController {
	@Autowired
	private WorkflowTraceService traceService;
	
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
	
	//RepositoryService :  管理和控制 发布包和流程定义
	@Resource(name="repositoryService")
	private RepositoryService repositoryService;
	//日志信息
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@RequestMapping("/toupload.do")
	public String toupload(){
		return "views/workflow/upload";
	}
	/**
	 * 部署流程定义文件(Spring MVC文件上传)
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/deploy.do",method=RequestMethod.POST)
	public String deploy(@RequestParam("username")String username,@RequestParam("file")MultipartFile file,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes) throws Exception{
		System.out.println(username);
		if(!file.isEmpty()){
			//获取文件字节数组
			byte[] bytes= file.getBytes();
			//获取文件保存路径
			String realPath = request.getSession().getServletContext().getRealPath("/upload");
			/*	MultipartFile类中两个方法区别：
			getName : 获取表单中文件组件的名字
			getOriginalFilename : 获取上传文件的原名*/
			File out=new File(realPath,file.getOriginalFilename());
			//将文件写到指定目录下
			FileUtils.writeByteArrayToFile(out, bytes);
			//FilenameUtils.getExtension文件名的扩展名
			if(FilenameUtils.getExtension(file.getOriginalFilename()).equals("zip")||FilenameUtils.getExtension(file.getOriginalFilename()).equals("bar")){
				ZipInputStream zipInputStream=new ZipInputStream(file.getInputStream());
				//部署流程定义文件
				repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
			}else{
				redirectAttributes.addFlashAttribute("message", "请上传zip或bar格式的文件!");
				return "redirect:/workflow/toupload.do";
			}
			redirectAttributes.addFlashAttribute("message", "文件上传成功!保存在:"+out.getPath());
		}
		return "redirect:/workflow/processlist.do";
	}
	//展示所有部署的流程
	@RequestMapping(value="/processlist.do",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView processlist(HttpServletRequest request, HttpServletResponse response){
		ModelAndView modelAndView=new ModelAndView("views/workflow/processlist");
		/*
		 * 保存两个对象，一个是ProcessDefinition（流程定义），一个是Deployment（流程部署）
		 */
		List<Object[]> objects = new ArrayList<Object[]>();
		//获取所有定义的流程
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		for (ProcessDefinition processDefinition : list) {
			String deploymentId = processDefinition.getDeploymentId();
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			objects.add(new Object[]{processDefinition,deployment});
		}
		modelAndView.addObject("objects",objects);
		return modelAndView;
	}
	/**
	 * 根据流程部署Id和资源名称加载流程资源
	 * @param deploymentId
	 * @param resourceName
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/loadResourceByDeployment.do",method={RequestMethod.GET,RequestMethod.POST})
	public void loadResourceByDeployment(@RequestParam("deploymentId")String deploymentId,@RequestParam("resourceName")String resourceName,HttpServletRequest request, HttpServletResponse response){
		//获得此图像资源,activitiAPI
		InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
		try {
			/*直接写到页面上
			input = new FileInputStream(file);  
	        data = IOUtils.toByteArray(input);*/
			byte[] byteArray = IOUtils.toByteArray(resourceAsStream);//将输出流写到byte数组中
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(byteArray, 0, byteArray.length);
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据流程部署Id级联删除已部署的流程
	 * @param deploymentId
	 * @param request
	 * @param response
	 * @return 跳转到已部署的流程列表
	 */
	@RequestMapping(value="/deleteDeploymentById/{deploymentId}.do",method={RequestMethod.GET})
	public String deleteDeploymentById(@PathVariable("deploymentId")String deploymentId,HttpServletRequest request, HttpServletResponse response,RedirectAttributes redirectAttributes){
		try {
			repositoryService.deleteDeployment(deploymentId,true);//删除已部署的流程
			//redirectAttributes.addFlashAttribute对请求的重定向生效之前被临时存储（通常是在session)中，并且在重定向之后被立即移除
			redirectAttributes.addFlashAttribute("message", "已部署的流程"+deploymentId+"已成功删除!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "已部署的流程"+deploymentId+"删除失败!");
		}
		return "redirect:/workflow/processlist.do";
	}
	/**
	 * 根据流程实例Id和资源类型加载流程资源
	 * @param processInstanceId 流程实例id
	 * @param resourceType 流程资源类型
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/loadResourceByProcessInstance.do",method={RequestMethod.GET,RequestMethod.POST})
	public void loadResourceByProcessInstance(@RequestParam("processInstanceId")String processInstanceId,@RequestParam("resourceType")String resourceType,HttpServletRequest request, HttpServletResponse response){
		//根据流程实例id查询流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		//根据流程定义id查询流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		String resourceName="";
		if(resourceType.equals("xml")){
			//获取流程定义资源名称
			resourceName=processDefinition.getResourceName();
		}else if(resourceType.equals("image")){
			//获取流程图资源名称
			resourceName=processDefinition.getDiagramResourceName();
		}
		//打开流程资源流
		InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
		//输出到浏览器
		try {
			byte[] byteArray = IOUtils.toByteArray(resourceAsStream);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(byteArray, 0, byteArray.length);
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 请求转发到查看流程图页面
	 * @param taskId
	 * @return
	 * 使用{executionId}表达式，传递参数@PathVariable("executionId")String executionId获取传递的参数
	 */
	@RequestMapping(value="/view/{executionId}/page/{processInstanceId}.do",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView viewImage(@PathVariable("executionId")String executionId,@PathVariable("processInstanceId")String processInstanceId){
		ModelAndView modelAndView=new ModelAndView("views/workflow/view");
		modelAndView.addObject("executionId", executionId);
		modelAndView.addObject("processInstanceId", processInstanceId);
		return modelAndView;
	}
	/**
	 * 根据流程实例id查询流程图(跟踪流程图)
	 * @param processInstanceId 流程实例id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/view/{processInstanceId}.do",method={RequestMethod.GET,RequestMethod.POST})
	public void viewProcessImageView(@PathVariable("processInstanceId")String processInstanceId,HttpServletRequest request, HttpServletResponse response){
		InputStream resourceAsStream = null;
		try {
			//根据流程实例id查询流程实例
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			//根据流程定义id查询流程定义
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
			String resourceName=processDefinition.getDiagramResourceName();
			//打开流程资源流
			resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
			runtimeService.getActiveActivityIds(processInstance.getId());
			//输出到浏览器
			byte[] byteArray = IOUtils.toByteArray(resourceAsStream);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(byteArray, 0, byteArray.length);
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 输出跟踪流程信息
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/process/{executionId}/trace/{processInstanceId}.do",produces={MediaType.APPLICATION_JSON_VALUE})
/**RequestMapping注解有六个属性，下面我们把她分成三类进行说明。
1、 value， method；
value：     指定请求的实际地址，指定的地址可以是URI Template 模式（后面将会说明）；
method：  指定请求的method类型， GET、POST、PUT、DELETE等；
2、 consumes，produces；
consumes： 指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
produces:    指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
3、 params，headers；
params： 指定request中必须包含某些参数值是，才让该方法处理。
headers： 指定request中必须包含某些指定的header值，才能让该方法处理请求。*/
	public @ResponseBody Map<String,Object> traceProcess(@PathVariable("executionId") String executionId,@PathVariable("processInstanceId") String processInstanceId) throws Exception {
		//根据executionId查询当前执行的节点
		ExecutionEntity execution=(ExecutionEntity) runtimeService.createExecutionQuery().processInstanceId(processInstanceId).executionId(executionId).singleResult();
		//获取当前节点的activityId
		String activityId=execution.getActivityId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
		List<ActivityImpl> activities = processDefinitionEntity.getActivities();
	/**ActivityImpl表示单个流程元素，比如任务、开始节点、连接线、网关等。它的属性包括：
		variables、activityBehavior、incomingTransitions、outgoingTransitions以及元素界面位置等*/
		Map<String,Object> activityImageInfo=new HashMap<String,Object>();
		for (ActivityImpl activityImpl : activities) {
			String id=activityImpl.getId();
			//判断是否是当前节点
			if(id.equals(activityId)){
				activityImageInfo.put("x", activityImpl.getX());
				activityImageInfo.put("y", activityImpl.getY());
				activityImageInfo.put("width", activityImpl.getWidth());
				activityImageInfo.put("height", activityImpl.getHeight());
				break;//跳出循环
			}
		}
		return activityImageInfo;
	}
	/**
	 * 输出跟踪流程信息
	 * @param processInstanceId 流程实例id
	 * @return 
	 * @throws Exception
	 */
	/*@RequestMapping(value = "/produces", produces = "application/json")：表示将功能处理方法将生产json格式的数据，此时根据请求头中的Accept进行匹配，如请求头“Accept:application/json”时即可匹配;
	@RequestMapping(value = "/produces", produces = "application/xml")：表示将功能处理方法将生产xml格式的数据，此时根据请求头中的Accept进行匹配，如请求头“Accept:application/xml”时即可匹配。*/
	//错误的使用${processInstanceId}
	@RequestMapping(value = "/process/{processInstanceId}/trace.do",produces={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public List<Map<String, Object>> traceProcess(@PathVariable("processInstanceId") String processInstanceId) throws Exception {
		List<Map<String, Object>> activityInfos = traceService.traceProcess(processInstanceId);
		logger.debug("流程跟踪大小：", activityInfos.size());
		return activityInfos;
	}
}