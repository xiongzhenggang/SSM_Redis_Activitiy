/**
 * 
 */
package com.xzg.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.xzg.domain.User;

/**
 * 
 * @author     cmy
 * @description ³Ö¾Ã»¯
 */

public interface RoomMapper {

    @Insert("insert into user(userId,userNsme) values(#{userId},#{userNsme})")
    int insert(User user);

    @Select("select * from user where userId=#{userId}")
    public User selectByPrimaryKey(@Param("userId")Integer userId);

}
