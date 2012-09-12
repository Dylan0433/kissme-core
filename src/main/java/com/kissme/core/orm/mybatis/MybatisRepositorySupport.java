package com.kissme.core.orm.mybatis;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kissme.core.orm.DataAccessException;
import com.kissme.core.orm.Page;
import com.kissme.lang.Ghost;
import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class MybatisRepositorySupport<ID, T> extends SqlSessionDaoSupport {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final Class<ID> idClazz;
	private final Class<T> entityClazz;

	@SuppressWarnings("unchecked")
	protected MybatisRepositorySupport() {
		Class<?>[] genericsTypes = Ghost.me(getClass()).genericsTypes(MybatisRepositorySupport.class);
		this.idClazz = (Class<ID>) genericsTypes[0];
		this.entityClazz = (Class<T>) genericsTypes[1];
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T get(ID id) {
		Preconditions.notNull(id, new DataAccessException("id must not be null!"));

		try {
			return (T) getSqlSession().selectOne(getNamespace() + ".get", id);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * 
	 * @param entity
	 */
	public void save(T entity) {
		Preconditions.notNull(entity, new DataAccessException("entity must not be null!"));

		try {
			getSqlSession().insert(getNamespace() + ".save", entity);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * 
	 * @param entity
	 */
	public void update(T entity) {
		Preconditions.notNull(entity, new DataAccessException("entity must not be null!"));
		try {

			getSqlSession().update(getNamespace() + ".update", entity);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * 
	 * @param entity
	 */
	public void delete(T entity) {
		Preconditions.notNull(entity, new DataAccessException("entity must not be null!"));
		try {

			getSqlSession().delete(getNamespace() + ".delete", entity);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> query(Object value) {
		try {

			return getSqlSession().selectList(getNamespace() + ".query", value);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * 
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> queryPage(Page<T> page) {
		Preconditions.notNull(page, new DataAccessException("page must not be null!"));

		try {

			List<T> result = getSqlSession().selectList(getNamespace() + ".queryPage", page);
			page.setResult(result);
			return page;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	protected Logger getLogger() {
		return logger;
	}

	/**
	 * 
	 * @return
	 */
	public Class<ID> getIdClazz() {
		return idClazz;
	}

	/**
	 * 
	 * @return
	 */
	public Class<T> getEntityClazz() {
		return entityClazz;
	}

	/**
	 * 
	 * @return
	 */
	protected String getNamespace() {
		return entityClazz.getName();
	}
}
