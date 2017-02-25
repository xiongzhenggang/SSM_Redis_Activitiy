/**
 * 
 */
package com.xzg.domain;

import java.io.Serializable;

/**
 * @author hasee
 * @TIME 2016年12月26日
 * 注意类的隐藏和实例创建
 */
public class Group implements Serializable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 5051089085449058827L;
	/**
	 * 
	 */
	private   String roleId ;
	    private  String roleInfo  ;
	    private  String roleName	;
	    private  String proleId;
	    private String proleName	;
		public String getRoleId() {
			return roleId;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			
			return getRoleId()+":"+getRoleName();
		}
		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}
		public String getRoleInfo() {
			return roleInfo;
		}
		public void setRoleInfo(String roleInfo) {
			this.roleInfo = roleInfo;
		}
		public String getRoleName() {
			return roleName;
		}
		public void setRoleName(String roleName) {
			this.roleName = roleName;
		}
		public String getProleId() {
			return proleId;
		}
		public void setProleId(String proleId) {
			this.proleId = proleId;
		}
		public String getProleName() {
			return proleName;
		}
		public void setProleName(String proleName) {
			this.proleName = proleName;
		}
}
