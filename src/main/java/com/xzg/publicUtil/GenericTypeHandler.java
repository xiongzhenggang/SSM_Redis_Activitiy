/**
 * 
 */
package com.xzg.publicUtil;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

/**
 * @author hasee
 * @TIME 2017年1月1日
 * 注意类的隐藏和实例创建
 */
@SuppressWarnings("unused")
@MappedJdbcTypes(JdbcType.VARCHAR) 
public class GenericTypeHandler<E extends String> extends BaseTypeHandler<E> {
	private Class<E> type;
	  public GenericTypeHandler(Class<E> type)
	 {   
		  if (type == null) throw new IllegalArgumentException("Type argument cannot be null");   
		  this.type = type; 
}
	/* (non-Javadoc)
	 * @see org.apache.ibatis.type.BaseTypeHandler#setNonNullParameter(java.sql.PreparedStatement, int, java.lang.Object, org.apache.ibatis.type.JdbcType)
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, E parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, java.lang.String)
	 */
	@Override
	public E getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.ResultSet, int)
	 */
	@Override
	public E getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.apache.ibatis.type.BaseTypeHandler#getNullableResult(java.sql.CallableStatement, int)
	 */
	@Override
	public E getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	  }
