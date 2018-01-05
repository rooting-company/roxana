package br.com.rooting.roxana.translator;

import static br.com.rooting.roxana.config.RoxanaProperties.MessageBundle.MESSAGE_BUNDLE_PATH_PROPERTY;

public class MessageBundlePathInvalid extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "Invalid message bundle path. Please, "
										+ "verify the property " + MESSAGE_BUNDLE_PATH_PROPERTY
										+ " of your String property file.";

	MessageBundlePathInvalid(Exception e) {
		super(MESSAGE, e);
	}
	
}