/**
 * 
 */
package com.xzg.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Resource(name="roomServiceImple")
    private RoomService  roomService;
    
    @GetCache(name="user",value="id")
    @RequestMapping("selectByPrimaryKey.do")
    public @ResponseBody Object roomList(@RequestParam("userId")Integer userId) throws Exception{  
        System.out.println("已查询到数据，准备缓存到redis...  "+roomService.selectByPrimaryKey(userId));
        return roomService.selectByPrimaryKey(userId);
    }
    

}
