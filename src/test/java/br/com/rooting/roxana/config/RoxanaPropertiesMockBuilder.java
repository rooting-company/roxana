package br.com.rooting.roxana.config;

import static br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy.TRANSLATED;
import static br.com.rooting.roxana.translator.LocaleTagEnum.PT_BR;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;

public class RoxanaPropertiesMockBuilder {
	
	public static final String TRANSLATOR_MESSAGES_BUNDLE_BASE_NAME = "translator-messages-bundle/messages";
	
	private String locale = PT_BR.getTag();	
	
	private boolean supressFailsTranslations = true;
	
	private String baseName = TRANSLATOR_MESSAGES_BUNDLE_BASE_NAME;
	
	private ResponseStrategy responseStrategy = TRANSLATED;
	
	private Boolean suppressOthersExceptions = Boolean.TRUE;
	
	public RoxanaProperties build() {
		RoxanaProperties mock = mock(RoxanaProperties.class);
		when(mock.getMessageBundleLocale()).thenReturn(this.getLocale());
		when(mock.getMessageBundleSuppressFailsTranslations()).thenReturn(this.isSupressFailsTranslations());
		when(mock.getMessageBundleBaseName()).thenReturn(this.getBaseName());
		when(mock.getBusinessResponseStrategy()).thenReturn(this.getResponseStrategy());
		when(mock.getBusinessExceptionHandlerSuppressOthersExceptions()).thenReturn(this.getSuppressOthersExceptions());
		return mock;
	}

	private String getLocale() {
		return locale;
	}

	public RoxanaPropertiesMockBuilder withLocale(String locale) {
		this.locale = locale;
		return this;
	}

	private boolean isSupressFailsTranslations() {
		return supressFailsTranslations;
	}

	public RoxanaPropertiesMockBuilder withSupressFailsTranslations(boolean supressFailsTranslations) {
		this.supressFailsTranslations = supressFailsTranslations;
		return this;
	}

	private String getBaseName() {
		return baseName;
	}

	public RoxanaPropertiesMockBuilder withBaseName(String baseName) {
		this.baseName = baseName;
		return this;
	}

	private ResponseStrategy getResponseStrategy() {
		return responseStrategy;
	}

	public RoxanaPropertiesMockBuilder withResponseStrategy(ResponseStrategy responseStrategy) {
		this.responseStrategy = responseStrategy;
		return this;
	}

	private Boolean getSuppressOthersExceptions() {
		return this.suppressOthersExceptions;
	}

	public RoxanaPropertiesMockBuilder withSuppressOthersExceptions(Boolean suppressOthersExceptions) {
		this.suppressOthersExceptions = suppressOthersExceptions;
		return this;
	}
	
}