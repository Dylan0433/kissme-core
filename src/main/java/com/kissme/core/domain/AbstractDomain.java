package com.kissme.core.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.kissme.lang.Strings;

/**
 * 
 * @author loudyn
 * 
 * @param <ID>
 */
@SuppressWarnings("serial")
public abstract class AbstractDomain implements DomainObject<AbstractDomain>, Serializable {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean hasIdentity() {
		return !Strings.isBlank(getId());
	}

	public boolean sameIdentityAs(AbstractDomain other) {
		if (null == other) {
			return false;
		}

		return new EqualsBuilder().append(this.getId(), other.getId()).isEquals();
	}
}
