package br.com.rooting.roxana.translator;

import static br.com.rooting.roxana.config.RoxanaProperties.MessageBundle.MESSAGE_BUNDLE_BASE_NAME_PROPERTY;

public class FailToCreateRoxanaResourceBundleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "Can not create ResourceBundle. Please, "
            + "verify the property " + MESSAGE_BUNDLE_BASE_NAME_PROPERTY
            + " of your String property file.";

    FailToCreateRoxanaResourceBundleException(Exception cause) {
        super(MESSAGE, cause);
    }

}