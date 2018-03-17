package br.com.rooting.roxana.translator;

import java.util.List;
import java.util.Locale;

import br.com.rooting.roxana.parameter.Parameter;

public class MockedTranslator implements Translator {
	
	@Override
	public String translate(String key, Locale locale, List<Parameter> parameters) {
		for (Parameter parameter : parameters) {
			key += parameter.getFormattedValue(locale);
		}
		return key;
	}

	@Override
	public Locale getLocale() {
		return Locale.getDefault();
	}

}