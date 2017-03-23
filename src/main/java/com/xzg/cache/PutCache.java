/**
 * 
 */
package com.xzg.cache;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
/** 
 * 自定义注解,对于查询使用缓存的方法加入该注解 
 * @author Chenth 
 */  
@Retention(RetentionPolicy.RUNTIME)   // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.METHOD})  
public @interface PutCache {  
    String name() default "";  
    String value() default "";  
}