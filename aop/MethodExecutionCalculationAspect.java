package com.se2.alert.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class MethodExecutionCalculationAspect {

	private final static Logger logger = LoggerFactory.getLogger(MethodExecutionCalculationAspect.class);

	@Around("com.se2.alert.aop.CommonJoinPointConfig.controllerMethods()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();

		Object output = joinPoint.proceed();

		long timeTaken = System.currentTimeMillis() - startTime;
		logger.info("Time Taken by {} is {} milliseconds", joinPoint, timeTaken);
		return output;
	}

	@Before("com.se2.alert.aop.CommonJoinPointConfig.controllerMethods()")
	public void before(JoinPoint joinPoint) {
		logger.info("{} called with parameters: ", joinPoint);
		Object[] args = joinPoint.getArgs();
		if (args != null && args.length > 0)
			for (Object arg : args)
				if (arg != null)
					logger.debug(arg.toString());
	}
}
