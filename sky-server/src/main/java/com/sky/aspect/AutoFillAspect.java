package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 自定义切面类，统一实现填充
 */
@Aspect//表示这是切面类
@Component//交给spring管理
@Slf4j//
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")//定义一个切入点函数，
    //表示在com.sky.mapper包下的所有方法，而且这些方法上必须加了@AutoFill注解
    public void autoFillPointcut(){

    }

    /**
     * 在切入点方法执行之前执行,在通知中进行字符串的填充
     */
    @Before("autoFillPointcut()")
    public void autoFillPointcut2(org.aspectj.lang.JoinPoint joinPoint){//参数用来拿到被拦截的方法的详细信息
        log.info("开始进行字符串的填充了喵");
        //首先知道拦截的到底是什么类型的数据操作
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取注解对象
        OperationType operationType = autoFill.value();
        //获取被拦截的方法参数
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            log.info("没有参数");
            return;
        }
        Object entity=args[0];
        //赋值参数
        LocalTime now = LocalTime.now();
        Long currentId= BaseContext.getCurrentId();
        //根据上面的参数为语句赋值
        if(operationType== OperationType.INSERT){
            log.info("进行插入操作");
            try{
                Method setCreateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                //调用
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else if(operationType== OperationType.UPDATE){
            log.info("进行更新操作");
            try{
                Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
                Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
