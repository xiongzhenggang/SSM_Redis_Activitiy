/**
 * 
 */
package com.xzg.cache;

import javax.annotation.Resource;

import com.xzg.publicUtil.SerializeUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author hasee
 * @TIME 2017年3月20日
 * 注意类的隐藏和实例创建
 */
public class RedisCache {
    
    @Resource(name="jedisPool")
    private JedisPool jedisPool;
    //从redis缓存中查询，反序列化
    public Object getDataFromRedis(String redisKey){
        //查询
        Jedis jedis = jedisPool.getResource();
        byte[] result = jedis.get(redisKey.getBytes());
        
        //如果查询没有为空
        if(null == result){
            return null;
        }
        //查询到了，反序列化
        return SerializeUtil.unSerialize(result);
    }
    //将数据库中查询到的数据放入redis
    public void setDataToRedis(String redisKey, Object obj){
        //序列化
        byte[] bytes = SerializeUtil.serialize(obj);
        //存入redis
        Jedis jedis = jedisPool.getResource();
        //String success = jedis.set(redisKey.getBytes(), bytes);
       String success = jedis.setex(redisKey.getBytes(), 60, bytes);//设置缓存失效时间为60秒
        if("OK".equals(success)){
            System.out.println("数据成功保存到redis...");
        }
    }
    //删除redis中指定的key值
    public void removeDataToRedis(String redisKey){
    	try{
    		Jedis jedis = jedisPool.getResource();
    		jedis.del(redisKey.getBytes());
    	}catch(JedisConnectionException e){//无法获取连接
    		e.printStackTrace();
    	}
    }
}