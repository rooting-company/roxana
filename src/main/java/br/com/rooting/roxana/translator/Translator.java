package br.com.rooting.roxana.translator;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import br.com.rooting.roxana.parameter.Parameter;

public interface Translator {
	
	public static final String INTERPOLATION_PREFIX = "{";
	
	public static final String INTERPOLATION_SUFIX = "}";
	
	public static final String NOT_FOUND_DELIMITER = "???";
	
	public static String getInterpoledKeyOf(String key) {
		return INTERPOLATION_PREFIX + key + INTERPOLATION_SUFIX;
	}
	
	public String translate(final String key, final Locale locale, final List<Parameter> parameters);
	
	public default String translate(final String key, final Locale locale, final Parameter...parameters) {
		return this.translate(key, locale, Arrays.asList(parameters));
	}
	
	public default String translate(final String key, final List<Parameter> parameters) {
		return this.translate(key, this.getLocale(), parameters);
	}
	
	public default String translate(final String key, final Parameter...parameters) {
		return this.translate(key, this.getLocale(), parameters);
	}
	
	public Boolean getSuppressFailToTranslateException();

	public Locale getLocale();
	
}