package com.se2.alert.aop;

import org.aspectj.lang.annotation.Pointcut;

public class CommonJoinPointConfig {

	@Pointcut("execution(* com.se2.alert.controller.*.*(..))")
	public void controllerMethods() {
		throw new UnsupportedOperationException();
	}
}