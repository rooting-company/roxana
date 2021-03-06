package br.com.rooting.roxana.translator;

import br.com.rooting.roxana.parameter.Parameter;

import java.util.List;
import java.util.Locale;

public class MockedTranslator implements Translator {

    @Override
    public String translate(String key, Locale locale, List<Parameter> parameters) {
        StringBuilder keyBuilder = new StringBuilder(key);
        for (Parameter parameter : parameters) {
            keyBuilder.append(parameter.getFormattedValue(locale));
        }
        return keyBuilder.toString();
    }

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

}