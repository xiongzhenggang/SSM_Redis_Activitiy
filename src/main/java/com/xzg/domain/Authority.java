/**
 * 
 */
package com.xzg.domain;

import java.io.Serializable;

/**
 * @author hasee
 * @TIME 2017年3月2日
 * 注意类的隐藏和实例创建
 */
public class Authority  implements Serializable{
	private static final long serialVersionUID = 1L;
	private String actionId;
	private String url;
	private String actionName;
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
}
