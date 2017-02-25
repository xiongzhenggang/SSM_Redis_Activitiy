package com.xzg.domain;  

import java.io.Serializable;

/** 
 
 */  
public class User implements Serializable{  
    /**
	 * 
	 */
	private static final long serialVersionUID = 7294854952802230207L;
	private Long userId;  
    private String userName;  
    private String password;  
    private String tel; 
    private String email;  
    private String address;  
	private String roleId;  
    private String roleName;  
    private String sex;  
    private String deptId;  
    private String deptName;  
    private String puserId; 
    private String puserName; 
    
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPuserId() {
		return puserId;
	}

	public void setPuserId(String puserId) {
		this.puserId = puserId;
	}

	public String getPuserName() {
		return puserName;
	}

	public void setPuserName(String puserName) {
		this.puserName = puserName;
	}

    public Long getUserId() {  
        return userId;  
    }  
  
    public void setUserId(long userId) {  
        this.userId = userId;  
    }  
  
    public String getUserName() {  
        return userName;  
    }  
  
    public void setUserName(String userName) {  
        this.userName = userName;  
    }  
  
    @Override  
    public String toString() {  
        return "User [userId=" + userId + ", userName=" + userName  
                + ", userPassword=" + password + ", userEmail=" + email  
                + "]";  
    }  
      
}  