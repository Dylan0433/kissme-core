package com.kissme.core.domain.event;

import java.util.EventListener;

import com.google.common.eventbus.Subscribe;
import com.kissme.core.domain.event.LifecycleEvent.LifecycleEventType;
import com.kissme.core.domain.monitor.Monitoring;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class LifecycleEventHandler implements EventListener {

	/**
	 * 
	 * @param event
	 */
	@Subscribe
	@Monitoring(action = "NP")
	public final void handleLifecycleEvent(LifecycleEvent event) {

		if (!isAcceptableLifecycleEvent(event)) {
			return;
		}

		LifecycleEventType type = event.getType();
		switch (type) {
		case CREATE:
			onCreate(event);
			break;
		case MODIFY:
			onModify(event);
			break;
		default:
			onDelete(event);
		}
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	protected abstract boolean isAcceptableLifecycleEvent(LifecycleEvent event);

	/**
	 * 
	 * @param event
	 */
	protected abstract void onCreate(LifecycleEvent event);

	/**
	 * 
	 * @param event
	 */
	protected abstract void onModify(LifecycleEvent event);

	/**
	 * 
	 * @param event
	 */
	protected abstract void onDelete(LifecycleEvent event);
}
