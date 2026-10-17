package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于判断哪些需要给切口拦截用于自动填充
 */
@Target(ElementType.METHOD)//这个标识表示注解会加在哪个位置,同时说明只能加在方法上
@Retention(RetentionPolicy.RUNTIME)//此注解生命周期是运行时
public @interface AutoFill {
    //数据库操作类型，update或者insert
    OperationType value();

}
