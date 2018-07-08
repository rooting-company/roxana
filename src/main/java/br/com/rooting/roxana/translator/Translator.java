package br.com.rooting.roxana.translator;

import br.com.rooting.roxana.parameter.Parameter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public interface Translator {

    String INTERPOLATION_PREFIX = "{";

    String INTERPOLATION_SUFFIX = "}";

    String PARAMETER_INTERPOLATION_PREFIX = "[";

    String PARAMETER_INTERPOLATION_SUFFIX = "]";

    String NOT_FOUND_DELIMITER = "???";

    static String getInterpolatedKeyOf(String key) {
        return INTERPOLATION_PREFIX + key + INTERPOLATION_SUFFIX;
    }

    String translate(final String key, final Locale locale, final List<Parameter> parameters);

    default String translate(final String key, final Locale locale, final Parameter... parameters) {
        return this.translate(key, locale, Arrays.asList(parameters));
    }

    default String translate(final String key, final List<Parameter> parameters) {
        return this.translate(key, this.getLocale(), parameters);
    }

    default String translate(final String key, final Parameter... parameters) {
        return this.translate(key, this.getLocale(), parameters);
    }

    Locale getLocale();

}