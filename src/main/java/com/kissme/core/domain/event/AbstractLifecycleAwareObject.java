package com.kissme.core.domain.event;

import com.kissme.core.domain.AbstractDomain;

import static com.kissme.core.domain.event.LifecycleEvent.LifecycleEventType.*;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class AbstractLifecycleAwareObject<T> extends AbstractDomain implements LifecycleAware<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @return
	 */
	public String identity() {
		return getId();
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	public AbstractLifecycleAwareObject<T> identity(String id) {
		setId(id);
		return this;
	}

	@Override
	public final T create() {
		try {

			T caller = getCaller();
			if (beforeCreate()) {
				LifecycleEvents.me().oneMore(new LifecycleEvent(caller, CREATE, createTimestamp()));
			}

			return caller;
		} finally {
			afterCreate();
		}
	}

	/**
	 * 
	 * @return
	 */
	protected boolean beforeCreate() {
		return true;
	}

	protected void afterCreate() {}

	@Override
	public final T modify() {
		try {

			T caller = getCaller();
			if (beforeModify()) {
				LifecycleEvents.me().oneMore(new LifecycleEvent(caller, MODIFY, createTimestamp()));
			}

			return caller;
		} finally {
			afterModify();
		}
	}

	/**
	 * 
	 * @return
	 */
	protected boolean beforeModify() {
		return true;
	}

	protected void afterModify() {}

	@Override
	public final T delete() {

		try {
			T caller = getCaller();

			if (beforeDelete()) {
				LifecycleEvents.me().oneMore(new LifecycleEvent(caller, DELETE, createTimestamp()));
			}

			return caller;
		} finally {
			afterDelete();
		}
	}

	/**
	 * 
	 * @return
	 */
	protected boolean beforeDelete() {
		return true;
	}

	protected void afterDelete() {}

	/**
	 * 
	 * @return
	 */
	protected long createTimestamp() {
		return System.currentTimeMillis();
	}

	/**
	 * nerver do domain logic on this method,return who call create,modify,delete or acquire simply
	 * 
	 * @return
	 */
	protected abstract T getCaller();
}
