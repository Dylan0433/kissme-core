package com.kissme.core.orm;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.kissme.core.helper.WordHelper;
import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public class PageHelper {

	private static final Pattern DISTINCT_PATTERN = Pattern.compile(
																	"select?(\\s+distinct{1}\\s{1}(.+)\\s{1})from{1}\\s+.+",
																	Pattern.CASE_INSENSITIVE
																);
	private static final int SELECT_ITEMS_GROUP = 2;

	private static final String[] SQL_META = new String[] { " ", "'", "select", "or", "and", "union", "exec", "-- ", "#", ";" };
	private static final WordHelper SQL_SAFE_GUARDER = new WordHelper() {

		@Override
		protected void afterFilter(List<String> hitWords, String text, String replacement) {
			if (!hitWords.isEmpty()) {
				throw new IllegalArgumentException("Bad sql!");
			}
		}

	}.addAll(Arrays.asList(SQL_META));

	/**
	 * 
	 * @param sql
	 * @return
	 */
	public static String buildCountSQL(String sql) {
		if (isContainsDistinct(sql)) {
			return buildDistinctCountSql(sql);
		}

		return buildCountSql(sql);
	}

	/**
	 * 
	 * @param page
	 * @param sql
	 * @return
	 */
	public static String buildOrderByString(Page<?> page, String sql) {

		Preconditions.notNull(page, "page must not null");
		doSQLSafeGuard(page);

		StringBuilder orderBy = new StringBuilder();
		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = StringUtils.split(page.getOrder(), ',');

			Preconditions.isTrue(orderByArray.length == orderArray.length, "orderBy.length must equal order.length");

			for (int i = 0; i < orderByArray.length; i++) {
				if (i > 0) {
					orderBy.append(",");
				}

				orderBy.append(String.format("%s %s", orderByArray[i], orderArray[i]));
			}

			if (sql.toLowerCase().indexOf("order by") < 0) {
				orderBy.insert(0, " order by ");
			}
			else {
				orderBy.insert(0, ",");
			}
		}

		return orderBy.toString();
	}

	private static void doSQLSafeGuard(Page<?> page) {

		if (page.isOrderBySetted()) {
			SQL_SAFE_GUARDER.filter(page.getOrderBy(), "");
		}
	}

	/**
	 * @param sql
	 * @return
	 */
	private static boolean isContainsDistinct(String sql) {

		if (sql.indexOf("distinct") == -1) {
			return sql.contains("DISTINCT");
		}

		return true;
	}

	/**
	 * @param sql
	 * @return
	 */
	private static String buildDistinctCountSql(String sql) {
		String selectItems = findDistinctSelectItems(sql);
		String fromHql = buildFromHql(sql);
		return String.format("select count(distinct %s) %s", selectItems, fromHql);
	}

	/**
	 * @param sql
	 * @return
	 */
	private static String findDistinctSelectItems(String sql) {
		Matcher m = DISTINCT_PATTERN.matcher(sql);
		if (m.matches()) {
			return m.group(SELECT_ITEMS_GROUP);
		}
		throw new UnsupportedOperationException("can't count this distintc hql");
	}

	/**
	 * @param sql
	 * @return
	 */
	private static String buildCountSql(String sql) {
		String fromHql = buildFromHql(sql);
		return String.format("select count(*) %s", fromHql);
	}

	/**
	 * @param sql
	 * @return
	 */
	private static String buildFromHql(String sql) {
		String fromHql = sql;
		if (fromHql.indexOf("from") == -1) {
			fromHql = StringUtils.substringAfter(fromHql, "FROM");
		}
		else {
			fromHql = StringUtils.substringAfter(fromHql, "from");
		}

		if (fromHql.indexOf("order by") == -1) {
			fromHql = StringUtils.substringBefore(fromHql, "ORDER BY");
		}
		else {
			fromHql = StringUtils.substringBefore(fromHql, "order by");
		}

		return "from" + fromHql;
	}

}
