/**
 * 
 */
package com.xzg.controller;

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
@RequestMapping("room")
public class RoomController {

    @Autowired
    private RoomService  roomService;
    
    @GetCache(name="room",value="id")
    @RequestMapping("selectByPrimaryKey")
    public @ResponseBody Object roomList(Integer id) throws Exception{  
        System.out.println("已查询到数据，准备缓存到redis...  "+roomService.selectByPrimaryKey(id));
        return roomService.selectByPrimaryKey(id);
    }
    

}
