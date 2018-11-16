package com.github.dapeng.dms.web.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
 * @since 2018-11-16 1:49 PM
 */
@Aspect
@Slf4j
@Component
public class LogAspect {

    //    @Pointcut("@annotation(com.space.aspect.anno.SysLog)")
    @Pointcut("execution(* com.github.dapeng.dms.web.controller.*.*(..))")
    public void logPointCut() {
    }

    @Around(value = "logPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestUrl = request.getRequestURI();
        Signature signature = pjp.getSignature();
        String name = signature.getName();
        String className = signature.getDeclaringType().getSimpleName();
        log.info("=== 请求url:[{}], 请求方法name:[{}], 请求方法所属类Class:[{}] ===", requestUrl, name, className);
        return pjp.proceed();
    }


}
