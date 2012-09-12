package com.kissme.core.domain.event;

import com.google.common.eventbus.EventBus;

/**
 * 
 * @author loudyn
 * 
 */
public final class LifecycleEvents {

	private final static LifecycleEvents ME = new LifecycleEvents();
	private EventBus delegate;

	private LifecycleEvents() {
		this.delegate = new EventBus("lifecycle.eventbus");
	}

	/**
	 * 
	 * @return
	 */
	public static LifecycleEvents me() {
		return ME;
	}

	/**
	 * 
	 * @param monitor
	 * @return
	 */
	public LifecycleEvents register(LifecycleEventHandler monitor) {
		this.delegate.register(monitor);
		return this;
	}

	/**
	 * 
	 * @param monitor
	 * @return
	 */
	public LifecycleEvents unregister(LifecycleEventHandler monitor) {
		this.delegate.unregister(monitor);
		return this;
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public LifecycleEvents oneMore(LifecycleEvent event) {
		this.delegate.post(event);
		return this;
	}
}
