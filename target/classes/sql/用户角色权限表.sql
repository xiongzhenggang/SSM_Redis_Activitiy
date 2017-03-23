--用户-角色-权限-权限分栏（控制系统运行时左侧菜单中的权限分栏，也就是“权限分栏表”）
drop table actioncolumn,action,action_role,role,user_role,user,dept,master;

--新增树形结构展示
CREATE TABLE menue_tree
(
	ID   		            	VARCHAR(20),
	PID				            VARCHAR(20),
	name				     	VARCHAR(50),
	isleaf			  			char(1),
	url              			VARCHAR(120),
	target 			  			VARCHAR(20),
	open 			  			VARCHAR(5),
	PRIMARY KEY(ID)

)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--权限表
CREATE TABLE authorty 
(
    actionId                        VARCHAR(20) NOT NULL,
    actionName                	 	VARCHAR(50),
    url 						VARCHAR(120),
    PRIMARY KEY(actionId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

--权限-角色表
CREATE TABLE authorty_role
( 
    roleId		   			 		VARCHAR(20) not NULL,
    roleName		   	  			VARCHAR(50),
    actionId               	 		VARCHAR(60) not NULL,
  	actionName		 	   			VARCHAR(50),
    PRIMARY KEY(roleId,actionId)   
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;



CREATE TABLE role --角色表
(
    roleId                        	VARCHAR(20) NOT NULL,
    roleInfo               	 		VARCHAR(50),
    roleName			  			VARCHAR(50),
    proleId				 		 	VARCHAR(20),--父角色
    proleName			 			VARCHAR(50), 
    PRIMARY KEY(roleId) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE user_role --用户-角色
(
	ID                        		INT NOT NULL AUTO_INCREMENT,
	userId					 		int NOT NULL,
	userName				 		VARCHAR(50),
    roleId                        	VARCHAR(20)	not NULL,
    roleInfo               	   		VARCHAR(50),
    roleName			 	 		VARCHAR(50),
    PRIMARY KEY(ID) 
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;

CREATE TABLE user --用户表
(
	userId   			 			INT NOT NULL AUTO_INCREMENT,
	userName					 	VARCHAR(50),
	password					  	VARCHAR(20),
	tel							 	VARCHAR(20),
	email						 	VARCHAR(20),
	address						 	VARCHAR(100),
	roleId 						 	VARCHAR(20),
    roleName			 		 	VARCHAR(50),
    sex						 	 	char(2),
    deptId						 	VARCHAR(20),
    deptName					 	VARCHAR(50),
    puserId						 	VARCHAR(20),--上级用户
    puserName					 	VARCHAR(50),	
    PRIMARY KEY(userId) 
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;


--user修改与act_id_user

CREATE TABLE dept --部门表
(
	deptId                        	VARCHAR(20) NOT NULL,
	deptName		 				VARCHAR(50),
	managerId		 				VARCHAR(20),--部们老大id
	managerName		 				VARCHAR(50),
	deptoInfo		 				VARCHAR(50),
    tel				 		 		VARCHAR(20),--座机
    PRIMARY KEY(deptId) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE master --管理员记录表
(
	id                        		INT NOT NULL AUTO_INCREMENT,
	masterName			 			VARCHAR(50),
	password			 			VARCHAR(20),
	createDate			 			date,
	userId 				 			VARCHAR(20),
    userName			  			VARCHAR(50),
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;

CREATE TABLE leave_list --请假状态表
(
    LeaveId                         INT NOT NULL AUTO_INCREMENT,
    userId                          int,
    userName                        VARCHAR(20),
    roleid                          VARCHAR(20),
    roleName                        VARCHAR(50),
    reason                          VARCHAR(50),
    leaveDay                        date,
    howlong                         int,    
    leaveState                      char(2),
    PRIMARY KEY(LeaveId)
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;

CREATE TABLE leave_Flow --请假流程记录表
(
    id                              INT NOT NULL AUTO_INCREMENT,
    LeaveId                         int,
    leaveState                      char(2),
    nextdealUser                    VARCHAR(50),
    dealUser                        VARCHAR(20),
    opinion                         char(2),
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;

--角色表插入内容
INSERT INTO role(roleId, roleName) VALUES ('T_EMP', '普通员工'); 
INSERT INTO role(roleId, roleName) VALUES ('T_MAN', '经理');  
INSERT INTO role(roleId, roleName) VALUES ('T_BOSS', '老板');

INSERT INTO role(roleId, roleName) VALUES ('admin', '管理员'); 
INSERT INTO role(roleId, roleName) VALUES ('deptLeader', '部门领导');  
INSERT INTO role(roleId, roleName) VALUES ('hr', '人事');
INSERT INTO role(roleId, roleName) VALUES ('user', '用户');
INSERT INTO role(roleId, roleName) VALUES ('kafeitu', 'kafeitu');
--用户表插入内容
INSERT INTO user(userName, password) VALUES ('张三', '000000');
INSERT INTO user(userName, password) VALUES ('李四', '000000'); 
INSERT INTO user(userName, password) VALUES ('王五', '000000');
INSERT INTO user(userName, password) VALUES ('陈六', '000000'); 
INSERT INTO user(userName, password) VALUES ('蔡七', '000000');
----
INSERT INTO user(userId, password) VALUES ('admin', '000000');
INSERT INTO user(userId, password) VALUES ('hruser', '000000'); 
INSERT INTO user(userId, password) VALUES ('kafeitu', '000000');
INSERT INTO user(userId, password) VALUES ('leaderuser', '000000');
INSERT INTO user(userName, password) VALUES ('admin', '000000');
INSERT INTO user(userName, password) VALUES ('hruser', '000000'); 
INSERT INTO user(userName, password) VALUES ('kafeitu', '000000');
INSERT INTO user(userName, password) VALUES ('leaderuser', '000000');

--配置角色用户表
INSERT INTO user_role(userId,userName,roleId,roleName) VALUES ('10000','张三', 'T_EMP','普通员工');
INSERT INTO user_role(userId,userName,roleId,roleName) VALUES ('10001','李四', 'T_MAN','经理');
INSERT INTO user_role(userId,userName,roleId,roleName) VALUES ('10002','王五', 'T_BOSS','老板');
INSERT INTO user_role(userId,userName,roleId,roleName) VALUES ('10003','陈六', 'T_EMP','普通员工');
------
INSERT INTO user_role(userId,userName,roleId) VALUES (10005,'admin', 'admin');
INSERT INTO user_role(userId,userName,roleId) VALUES (10007,'kafeitu', 'admin');
INSERT INTO user_role(userId,userName,roleId) VALUES (10008,'leaderuser', 'deptLeader');
INSERT INTO user_role(userId,userName,roleId) VALUES (10006,'hruser', 'hr');
INSERT INTO user_role(userId,userName,roleId) VALUES (10005,'admin', 'user');
INSERT INTO user_role(userId,userName,roleId) VALUES (10006,'hruser', 'user');
INSERT INTO user_role(userId,userName,roleId) VALUES (10008,'leaderuser', 'user');

--修改表为utf-8
alter table role modify category default charset = utf8;

--增加请假数据 请假状态 '00'：初始录入 '01':审批中 '02'：审批通过 '03'：作废 '04':退回重写
--对应请假开始-提交-同意/拒绝/退回重写
INSERT INTO leave_list(userId,userName,roleId,roleName,leaveDay,leaveState) VALUES ('10003','陈六', 'T_EMP','普通员工','2016-12-12','00');
--满足activit学习中的表明兴建leave表
CREATE TABLE LEAVE_F
(
    ID                               int NOT NULL AUTO_INCREMENT,
    PROCESS_INSTANCE_ID              VARCHAR(20),
    USER_ID                          VARCHAR(20),
    START_TIME                       date,
    END_TIME                         date,
    APPLY_TIME                       date,
    REALITY_START_TIME               date,
    REALITY_END_TIME                 date,
    REASON                           VARCHAR(50),
    LEAVE_TYPE                       char(2),
    PRIMARY KEY(ID)
)ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8;
--登录锁定限定表
CREATE TABLE login_tmp
(
	USER_ID   		            	int NOT NULL,
	NUM				                int,
	CURRENT				            date,
	PRIMARY KEY(USER_ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
--新增用户状态字段,0正常、1锁定、2注销
alter table  user add  user_state char(1) not null default '0'; 
---------------------------------------------------------------------------
insert into menue_tree(Id,name,isleaf,target,open) VALUES('m','菜单功能栏','0','main','true');
insert into menue_tree(Id,PID,name,isleaf,target,open) VALUES('m01','m','系统管理','0','main','true');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m0101','m01','权限管理','1','/ssm_project/authority.do','main');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m0102','m01','用户管理','1','/ssm_project/userlist.do','main');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m0103','m01','角色管理','1','/ssm_project/grouplist.do','main');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m02','m','上传流程定义','1','/ssm_project/workflow/toupload.do','main');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m03','m','查看流程定义','1','/ssm_project/workflow/processlist.do','main');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m04','m','填写请假单','1','/ssm_project/leave/form.do','main');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m05','m','待办任务列表','1','/ssm_project/leave/task/list.do','main');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m06','m','运行中的流程实例','1','/ssm_project/leave/process/running/leave/list.do','main');
insert into menue_tree(Id,PID,name,isleaf,url,target) VALUES('m07','m','已经结束的流程实例','1','/ssm_project/leave/process/finished/leave/list.do','main'); 
-----------------------------------------------------------------------------------
insert into authorty_role(roleId,actionId) VALUES('admin','m');
insert into authorty_role(roleId,actionId) VALUES('admin','m01');
insert into authorty_role(roleId,actionId) VALUES('admin','m0101');
insert into authorty_role(roleId,actionId) VALUES('admin','m0102');
insert into authorty_role(roleId,actionId) VALUES('admin','m0103');
insert into authorty_role(roleId,actionId) VALUES('admin','m02');
insert into authorty_role(roleId,actionId) VALUES('admin','m03');
insert into authorty_role(roleId,actionId) VALUES('admin','m04');
insert into authorty_role(roleId,actionId) VALUES('admin','m05');
insert into authorty_role(roleId,actionId) VALUES('admin','m06');
insert into authorty_role(roleId,actionId) VALUES('admin','m07');
insert into authorty_role(roleId,actionId) VALUES('hr','m');
insert into authorty_role(roleId,actionId) VALUES('hr','m02');
insert into authorty_role(roleId,actionId) VALUES('hr','m03');
insert into authorty_role(roleId,actionId) VALUES('hr','m04');
insert into authorty_role(roleId,actionId) VALUES('hr','m05');
insert into authorty_role(roleId,actionId) VALUES('hr','m06');
insert into authorty_role(roleId,actionId) VALUES('hr','m07');
insert into authorty_role(roleId,actionId) VALUES('user','m');
insert into authorty_role(roleId,actionId) VALUES('user','m02');
insert into authorty_role(roleId,actionId) VALUES('user','m03');
insert into authorty_role(roleId,actionId) VALUES('user','m04');
insert into authorty_role(roleId,actionId) VALUES('user','m05');
insert into authorty_role(roleId,actionId) VALUES('user','m06');
insert into authorty_role(roleId,actionId) VALUES('user','m07');
-----------------------------------------------------------
--由用户查询树形权限
select * from menue_tree where id in(select distinct actionid from authorty_role where roleid in
 (select roleid from user_role where userid='10008'));
---------------------------------------------------------------------------

insert into authorty(actionId,actionName,url) VALUES('m01','权限管理','/ssm_project/authority.do');