package br.com.rooting.roxana.translator;

import static br.com.rooting.roxana.config.RoxanaProperties.MessageBundle.MESSAGE_BUNDLE_LOCALE_PROPERTY;

public class InvalidMessageBundleLocaleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "The locale tag (%s) informed is invalid. "
            + "Please, verify the property "
            + MESSAGE_BUNDLE_LOCALE_PROPERTY
            + " of your String property file.";

    InvalidMessageBundleLocaleException(final String invalidLanguageTag) {
        super(String.format(MESSAGE, invalidLanguageTag));
    }

}