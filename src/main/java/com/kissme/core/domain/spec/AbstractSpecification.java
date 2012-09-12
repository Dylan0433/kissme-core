package com.kissme.core.domain.spec;

/**
 * 
 * @author loudyn
 * 
 * @param <T>
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.core.domain.spec.Specification#and(com.kissme.core.domain.spec.Specification)
	 */
	public final Specification<T> and(Specification<T> another) {
		return new AndSpecification<T>(this, another);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	public static class AndSpecification<T> extends AbstractSpecification<T> {
		private final Specification<T> one;
		private final Specification<T> another;

		public AndSpecification(final Specification<T> one, final Specification<T> another) {
			this.one = one;
			this.another = another;
		}

		public boolean isSatisfiedBy(T entity) {
			return one.isSatisfiedBy(entity) && another.isSatisfiedBy(entity);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.core.domain.spec.Specification#or(com.kissme.core.domain.spec.Specification)
	 */
	public final Specification<T> or(Specification<T> another) {
		return new OrSpecification<T>(this, another);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	public static class OrSpecification<T> extends AbstractSpecification<T> {
		private final Specification<T> one;
		private final Specification<T> another;

		public OrSpecification(final Specification<T> one, final Specification<T> another) {
			this.one = one;
			this.another = another;
		}

		public boolean isSatisfiedBy(T entity) {
			return one.isSatisfiedBy(entity) || another.isSatisfiedBy(entity);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kissme.core.domain.spec.Specification#not(com.kissme.core.domain.spec.Specification)
	 */
	public final Specification<T> not(Specification<T> another) {
		return new NotSpecification<T>(another);
	}

	/**
	 * 
	 * @author loudyn
	 * 
	 */
	public static class NotSpecification<T> extends AbstractSpecification<T> {

		private final Specification<T> another;

		public NotSpecification(final Specification<T> another) {
			this.another = another;
		}

		public boolean isSatisfiedBy(T entity) {
			return !another.isSatisfiedBy(entity);
		}
	}

}
