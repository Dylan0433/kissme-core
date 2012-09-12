package com.kissme.core.domain.monitor;

import java.util.HashMap;
import java.util.Map;

import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public class MonitoringContext {

	public final static String ACTOR_ATTR = "monitoring.actor";
	public final static String SOURCE_ATTR = "monitoring.source";
	public final static String TARGET_ATTR = "monitoring.target";
	public final static String ACTION_ATTR = "monitoring.action";

	private final static ThreadLocal<MonitoringContext> LOCAL_CONTEXTES = new ThreadLocal<MonitoringContext>() {

		@Override
		protected MonitoringContext initialValue() {
			return new MonitoringContext(new HashMap<String, Object>());
		}
	};

	private final Map<String, Object> delegate;
	private final long createTime;

	public MonitoringContext(final Map<String, Object> delegate) {

		Preconditions.notNull(delegate);
		this.delegate = delegate;
		this.createTime = System.currentTimeMillis();
	}

	/**
	 * 
	 * @return
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * 
	 * @return
	 */
	public static MonitoringContext get() {
		return LOCAL_CONTEXTES.get();
	}

	/**
	 * 
	 * @param context
	 */
	public static void set(MonitoringContext context) {
		LOCAL_CONTEXTES.set(context);
	}

	/**
	 * 
	 * @param attr
	 * @return
	 */
	public Object get(String attr) {
		return delegate.get(attr);
	}

	/**
	 * 
	 * @param attr
	 * @param value
	 * @return
	 */
	public MonitoringContext set(String attr, Object value) {
		delegate.put(attr, value);
		return this;
	}

	/**
	 * 
	 * @return
	 */
	public String getActor() {
		return (String) get(ACTOR_ATTR);
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public MonitoringContext setActor(String username) {
		return set(ACTOR_ATTR, username);
	}

	/**
	 * 
	 * @return
	 */
	public String getAction() {
		return (String) get(ACTION_ATTR);
	}

	/**
	 * 
	 * @param action
	 * @return
	 */
	public MonitoringContext setAction(String action) {
		return set(ACTION_ATTR, action);
	}

	/**
	 * 
	 * @return
	 */
	public String getSource() {
		return (String) get(SOURCE_ATTR);
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public MonitoringContext setSource(String source) {
		return set(SOURCE_ATTR, source);
	}

	/**
	 * 
	 * @return
	 */
	public String getTarget() {
		return (String) get(TARGET_ATTR);
	}

	/**
	 * 
	 * @param action
	 * @return
	 */
	public MonitoringContext setTarget(String action) {
		return set(TARGET_ATTR, action);
	}
}
