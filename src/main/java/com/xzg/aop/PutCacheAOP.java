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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.xzg.cache.GetCache;
import com.xzg.cache.RedisCache;
import com.xzg.publicUtil.CookieUtil;
/**@AspectJ风格的切面可以通过@Compenent注解标识其为Spring管理Bean，
而@Aspect注解不能被Spring自动识别并注册为Bean，必须通过@Component注解来完成
*/
@Component
@Aspect//声明该类使用了aop
public class PutCacheAOP  {  
    @Resource(name="redisTemplate")
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Resource(name="redisCache")
    private RedisCache redisCache;
    @Pointcut("@annotation(com.xzg.cache.PutCache)")  
    public void putCache(){
        System.out.println("我是一个切入点");  
    }  
    /** 
     * 在所有标注@putCache的地方切入 
     * @param joinPoint 
     */
    @Around("putCache()")
    public Object beforeExec(ProceedingJoinPoint joinPoint){  
        //前置：到redis中查询缓存
        System.out.println("调用从redis中查询的方法...");
        //redis中key格式：    id
        String redisKey = getCacheKey(joinPoint);
        Object object = null;
        try {
        	  //删除缓存内容
            redisCache.removeDataToRedis(redisKey);
            //执行aop切面之后的方法
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
    /*public void setRedisTemplate( 
            RedisTemplate<Serializable, Object> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }*/
}