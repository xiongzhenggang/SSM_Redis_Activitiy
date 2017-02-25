package org.activiti.designer.test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestFailure;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
//import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application.xml")
public class WorkFlowTest {
		
    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    ProcessEngineFactoryBean processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private FormService formService ;
    @Test
    public void testEvent() throws InterruptedException {
    //先在activiti中增加相应的利用角色数据,执行一次
    	/* testSaveUser("10000", "张三");
 	  	testSaveUser("10001", "李四");
    	testSaveGroup("T_MAN");
    	testSaveMembership("10001", "T_MAN");*/
    	 /* RepositoryService :  管理和控制 发布包和流程定义(包含了一个流程每个环节的结构和行为) 的操作
  		除此之外，服务可以
    	查询引擎中的发布包和流程定义。
    	暂停或激活发布包，对应全部和特定流程定义。 暂停意味着它们不能再执行任何操作了，激活是对应的反向操作。
    	获得多种资源，像是包含在发布包里的文件， 或引擎自动生成的流程图。
    	获得流程定义的pojo版本， 可以用来通过java解析流程，而不必通过xml。*/
    	/*  activiti中用户角色相关内置的四个表，分别对应相关的操作
		act_id_group 用户组表；
		act_id_user 用户表；
		act_id_membership 用户与组的关联表，用来实现多对多；
		act_id_info 用户信息表；*/
    		/*本次测试使用内置表单form*/ 
    		repositoryService.createDeployment()
            .addClasspathResource("com/lin/workflow/leaveWork.bpmn") 
            // .addClasspathResource("diagrams/form/approve.form")  //添加外置表单部署src/main/resources/
            .deploy();//部署act_re_procdef表记录了每次部署流程定义
    		
    		/*删除部署操作方法 
    		 * 普通删除，如果当前规则下有正在执行的流程，则抛异常
    	   	  repositoryService.deleteDeployment(deploymentId);
    	   	   级联删除,会删除和当前规则相关的所有信息，包括历史
    	   	  repositoryService.deleteDeployment(deploymentId, true);*/
    	System.out.println("=============================流程部署完成！！=============================================");        
    		//创建查询对象 
      		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
      		query.processDefinitionKey("vacationRequest");//通过key获取添加查询条件
      		 	// .processDefinitionName("My process")//通过name获取
      			// .orderByProcessDefinitionId()//根据ID排序
      			   //.processDefinitionKeyLike(processDefinitionKeyLike)//支持模糊查询
      				//.listPage(firstResult, maxResults)//分页
      		List<ProcessDefinition> pds = query.list();//获取批量的明细
      		//.singleResult()//获取单个的明细
      		for (ProcessDefinition pd : pds) {
      			System.out.println("=============================获取key为leaveWork流程批量信息=============================================");
      			System.out.println("ID:"+pd.getId()+",NAME:"+pd.getName()+",KEY:"+pd.getKey()+",VERSION:"+pd.getVersion()+",RESOURCE_NAME:"+pd.getResourceName()+",DGRM_RESOURCE_NAME:"+pd.getDiagramResourceName());
      		}
        System.out.println("Number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());//部署任务数
        
        //业务数据和activiti数据同步，使用identityService操作相应的act_id_*表如上面的用户角色表
        //  IdentityService:  管理（创建，更新，删除，查询...）群组和用户
        //identityService.setAuthenticatedUserId("10000");来设置认证的用户
        Map<String, Object> variables = new HashMap<String, Object>();//作为添加内置表单的数据
        variables.put("employeeName", "10000");
        variables.put("numberOfDays", new Integer(4));
        variables.put("vacationMotivation", "想回家休息怎么搞!");
        //启动流程
       /* RuntimeService : 负责启动一个流程定义的新实例,获取和保存 流程变量,查询流程实例和执行*/
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("vacationRequest", variables);  //表单数据
        System.out.println("=======================当前流程实例总数是："+ runtimeService.createProcessInstanceQuery().count());
        //以startProcessInstanceByKey的方式启动，而非id
        System.out.println("===========启动流程，以下为该流程的ID、name、ProcessDefinitionId、ProcessDefinitionKey============");
        System.out.println(processInstance.getId()+"名字"+processInstance.getName());
        System.out.println(processInstance.getProcessDefinitionId()+"   DefinitionId:DefinitionKey   "+processInstance.getProcessDefinitionKey());
        System.out.println("业务主键==============================："+processInstance.getBusinessKey());
        //要在业务（如leave_list）中添加流程主键字段PROCESS_INSTANCE_ID varchar(64);
       /* start 事件总是捕获型的：从概念上讲，该事件（任何时候）会一直等待直到触发发生
        * start 事件中，可以指定下面的 activiti 所特有的属性
        * formKey：指向一个用户必须在启动新流程实例时填写的表单模板activiti:formKey="org/activiti/examples/taskforms/request.form"
        * initiator：标识流程启动时，存储认证用户 id 的变量名<startEvent id="request" activiti:initiator="initiator" /> 
        * 必须在 tye-finally 块中，使用方法 IdentityService.setAuthenticatedUserId(String)来设置认证的用户
        * try {  identityService.setAuthenticatedUserId("bono"); 
		 runtimeService.startProcessInstanceByKey("someProcessKey"); } 
		 finally {  identityService.setAuthenticatedUserId(null); } 
        * */
        /*TaskService : 所有与任务有关的功能
		查询分配给用户或组的任务
		创建 独立运行任务。这些任务与流程实例无关。
		手工设置任务的执行者，或者这些用户通过何种方式与任务关联。
		认领并完成一个任务。认领意味着一个人期望成为任务的执行者，
 		即这个用户会完成这个任务。完成意味着“做这个任务要求的事情”。 通常来说会有很多种处理形式。*/
        //直接分配给用户的任务可以通过 TaskService 来获取List<Task> tasks = taskService.createTaskQuery().taskAssignee("kermit").list(); 
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("T_MAN").list();//获取用户组“T_MAN”所有的任务
       /* if(tasks.size()==0){
        	  taskService.claim("handleRequest", "T_MAN");//认领，组其他成员的任务列表 中消失
        }*/
        for (Task task : tasks) {
         System.out.println("=============任务名+执行者=========="+task.getName() + " : " + task.getAssignee());
   //通过认领任务，将有专人作为该任务的代理人，同时该任务会从会计组其他成员的任务列表 中消失taskService.claim(task.getId(), "fozzie"); 
          taskService.claim(task.getId(), "T_MAN");//认领，组其他成员的任务列表 中消失
        }
        findTask("T_MAN");
        //使用 taskService 通过添加如下的逻辑就能获得该任务List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("kermit").list(); 
        Task task = tasks.get(0);//获取第一个任务
        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("vacationApproved", "false");
        taskVariables.put("managerMotivation", "我们时间紧迫，不能请假!");//表单内容
        taskService.complete(task.getId(), taskVariables);//完成代办任务
       /* for (Task task : tasks) {
            taskService.complete(task.getId());
            System.out.println("=========完成代办任务========："+task.getName() + " : " + task.getId() + " 完成！！！！ ");
        }*/
        System.out.println("==================================任务完成等待十秒========================================");
        Thread.sleep(10 * 1000);//等待10秒
        //走向下一个任务节点	
        /*System.out.println("====================10001增加任务=======================================");
        tasks = taskService.createTaskQuery().taskAssignee("10001").list(); //查询的任务
        for (Task task : tasks) {
            System.out.println("==========用户10001的任务是================"+task.getName() + " : " + task.getAssignee());
            taskService.claim(task.getId(), "10001");//认领
        }*/
        //利用 FormService.getRenderStartForm 来取得渲染过的表单字符串：
        //String processDefinitionId= (String) FormService.getRenderedStartForm("dd") ;
       /* tasks = taskService.createTaskQuery().taskAssignee("10001").list();
        for (Task task : tasks) {
            taskService.complete(task.getId());
            System.out.println("================用户10001完成任务============"+task.getName() + " : " + task.getId() + " 完成！！！！ ");
        }
        System.out.println("=======================10001完成任务===========================================");*/
        /*  Activiti任务认领TaskService taskService;
        taskService.setAssignee(String taskId, String userId);
        taskService.claim(String taskId, String userId);
        taskService.setOwner(String taskId, String userId);
      关于上面三个方法的区别：setAssignee和claim两个的区别是在认领任务时，
        claim会检查该任务是否已经被认领，如果被认领则会抛出ActivitiTaskAlreadyClaimedException 而setAssignee不会进行这样的检查，
        其他方面两个方法效果一致。
        setOwner和setAssignee的区别在于setOwner实在代理任务时使用，代表着任务的归属者，而这时，setAssignee代表的时代理办理者，
        举个例子来说，公司总经理现在有个任务taskA，去核实一下本年度的财务报表，他现在又很忙没时间，
        于是将该任务委托给其助理进行办理，此时，就应该这么做：
        taskService.setOwner(taskA.getId(), 总经理.getId());taskService.setAssignee/claim(taskA.getId(), 助理.getId());*/
        tasks = taskService.createTaskQuery().taskCandidateGroup("T_MAN").list();//获取T_MAN组下的任务 
        for (Task task1 : tasks) {
        	System.out.println("=================该组下的可用任务: =================" + task1.getName());   
        	//taskService.claim(task.getId(), "kermit"); 
        }
/*//流程暂挂状态新的流程不能开启
        repositoryService.suspendProcessDefinitionByKey("vacationRequest");
        try {
          runtimeService.startProcessInstanceByKey("vacationRequest");
        } catch (ActivitiException e) {
          e.printStackTrace();
        }*/
        taskVariables = new HashMap<String, Object>();
        variables.put("numberOfDays", new Integer(2));
        variables.put("vacationMotivation", "想回家休息怎么搞!");
        variables.put("resendRequest", "false");//是否给经理查看
        //返回员工请假
        tasks = taskService.createTaskQuery().taskAssignee("10000").list();//三个任务？？其中有${resendRequest == 'true'}（忘记认领）原因就是任务没通过认领就进行完成操作，导致出错
     /*   删除任务的关系，首先删除历史任务表然后依次寻主外键关联删除，顺序如下
      * act_hi_procinst 流程实例历史表  
      * act_hi_actinst 存放历史所有完成的任务 
      * act_hi_taskinst 代办任务历史表 （只对应节点是UserTask的） 
      * act_hi_actinst  所有节点活动历史表 （对应流程的所有节点的活动历史，从开始节点一直到结束节点中间的所有节点的活动都会被记录） 
      * act_hi_variable 流程变量历史表 
      * act_ru_identitylink
      * act_ru_task 代办任务表 （只对应节点是UserTask的）
      * act_ru_variable 正在执行的流程变量表 
      * act_ru_execution 正在执行的执行对象表（包含执行对象ID和流程实例ID，如果有多个线程可能流程实例ID不一样
      * */
     //  taskService.complete(tasks.get(0).getId(),taskVariables);
  for (Task task1 : tasks) {
	  //先进行认领操作
	  taskService.claim(task1.getId(), "10000");//认领，组其他成员的任务列表 中消失
	  /*设置流程变量的几种方式
	   * 1、利用setVariables方法在任务办理过程中添加一批流程变量
	   */	 
	  taskService.setVariables(task1.getId(), variables );
	  		/**
	  		 * 2、在任务完成时设置流程变量
	  		 * taskService.complete(task1.getId(),taskVariables);
	  		 */
	  		taskService.complete(task1.getId());
	  		System.out.println("================用户员工再次完成任务============"+task1.getName() + " : " + task1.getId() + " 完成！！！！ ");
        /**3、通过表单设置
         * public void setVarByObj() throws Exception {
        String processInstanceId = "1901";
        Task task =taskService.createTaskQuery().taskAssignee("manager").processInstanceId(processInstanceId ).singleResult();
        Form form = new Form();//这个javabean实现了Serializable接口
 		form.setName("表单名称");
        form.setContent("我是张三，我要请假3天");
        taskService.setVariable(task.getId(), "form", form);
    	} */
  }
  
 /* String candidateUser = "10000";  
  List<Task> list = processEngine.getProcessEngineConfiguration()//  
                  .createTaskQuery()//  
                  .taskCandidateUser(candidateUser)//参与者，组任务查询  
                  .list();  
  if(list!=null && list.size()>0){ 
      for(Task task1:list){
          System.out.println("任务ID："+task1.getId());  
          System.out.println("任务的办理人："+task1.getAssignee());  
          System.out.println("任务名称："+task1.getName());  
          System.out.println("任务的创建时间："+task1.getCreateTime());  
          System.out.println("流程实例ID："+task1.getProcessInstanceId());  
          System.out.println("#######################################");  
      } 
  }  */
  
//  HistoryService:  提供了Activiti引擎的所有历史数据
        HistoricProcessInstance hpInstance = 
                historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getId()).singleResult();
        System.out.println("========================流程结束的时间=============: " + hpInstance.getEndTime());
  
        //查询10000历史任务====核实流程是否结束
    String assignee = "10000";
    List<HistoricTaskInstance> htis = historyService.createHistoricTaskInstanceQuery()//创建历史任务查询对象
			//创建一个历史详细信息查询对象
			//.createHistoricDetailQuery()
			//创建历史流程实例查询对象
			//.createHistoricProcessInstanceQuery()
			.taskAssignee(assignee)//根据接受人查询历史任务
			.list();//返回批量结果
	for (HistoricTaskInstance hti : htis) {
		System.out.println("=====================10000的ID:"+hti.getId()+",流程实例ID:"+hti.getProcessInstanceId()+",任务接收人:"+hti.getAssignee()+"任务执行对象ID:"+hti.getExecutionId());
	}
	System.out.println("=========================================流程结束============================================");
  //  其他一些服务
/*   FormService:  一个可选服务,这个服务提供了 启动表单 和 任务表单 两个概念
  ManagementService : 在使用Activiti的定制环境中基本上不会用到。 它可以查询数据库的表和表的元数据。
  另外，它提供了查询和管理异步操作的功能。*/
}
  /*  添加用户测试*/
/*    public void testSaveUser(String userid,String username){
       // IdentityService identityService=processEngine.getIdentityService();
        User user1=new UserEntity(); // 实例化用户实体
        user1.setId(userid);
        user1.setLastName(username);
        identityService.saveUser(user1); // 添加用户
           }*/
        
        public void testFinduser(String userid){
        	
    }
 
    /**
     * 测试删除用户
     */
    public void testDeleteUser(String userid){
       // IdentityService identityService=processEngine.getIdentityService();
        identityService.deleteUser(userid); 
    }
    /*
     * 测试添加组
     */
    public void testSaveGroup(String groupid){
       // IdentityService identityService=processEngine.getIdentityService();
        Group group1=new GroupEntity(); // 实例化组实体
        group1.setId(groupid);
        identityService.saveGroup(group1);
    }
    /**
     * 测试删除组
     */
    public void testDeleteGroup(String groupid){
       // IdentityService identityService=processEngine.getIdentityService();
        identityService.deleteGroup(groupid);
    }
    /**
     * 测试添加用户和组关联关系
     */
    public void testSaveMembership(String userid,String groupid){
       // IdentityService identityService=processEngine.getIdentityService();
    	identityService.createMembership(userid, groupid);
    }
    /**
     * 测试删除用户和组关联关系
     */
    public void testDeleteMembership(String userid,String gropid){
      //  IdentityService identityService=processEngine.getIdentityService();
        identityService.deleteMembership(userid, gropid);
    }
    public void findTask(String groupid){
        List<Task> taskList=taskService // 任务相关Service
            .createTaskQuery() // 创建任务查询
            //.taskAssignee("李四") // 指定某个人
            .taskCandidateGroup(groupid)//指定组
          //  .taskCandidateUser("zhangsan") // 指定候选人
            .list();
        for(Task task:taskList){
            System.out.println("任务ID:"+task.getId()); 
            System.out.println("任务名称:"+task.getName());
            System.out.println("任务创建时间:"+task.getCreateTime());
            System.out.println("任务委派人:"+task.getAssignee());
            System.out.println("流程实例ID:"+task.getProcessInstanceId());
        }
    }
    /* 
	*部署流程定义 （从zip）
     */
/*    @Test
    public void deploymentProcessDefinition_zip(){
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/HelloWorld.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                        .createDeployment()//创建一个部署对象
                        .name("流程定义")//添加部署名称
                        .addZipInputStream(zipInputStream)//完成zip文件的部署
                        .deploy();//完成部署
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署名称:"+deployment.getName());
    }*/
    /**
     * 删除流程定义
     */
    public void deleteProcessDefinition(){
      //  IdentityService identityService=processEngine.getIdentityService();
    	repositoryService.deleteDeployment("15001",true);
    	System.out.println("删除成功");
    }
}