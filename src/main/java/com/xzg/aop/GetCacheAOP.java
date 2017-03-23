/**
 * 
 */
package com.xzg.aop;

import java.io.Serializable;
import java.lang.reflect.Method;






import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.xzg.cache.GetCache;
import com.xzg.cache.RedisCache;
import com.xzg.controller.RoomController;
import com.xzg.publicUtil.CookieUtil;
/**@AspectJ风格的切面可以通过@Compenent注解标识其为Spring管理Bean，
而@Aspect注解不能被Spring自动识别并注册为Bean，必须通过@Component注解来完成
*/
@Component
@Aspect//声明该类使用了aop
public class GetCacheAOP  {  
    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Resource(name="redisCache")
    private RedisCache redisCache;
    @Pointcut("@annotation(com.xzg.cache.GetCache)")  
    public void getCache(){
        System.out.println("我是一个切入点");  
    }  
    /** 
     * 在所有标注@getCache的地方切入 
     * @param joinPoint 
     */
    @Around("getCache()")
    public Object beforeExec(ProceedingJoinPoint joinPoint){  
        //前置：到redis中查询缓存
        System.out.println("调用从redis中查询的方法...");
        
        //redis中key格式：    id
        String redisKey = getCacheKey(joinPoint);
        //获取从redis中查询到的对象
        Object objectFromRedis = redisCache.getDataFromRedis(redisKey);
        
        //如果查询到了
        if(null != objectFromRedis){
            System.out.println("从redis中查询到了数据...不需要查询数据库");
            return objectFromRedis;
        }
        System.out.println("没有从redis中查到数据...");
        //没有查到，那么查询数据库
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            
            e.printStackTrace();
        }
        System.out.println("从数据库中查询的数据...");
        //后置：将数据库中查询的数据放到redis中
        System.out.println("调用把数据库查询的数据存储到redis中的方法...");
        redisCache.setDataToRedis(redisKey, object);
        System.out.println("redis中的数据..."+object.toString());
        //将查询到的数据返回
        return object;
    }
    /**
     * 根据类名、方法名和参数值获取唯一的缓存键
     * @return 格式为 "包名.类名.方法名.参数类型.参数值"，类似 "your.package.SomeService.getById(int).123"
     */
    private String getCacheKey(ProceedingJoinPoint joinPoint) {
        MethodSignature ms=(MethodSignature) joinPoint.getSignature();  
        Method method=ms.getMethod();  
        String ActionName = method.getAnnotation(GetCache.class).name();  
        String fieldList = method.getAnnotation(GetCache.class).value();  
        //System.out.println("签名是"+ms.toString());
        for (String field:fieldList.split(",")) //field表示输入的参数值可以有多个
             ActionName +="."+field;
        //先获取目标方法参数
        String id = null;
        Object[] args = joinPoint.getArgs();//[10005]
        if (args != null && args.length > 0) {
            id = String.valueOf(args[0]);
        }
        ActionName += "="+id;
        String redisKey = CookieUtil.getMD5(ms+"."+ActionName);//使用MD5加密后的作为换粗字
        System.out.println("当前方法的key值=="+redisKey);//Object com.xzg.controller.RoomController.roomList(Integer).user.id=10005
        return redisKey;
    }
    public void setRedisTemplate(  
            RedisTemplate<Serializable, Object> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }
    //
    public static void main(String[] args){
    	Class<RoomController> clz = RoomController.class;
    	Method[] mt = clz.getMethods();
    	System.out.println(mt[0]);
    	  String ActionName	= mt[0].getAnnotation(GetCache.class).value();  
    	System.out.println(ActionName);
    	
    }
}