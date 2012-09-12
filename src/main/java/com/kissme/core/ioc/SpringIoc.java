package com.kissme.core.ioc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public final class SpringIoc implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context. ApplicationContext)
	 */
	@Override
	public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringIoc.awareApplicationContext(applicationContext);
	}

	private static void awareApplicationContext(ApplicationContext applicationContext) {
		Preconditions.notNull(applicationContext);
		SpringIoc.applicationContext = applicationContext;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
}
