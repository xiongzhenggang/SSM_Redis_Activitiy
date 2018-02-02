## 项目开始
* 首先将本地项目创建本地仓库提交到github
* 步骤1、在本项目打开git shell。执行git init初始化git仓库
* 步骤2、使用客户端，提交到change代码
* 步骤3、sync同步到github

### 本应用在原有的基础上进行了大部分的修改。
 主要涉及三个方面：
 其一：是关于用户、角色、权限的设计，通过jQuery z_tree插件，将查询的权限菜单以json的形式传递给z_tree借助该插件
 可以很容易的实现导航栏功能和角色的对应。
 其二：本练习使用的工作流框架为activiti，通过覆盖原有的api设计自定义的用户角色信息。流程为请假流程
 * 本项目的activit是基于activitie5。当前版本activiti6的实现方式参
 ！[springboot集成activitie6及相关用户自定义实现](https://github.com/xiongzhenggang/SpringBootLearn/tree/master/springboot-shiro)
 其三：登陆上设计单点登录和本地cookie自动的登录
 ## ssm主要技术实现方式：
* 视图使用jsp技术，传递数据到控制层有两种方式如下：
 1. 普通的form表单提交
  ```html
  <form action="${ctx }/loginin.do" method="post">
        	用户名:<input name="username" id="username" type="text"/><br/>
        	密&nbsp;&nbsp;码:<input name="password"  id="password"  type="password"/><br/>
         <input name="submit" id="submit" type="submit" value="登录"/>
   </form>
   ```
2. 使用ajax
```js
var url ='${ctx}/saveCookie.do';<br />
	   var username= $("input[id='username']").val();
	   var password= $("input[id='password']").val();
          //利用对话框返回的值 （true 或者 false）  
          if (confirm("确定保存密码？")) {  
        	 //使用ajax异步处理
        	 $.ajax({
    		type:"POST", 
    		url:url,  /* 这里就是action名+要执行的action中的函数 */
    		contentType: "application/json; charset=utf-8",
    		data:JSON.stringify({username:username,password:password}),  //url后面要传送的参数    
    		async: true,
    		dataType : "json",
    		success:function(data)
```
* 同样接受数据对应的两种方式
1. el表达式
```
    <c:forEach var="user" items="${listuser }">
		<tr align="center">
			<td>${user.userId }</td>
			<td>${user.userName }</td>
			<td>${user.roleName }</td>
			<td>${user.email }</td>
			<td>
				<a href="#"  onclick="showUpdate(${user.userId })"  target="main" >修改</a>
				<a href="${ctx}/${user.userId }/deleteUserById.do" target="main">删除</a>
			</td>
		</tr>
	</c:forEach>
 ```
2. ajax的回调函数（json格式）
这里就需要要求controller返回值为json格式的数据了
 ```js
		$.ajax({
    		type:"POST",       
    		url:url,  /* 这里就是action名+要执行的action中的函数 */
    		contentType: "application/json; charset=utf-8",
    		data:JSON.stringify({id:id}),  //url后面要传送的参数    
    		async: true,
    		dataType : "json",
    		success:function(data){
    			if(data != null){
    				$("#tab").hide();
    			}
    		var result="<form action='${ctx }/updateAuthorityById.do'  method='post'>";
    		result+="权限编号：<input id='actionId' name='actionId'  type='text' readonly='true' value='"+data.authority.actionId+"'/><br/>";
    		result+="权限名称：<input id='actionName' name ='actionName'  type='text' value='"+data.authority.actionName+"' /><br/>";
    		result+="动作地址：<input id='url' name ='url'  type='text' value='"+data.authority.url+"' /><br/>";
    		result+='<br/><input type="submit" id="update"  value="确定修改"/></form>';
    		 $("#showUpdateAuthority").append(result);
    			}
    		});
```
### 接下来就是后台的springmvc的controller接受数据和返回数据的方式（注意注解的意义）
 从视图层接收数据
 1. 从普通form表单传递的数据处理
 * 方式1，直接@RequestParam("username")的注解方式取得form表单name为username的值，本质上等同于servlet中的getparameter
 ```java
	 @RequestMapping(value="/loginin.do",method={RequestMethod.POST,RequestMethod.GET})
public String loginin(@RequestParam("username")String userid,@RequestParam("password")String password)
```
 通过这中方式直接取得。
* 方式2、使用restful风格的取得
```java
	 @RequestMapping(value="/memberofgroup/{groupId}.do",method={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView memberOfGroup(@PathVariable("groupId")String groupId)
```
其中{groupId}.do应该对应前台的action地址
```xml
	 <a href="${ctx }/memberofgroup/${group.roleId}.do" target="main">查看该组的用户</a>
```
对应方可获取数据。当然记得在方法中使用@PathVariable注解。
 * 方式3、springmvc也会将form表单中的数据封装成对象类似struts2的方式
 
```java
	 @RequestMapping( "/updateUser.do")
public ModelAndView showUdateUser(User user)如同与方式1类似
```
2. 从ajax获取的json数据 关键注解@ResponseBody//在springMVC中提供了JSON响应的支持
* 方式1
 ```java
	 @RequestMapping(value="/showUpdateAuthorityById.do",method={RequestMethod.POST,RequestMethod.GET})
@ResponseBody
public Map<String,Object> showUpdateAuthorityById(@RequestBody  Map<String, String> map){
String authorityId;
	if(map.containsKey("id")){
		authorityId=map.get("id");
	}else{
		authorityId="";
	}
```
 ### 其中map用于节后前台传递的json数据，当然也可以使用list、set等集合接收。
  controller返回视图或数据：
* 方式1、ModelAndView返回渲染后的视图包括数据
```java
List<Authority> authoritys = activitiWorkflowLogin.authorityList();
	ModelAndView modelAndView=new ModelAndView();
	modelAndView.setViewName("views/authority/authority");
	modelAndView.addObject("authoritys", authoritys);
	return modelAndView;
  ```
setViewName的方法设置返回jsp视图的地址，addObject添加的是要返回的数据，在前端页面可以直接使用el表达式表示

* 方式2、只返回数据一般是视图层的ajax调用成功后获取相应的json数据。
```java
  @RequestMapping(value="/showUpdateUserById.do",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> showUpdateByid(@RequestBody  Map<String, String> map){
		String userid;
    	if(map.containsKey("id")){
    		userid=map.get("id");
    	}else{
    		userid="";
    	}
		Map<String,Object> mapout = new HashMap<String, Object>();
		com.xzg.domain.User user = activitiWorkflowLogin.getUserInfo(userid);
		mapout.put("user",user);
		return mapout;
	}
```
从视图层获取ajax异步传递json数据然后返回json数据的方法如上方法。在ajax成功后直接调用	
```xml
	result+='用户邮箱：<input id="email"  name="email" type="text" value="'+ data.user.email+'"/>' ;
```
即可
 * 方法3、则是重定向（应与转发区别）
 以下是两者之间的区别和处理过程。
 转发过程：客户浏览器发送http请求——》web服务器接受此请求——》调用内部的一个方法在容器内部完成请求处理和转发动作——》将目标资源发送给客户；在这里，
转发的路径必须是同一个web容器下的url，其不能转向到其他的web路径上去，中间传递的是自己的容器内的request。在客户浏览器路径栏显示的仍然是其第一次访问的路径，
也就是说客户是感觉不到服务器做了转发的。转发行为是浏览器只做了一次访问请求。

重定向过程：客户浏览器发送http请求——》web服务器接受后发送302状态码响应及对应新的location给客户浏览器——》客户浏览器发现是302响应，
则自动再发送一个新的http请求，请求url是新的location地址——》服务器根据此请求寻找资源并发送给客户。在这里location可以重定向到任意URL，
既然是浏览器重新发出了请求，则就没有什么request传递的概念了。在客户浏览器路径栏显示的是其重定向的路径，客户可以观察到地址的变化的。
重定向行为是浏览器做了至少两次的访问请求的。


* 重定向的方法就是在controler方法返回string类型的地址，那么在pringmvc的配置文件会加上后缀和前缀配置的xml如下：
 ```xml
<!-- 对模型视图添加前后缀 -->  
     <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver"  
      p:prefix="/WEB-INF/" 
      p:suffix=".jsp"/>
 ```
  以下是使用的具体事例方法：
  1. 字符串代表逻辑视图名，注意这里不是重定向！
 ```java
  @RequestMapping(value="/login.do",method={RequestMethod.GET,RequestMethod.GET})
	public String login(){
		return "views/login";
	}
 ```
  2. 以上也可以携带数据
 ```java
   @RequestMapping(value="/login.do",method={RequestMethod.GET,RequestMethod.GET})
   public String login(Model model){
model.addAttribute(attrName,attrValue);//相当于ModelAndView的addObject方法
return "views/login";
   }
```
  3. 需要使用redirect重定向
  redirect的特点和servlet一样，使用redirect进行重定向那么地址栏中的URL会发生变化，同时不会携带上一次的request。不过可以使用redirectAttributes来
  携带数据。具体使用方法如下：
```java
  @RequestMapping(value="/{Id}/deleteUserById.do" ,method={RequestMethod.POST,RequestMethod.GET})
	public  String deleteUserById(@PathVariable("Id") String userId,RedirectAttributes redirectAttributes){
		String message="";
		try{
			accountService.deleteUser(userId);
			message="删除用户成功！";
		}catch(Exception e){
			message="删除用户失败！";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/userlist.do";//重定向到用户管理界面
	}
 ```
### 在jsp页面直接使用el表达式{message}即可展示传递的信息。

## controler或者services层与dao层之间数据交互
* 持久层的dao使用mybitis，它有两种方式来处理sql，其一使用注解，其二使用xml的mapper。当然两种均时手动写sql。
首先相关配置：数据源配置、事务管理略
在spring的配置文件application.xml中 <import resource="applicationContext-mybatis.xml" />导入mybits的配置文件
 ```xml
 <!--  开启二级缓存 -->
      <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">  
        <property name="dataSource" ref="dataSource" />  
        <property name="configLocation" value="classpath:mybatis-config.xml"></property>  
        <property name="mapperLocations" value="classpath*:com/xzg/mapper/**/*.xml"/>  
 </bean> 
    <!-- 自动扫描了所有的XxxxMapper.xml对应的mapper接口文件，这样就不用一个一个手动配置Mpper的映射了，只要Mapper接口类和Mapper映射文件对应起来就可以了。 -->  
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">  
      	<!-- DAO接口所在包名，Spring会自动查找其下的类 --> 
        <property name="basePackage"  value="com.xzg.dao" /> 
     	  <!--  20170115增加，mybitis扫描接口包，用于实现方法--> 
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>  
    </bean> 
    <!--导入缓存的配置文件，具体参照项目-->
    <import resource="applicationContext-ehcache.xml" />
 ```
   以下为具体的使用方法：
  1. 使用mapper
   定义dao的接口ActivitiWorkflowLogin，下面为部分方法。
```java
    /**
     * 验证登录,mybits传递多个参数时，有三种方式1、如下。2，where user_name = #{0} and user_area=#{1}#{0}代表接收的是dao层中的第一个参数，
     * #{1}代表dao层中第二参数，更多参数一致往后加即可。3，采用Map传多参数.Public User selectUser(Map paramMap);
     * where user_name = #{userName，jdbcType=VARCHAR} and user_area=#{userArea,jdbcType=VARCHAR}
     */
	public int login(@Param("userId")String userId, @Param("password")String password) throws Exception;
	/**
	 * 获取用户详细信息验证
	 */
	public User getUserInfo(String userId);
	
  //略
 ```
* 所有这些dao的接口方法要与定义的mapper.xml文件对应如下： 首先要定义<mapper namespace="com.xzg.dao.ActivitiWorkflowLogin">用于识别dao,部分xml的sql
```xml
	 <!-- 开启本mapper的namespace下的二级缓存-->
<cache />
<select id="getListUser" useCache="true" resultType="com.xzg.domain.User">
	select * from user
</select>
<select id="getListGroup" useCache="true" resultType="com.xzg.domain.Group">
	select * from role
</select>
<select id="login"  useCache="true"  flushCache="true" resultType="java.lang.Integer">    
	select count(*)  from User where userId = #{userId}  and password = #{password} 
</select>
```
然后设计ado的实现类ActivitiWorkflowLoginImple.java
```java
@Service(value ="ActivitiWorkflowLoginImple")//用于识别该类为ActivitiWorkflowLogin的dao实现类
public class ActivitiWorkflowLoginImple implements ActivitiWorkflowLogin {
//将dao接口注入
@Resource
	private ActivitiWorkflowLogin activitiWorkflowLogin;
	/* (non-Javadoc)
	 * @see com.lin.dao.ActivitiWorkflowLogin#login(java.lang.String, java.lang.String)
	 */
	@Transactional//事务管理注入
  public int login(String userId, String password) throws Exception {
		// TODO Auto-generated method stub
		int result = activitiWorkflowLogin.login(userId, password);
		return result;
	}
	@Transactional
	public User getUserInfo(String userid) {
		// TODO Auto-generated method stub
		User user = activitiWorkflowLogin.getUserInfo(userid);
		return user;
	}
```
  * 接下来就是在service或controler中的使用，controller或service中可以直接注入dao接口，但是名称需要用之前实现了dao接口
  的实现类ActivitiWorkflowLoginImple中注解@Service(value="ActivitiWorkflowLoginImple")的name，这样在通过接口调用方法。同理在service中调用也相同。
```java
		@Controller
public class AccountController {
	//修改自定义用户
	@Resource(name="ActivitiWorkflowLoginImple")
	private ActivitiWorkflowLogin activitiWorkflowLogin;
方式2、使用的是mybits的注解没这样更加清楚明白。不需要额外的mapper的配置。具体如下：
和上面的方法总体思路相同，唯一不同的地方是在dao接口的实现类。注解的方式是不需要和配置文件关联
public interface StudentMapper  
{  
    @Insert("INSERT INTO STUDENTS(STUD_ID,NAME,EMAIL,ADDR_ID, PHONE)  
            VALUES(#{studId},#{name},#{email},#{address.addrId},#{phone})")  
    int insertStudent(Student student);  
} 
```
* 至于的接口实现类和controler、service调用不变。具体的相关内容课参考mybits的使用文档，内容很详细。
## 以上就是ssm框架的大致实现方式，当然这也仅仅是部分内容，具体还是用参考相关使用文档
下面在说点z_tree的，下载好插件后，一般只需要改改其中一个函数，当然必须要有jQuery
```html
function onLoadZTree(url){
  var treeNodes;
  $.ajax({
    async:false,//是否异步
    cache:false,//是否使用缓存
    type:'POST',//请求方式：post
    dataType:'json',//数据传输格式：json
    url:url,//请求的action路径
    error:function(){
      //请求失败处理函数
      alert('亲，请求失败！');
    },
    success:function(data){
      //console.log(data);
      //请求成功后处理函数
      treeNodes = data;//把后台封装好的简单Json格式赋给treeNodes
    }
  });
  var zTree;
  var setting = {
    view: {
      dblClickExpand: false,//双击节点时，是否自动展开父节点的标识
      txtSelectedEnable: true,//分别表示 允许 / 不允许 选择 zTree Dom 内的文本
      showLine: false,//是否显示节点之间的连线
      fontCss:{'color':'black','font-weight':'bold'},//字体样式函数
      selectedMulti: false //设置是否允许同时选中多个节点
    },
    check:{
      //chkboxType: { "Y": "ps", "N": "ps" },
      chkStyle: "checkbox",//复选框类型
      enable: false//每个节点上是否显示 CheckBox 
    },
    data: {
      simpleData: {//简单数据模式
        enable:true,
        idKey: "id",
        pIdKey: "pId",
        rootPId: ""
      },
      key:{
    	  checked: "id",
    	  target:"target" ,//指向url的target
    	  url:"url"
      }
    },
    callback: {//回调函数
      	beforeClick: function(treeId, treeNode) {
        zTree = $.fn.zTree.getZTreeObj("user_tree");
        //  zTree.checkNode(treeNode, !treeNode.checked, true, true);//单击勾选，再次单击取消勾选
        zTree.expandAll(true); //修改为展开所有
        return true;
      }
    },
    onClick: function(treeId,treeNode){
    	treeNode.open();
    }
  };
  var t = $("#user_tree");
  t = $.fn.zTree.init(t,setting,treeNodes);
}
 ```
其他关于具体的相关的包括用户权限角色、cookie加密登录、activiti使用方法等等具体在项目中
下面是实现cookie登录的思路
1． 保存用户信息阶段：
<p>
当 用户登陆网站时，在登陆页面填写完用户名和密码后，如果用户在提交时还选择了“两星期内自动登陆”复选框，那么在后台程序中验证用户名和密码全都正确后，

还要为用户保存这些信息，以便用户下一次可以直接进入网站；如果用户没有勾选“两星期内自动登陆”复选框，则不必为用户保存信息，那么用户在下一次登陆网 站时仍需要填写用户名和密码。

在保存用户信息阶段，主要的工作是对用户的信息进行加密并保存到客户端。加密用户的信息是较为繁琐的，大致上可分为以下几个步聚：

① 得到用户名、经MD5加密后的用户密码、cookie有效时间(本文设置的是两星期，可根据自己需要修改)

② 自定义的一个webKey，这个Key是我们为自己的网站定义的一个字符串常量，这个可根据自己需要随意设置

③ 将上两步得到的四个值得新连接成一个新的字符串，再进行MD5加密，这样就得到了一个MD5明文字符串

④ 将用户名、cookie有效时间、MD5明文字符串使用“：”间隔连接起来，再对这个连接后的新字符串进行Base64编码

⑤ 设置一个cookieName,将cookieName和上一步产生的Base64编码写入到客户端。
 </p>        

2． 读取用户信息：
<p>
其实弄明白了保存原理，读取及校验原理就很容易做了。读取和检验可以分为下面几个步骤：

① 根据设置的cookieName，得到cookieValue，如果值为空，就不帮用户进行自动登陆；否则执行读取方法

② 将cookieValue进行Base64解码，将取得的字符串以split(“:”)进行拆分，得到一个String数组cookieValues（此操作与保存阶段的第4步正好相反），这一步将得到三个值：

       cookieValues[0] ---- 用户名

       cookieValues[1] ---- cookie有效时间

       cookieValues[2] ---- MD5明文字符串

③ 判断cookieValues的长度是否为3，如果不为3则进行错误处理。

④ 如果长度等于3，取出第二个,即cookieValues[1]，此时将会得到有效时间（long型），将有效时间与服务器系统当前时间比较，如果小于当前时间，则说明cookie过期，进行错误处理。

⑤ 如果cookie没有过期，就取cookieValues[0]，这样就可以得到用户名了，然后去数据库按用户名查找用户。

⑥ 如果上一步返回为空，进行错误处理。如果不为空，那么将会得到一个已经封装好用户信息的User实例对象user

⑦ 取出实例对象user的用户名、密码、cookie有效时间（即cookieValues[1]）、webKey，然后将四个值连接起来，然后进行MD5加密，这样做也会得到一个MD5明文字符串（此操作与保存阶段的第3步类似）

⑧ 将上一步得到MD5明文与cookieValues[2]进行equals比较，如果是false，进行错误处理；如果是true，则将user对象添加到session中，帮助用户完成自动登陆
</p>
