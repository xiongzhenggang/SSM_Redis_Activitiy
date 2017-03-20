/**
 * 
 */
package com.xzg.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lin.service.RoomService;
import com.xzg.cache.GetCache;

/**
 * 
 * @description test controller
 */

@Controller
@RequestMapping("user")
public class RoomController {
    @Resource(name="")
    private RoomService  roomService;
    
    @GetCache(name="user",value="id")
    @RequestMapping("selectByPrimaryKey.do")
    public @ResponseBody Object roomList(Integer id) throws Exception{  
        System.out.println("已查询到数据，准备缓存到redis...  "+roomService.selectByPrimaryKey(id));
        return roomService.selectByPrimaryKey(id);
    }
    

}
