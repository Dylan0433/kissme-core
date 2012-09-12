package com.kissme.core.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.google.common.base.Objects;
import com.kissme.lang.Preconditions;

/**
 * 
 * @author loudyn
 * 
 */
public abstract class CrudControllerSupport<ID, T> extends ControllerSupport {
	
	/**
	 * 
	 * @param one
	 * @param another
	 */
	protected void checkIdNotModified(ID one, ID another) {
		Preconditions.isTrue(Objects.equal(one, another));
	}

	/**
	 * this method must use http get e.g: RequestMapping(value = "/create", method = RequestMethod.GET)
	 * 
	 * @param model
	 * @return
	 */
	public abstract String create(Model model);

	/**
	 * this method must use http post
	 * 
	 * @param entity
	 * @param result
	 * @return
	 */
	public abstract String create(T entity, BindingResult result);

	/**
	 * this method must use http get
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	public abstract String edit(ID id, Model model);

	/**
	 * this method must use http put
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	public abstract String edit(ID id, HttpServletRequest request);

	/**
	 * this method must use http delete
	 * 
	 * @param id
	 * @return
	 */
	public abstract String delete(ID id);

	/**
	 * this method must use http delete
	 * 
	 * @param request
	 * @return
	 */
	public abstract String delete(HttpServletRequest request);
}
