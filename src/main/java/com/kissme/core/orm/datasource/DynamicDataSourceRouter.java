package com.kissme.core.orm.datasource;

/**
 * 
 * @author loudyn
 * 
 */
public final class DynamicDataSourceRouter {
	private final static ThreadLocal<String> ROUTE_HOLDER = new ThreadLocal<String>();

	/**
	 * 
	 * @param route
	 */
	public static void specifyRoute(String route) {
		ROUTE_HOLDER.set(route);
	}

	/**
	 * 
	 * @return
	 */
	public static String getSpecifiedRoute() {
		return ROUTE_HOLDER.get();
	}

	/**
	 * 
	 */
	public static void clearSpecifiedRoute() {
		ROUTE_HOLDER.set(null);
	}

	private DynamicDataSourceRouter() {}
}
