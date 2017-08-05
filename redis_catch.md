## redis缓存策略
* 每次查询接口的时候首先判断 Redis 中是否有缓存，有的话就读取，没有就查询数据库并保存到 Redis 中，下次再查询的话就会直接从缓存中读取了。
首先是缓存的策略：
1. 	热点数据缓存才有意义。而冷数据可以考虑直接存储到db中或硬盘中。
2.	数据修改的频繁，视情况使用缓存。数据更新前至少读取两次，缓存才有意义。这个是最基本的策略，如果缓存还没有起作用就失效了，那就没有太大价值了。通过同步保存到Redis缓存，减少数据库压力。一般适用于某个产品的浏览数。文章的点赞数等
3.	一般会对缓存设置失效时间，一旦超过失效时间，就要从数据库重新加载，因此应用要容忍一定时间的数据不一致。还有一种是在数据更新时立即更新缓存，不过这也会更多系统开销和事务一致性问题。
4.	遇到缓存数据的不一致性和与脏读现象. 一般情况下，我们采取缓存双淘汰机制，在更新数据库的时候淘汰缓存。此外，设定超时时间，例如30分钟。极限场景下，即使有脏数据入cache，这个脏数据也最多存在三十分钟。
5.	缓存是提高数据读取性能的，缓存数据丢失和缓存不可用不会影响应用程序的处理。因此，一般的操作手段是，如果Redis出现异常，我们手动捕获这个异常，记录日志，并且去数据库查询数据返回给用户。

### 至于具体实现手段也有很多，这里只简单使用自定义aop注解为需要的数据操作进行缓存处理。
以下相关代码，环境为springmvc+mybits
1.	配置redis相关的连接池、服务中兴、模板等。配置aop动态代理，开启注解。
```xml
<!-- 开启注解这个一定要写到springmvc.xml里，否则注解会不起作用 -->
     <aop:aspectj-autoproxy proxy-target-class="true"/>
```
2.	自定义注解
```java
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.METHOD}) 
@Inherited
public @interface GetCache {  
String name() default "";  	
String value() default "";  }
```
3. 注解的实现方式，包含redis的缓存操作，具体参考ssm项目中的。。
```java
@Pointcut("@annotation(com.xzg.cache.GetCache)")  
```
4. 在相关的方法上添加自定义注解，即可实现。
```java
@GetCache(name="user",value="userId")
    @RequestMapping(value="selectByPrimaryKey.do",method = RequestMethod.GET)
    public @ResponseBody Object roomList(@RequestParam("userId") Integer userId) throws Exception{  
        System.out.println("已查询到数据，准备缓存到redis...  "+roomService.selectByPrimaryKey(userId).getUserId());
        return roomService.selectByPrimaryKey(10005);
    }
```
