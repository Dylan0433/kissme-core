package com.kissme.core.domain.monitor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author loudyn
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Monitoring {

	/**
	 * 
	 * @return
	 */
	String action();

	/**
	 * 
	 * @return
	 */
	Class<? extends Exception> except() default Exception.class;
}
