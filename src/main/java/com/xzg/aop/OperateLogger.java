/**
 * 
 */
package com.xzg.aop;

/**
 * @author hasee
 * @TIME 2017年1月5日
 * 注意类的隐藏和实例创建
 */
public class OperateLogger {

	//前置通知，工具类做为前置通知
	public void log1(){
		System.out.println("-->记录用户操作");
	}
	//
}
