package com.kissme.core.orm.mybatis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.DefaultParameterHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.kissme.core.orm.Page;
import com.kissme.core.orm.PageHelper;
import com.kissme.core.orm.dialect.Dialect;
import com.kissme.lang.Ghost;
import com.kissme.lang.Lang;
import com.kissme.lang.Preconditions;
import com.kissme.lang.Strings;

/**
 * 
 * @author loudyn
 * 
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class MybatisPagePlugin implements Interceptor {

	private Dialect dialect;
	private String interceptPattern;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#intercept(org.apache.ibatis.plugin.Invocation)
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		Object target = invocation.getTarget();
		if (target instanceof RoutingStatementHandler) {
			RoutingStatementHandler handler = (RoutingStatementHandler) target;
			StatementHandler delegate = (StatementHandler) Ghost.me(handler.getClass()).ejector(handler, "delegate").eject();
			interceptIfNecessary(invocation, delegate);
		}

		return invocation.proceed();
	}

	private void interceptIfNecessary(Invocation invocation, StatementHandler delegete) throws Exception {

		MappedStatement stmt = (MappedStatement) Ghost.me(delegete.getClass()).ejector(delegete, "mappedStatement").eject();
		if (!isSantisfiedByPattern(stmt)) {
			return;
		}

		BoundSql boundSql = delegete.getBoundSql();
		Object paramObject = boundSql.getParameterObject();

		if (!isSantisfiedByParam(paramObject, Page.class)) {
			return;
		}

		// paramObject instanceOf page,we intercept it.
		Page<?> page = (Page<?>) paramObject;

		// create count sql
		String countSql = PageHelper.buildCountSQL(boundSql.getSql());
		long count = executeCountSql(invocation, stmt, boundSql, countSql, page);
		page.setTotalCount(count);

		// create pageSql like select table_field from table where table_field=? order by table_field limit ?,?
		String pageSql = createPageSql(page, boundSql.getSql());
		Ghost.me(boundSql.getClass()).injector(boundSql, "sql").inject(pageSql);
	}

	private boolean isSantisfiedByPattern(MappedStatement stmt) {

		String stmtId = stmt.getId();

		// if user did not define the interceptPattern,always return true
		if (Strings.isBlank(interceptPattern)) {
			return true;
		}

		return stmtId.matches(this.interceptPattern);
	}

	private boolean isSantisfiedByParam(Object paramObject, Class<?> paramClazz) {
		if (null == paramObject) {
			return false;
		}

		// verify the paramClazz is superClass of the paramObject
		return paramClazz.isAssignableFrom(paramObject.getClass());
	}

	private long executeCountSql(Invocation invocation, MappedStatement stmt,
									BoundSql sql, String countSql, Object paramObject) throws Exception {

		long result = -1;
		BoundSql countBoundSql = new BoundSql(
												stmt.getConfiguration(),
												countSql,
												sql.getParameterMappings(),
												paramObject
									);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {

			Connection conn = getConnection(invocation);
			pstmt = conn.prepareStatement(countSql);
			ParameterHandler paramHandler = new DefaultParameterHandler(stmt, paramObject, countBoundSql);
			paramHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result = rs.getLong(1);
			}

		} finally {
			freeQuietly(rs, pstmt);
		}

		return result;
	}

	private Connection getConnection(Invocation invocation) {
		return (Connection) invocation.getArgs()[0];
	}

	private void freeQuietly(ResultSet rs, PreparedStatement pstmt) {
		try {
			if (null != rs) {
				rs.close();
			}
		} catch (Exception e) {}

		try {
			if (null != pstmt) {
				pstmt.close();
			}
		} catch (Exception e) {}
	}

	private String createPageSql(Page<?> page, String sql) {

		sql = String.format("%s%s", sql, PageHelper.buildOrderByString(page, sql));
		Dialect dialect = getDialect();
		sql = dialect.getLimitString(sql, page.getFirst() - 1, page.getPageSize());
		return sql;
	}

	protected Dialect getDialect() {
		return this.dialect;
	}

	@SuppressWarnings("unchecked")
	private Dialect getDialect(String dialectClazz) {

		try {

			Class<Dialect> clazz = (Class<Dialect>) Class.forName(dialectClazz);
			return Ghost.me(clazz).born();
		} catch (Exception e) {
			throw Lang.uncheck(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#setProperties(java.util.Properties)
	 */
	@Override
	public void setProperties(Properties props) {
		Preconditions.hasText(props.getProperty("dialect"));
		this.dialect = getDialect(props.getProperty("dialect"));
		this.interceptPattern = props.getProperty("interceptPattern");
	}

}
