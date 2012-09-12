package com.kissme.core.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.kissme.core.web.filter.FlashModel;
import com.kissme.lang.Files;

/**
 * @author loudyn
 */
public abstract class ControllerSupport extends MultiActionController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * @param success
	 */
	public final void success(String success) {
		FlashModel.setSuccessMessage(success);
	}

	/**
	 * @param error
	 */
	public final void error(String error) {
		FlashModel.setErrorMessage(error);
	}

	/**
	 * @param warning
	 */
	public final void warning(String warning) {
		FlashModel.setWarningMessage(warning);
	}

	/**
	 * @param info
	 */
	public final void info(String info) {
		FlashModel.setInfoMessage(info);
	}

	/**
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setIgnoreInvalidFields(true);
		binder.setIgnoreUnknownFields(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), false));
	}

	/**
	 * 
	 * @return
	 */
	protected String getViewPackage() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @return
	 */
	protected String listView() {
		return Files.asUnix(getViewPackage()) + "/list";
	}

	/**
	 * 
	 * @return
	 */
	protected String formView() {
		return Files.asUnix(getViewPackage() + "/form");
	}

	/**
	 * 
	 * @param validator
	 */
	@Autowired(required = false)
	protected void setValidator(Validator validator) {
		setValidators(new Validator[] { validator });
	}

	/**
	 * 
	 * @return
	 */
	protected Logger getLogger() {
		return logger;
	}
}
