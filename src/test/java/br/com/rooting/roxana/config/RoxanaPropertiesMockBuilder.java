package br.com.rooting.roxana.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.rooting.roxana.translator.LocaleTagEnum;

public class RoxanaPropertiesMockBuilder {
	
	public static final String TRANSLATOR_MESSAGES_BUNDLE_BASE_NAME = "translator-messages-bundle/messages";
	
	private String locale = LocaleTagEnum.PT_BR.getTag();	
	
	private boolean supressFailsTranslations = true;
	
	private String baseName = TRANSLATOR_MESSAGES_BUNDLE_BASE_NAME;
	
	public RoxanaProperties build() {
		RoxanaProperties mock = mock(RoxanaProperties.class);
		when(mock.getMessageBundleLocale()).thenReturn(this.getLocale());
		when(mock.getMessageBundleSuppressFailsTranslations()).thenReturn(this.isSupressFailsTranslations());
		when(mock.getMessageBundleBaseName()).thenReturn(this.getBaseName());
		return mock;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isSupressFailsTranslations() {
		return supressFailsTranslations;
	}

	public void setSupressFailsTranslations(boolean supressFailsTranslations) {
		this.supressFailsTranslations = supressFailsTranslations;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	
}