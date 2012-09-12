package com.kissme.core.domain.spec;

/**
 * 
 * @author loudyn
 *
 * @param <T>
 */
public interface Specification<T> {
	/**
	 * 
	 * @param entity
	 * @return
	 */
	boolean isSatisfiedBy(T entity);

	/**
	 * 
	 * @param another
	 * @return
	 */
	Specification<T> and(Specification<T> another);

	/**
	 * 
	 * @param another
	 * @return
	 */
	Specification<T> or(Specification<T> another);

	/**
	 * 
	 * @param another
	 * @return
	 */
	Specification<T> not(Specification<T> another);
}
