/**
 * 
 */
package com.xzg.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @author hasee
 * @TIME 2017年2月21日
 * 注意类的隐藏和实例创建
 */
public class SessionListener  implements HttpSessionBindingListener{
	private ServletContext application;
	
	public SessionListener(ServletContext application) {
		// TODO Auto-generated constructor stub
		this.application = application;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		System.out.println("======================检测到绑定用户了！=====================");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
	 */
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		System.out.println("======================检测到解除绑定了！=====================");
		//超时移除全局sessionId
		if(application != null&&application.getAttribute("userid") != null){
			application.removeAttribute("userid");
		}
	}

}
