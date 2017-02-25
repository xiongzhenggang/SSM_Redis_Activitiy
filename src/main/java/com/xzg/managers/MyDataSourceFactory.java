/**
 * 
 */
package com.xzg.managers;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * @author hasee
 * @TIME 2016年12月29日
 * 注意类的隐藏和实例创建
 */
public class MyDataSourceFactory  extends UnpooledDataSourceFactory {

	 public MyDataSourceFactory() {   
		// this.dataSource = new DataSourceFactory().createDataSource(null);
		 } 
}
