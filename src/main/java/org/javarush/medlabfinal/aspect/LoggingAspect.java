package org.javarush.medlabfinal.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* org.javarush.medlabfinal.service..*(..))")
    public void logBeforeService(JoinPoint joinPoint) {
        logger.info("Виклик методу: {}", joinPoint.getSignature().getName());
    }
}
