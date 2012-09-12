package com.kissme.core.domain.event;

import java.util.EventObject;

/**
 * 
 * @author loudyn
 * 
 */
public class LifecycleEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	private final long timestamp;
	private final LifecycleEventType type;

	/**
	 * 
	 * @param source
	 * @param timestamp
	 */
	public LifecycleEvent(Object source, long timestamp) {
		this(source, LifecycleEventType.CREATE, timestamp);
	}

	/**
	 * 
	 * @param source
	 * @param type
	 * @param timestamp
	 */
	public LifecycleEvent(Object source, LifecycleEventType type, long timestamp) {
		super(source);
		this.type = type;
		this.timestamp = timestamp;
	}

	/**
	 * 
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * 
	 * @return
	 */
	public LifecycleEventType getType() {
		return type;
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	public static enum LifecycleEventType {
		CREATE, MODIFY, DELETE
	}
}
