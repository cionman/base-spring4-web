package com.base.common.aop;

import com.base.common.util.LogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MethodLoggingAspect {

    @Autowired
    LogUtil logUtil;

    @Value("${project.name}")
    String projectName;

    @Value("${project.version}")
    String projectVersion;

    @Around("execution(public * com.base..*.*(..))")
    public Object errorLog(ProceedingJoinPoint jp) throws Throwable{
        try{
            return jp.proceed();
        }catch (Exception e){
            logUtil.error(projectName, e);
            e.printStackTrace();
            throw e;
        }
    }

}
