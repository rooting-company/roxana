package br.com.rooting.roxana.translator;

import static br.com.rooting.roxana.config.RoxanaProperties.MessageBundle.MESSAGE_BUNDLE_BASE_NAME_PROPERTY;

public class MessageBundleBaseNameNotDefinedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "The property " + MESSAGE_BUNDLE_BASE_NAME_PROPERTY
										+ " was not defined in the Spring context." 
										+ "It is possible define the property by "
										+ "editing the application.yml or "
										+ "application.properties files.";

	MessageBundleBaseNameNotDefinedException() {
		super(MESSAGE);
	}
	
}