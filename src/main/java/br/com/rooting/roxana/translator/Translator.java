package br.com.rooting.roxana.translator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.rooting.roxana.RoxanaProperties;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.translator.exception.FailToTranslateException;
import br.com.rooting.roxana.translator.exception.MessageBundlePathNotDefined;

@Component
public class Translator {
	
	private static final Logger LOG = LoggerFactory.getLogger(Translator.class);

	private static final String ERROR_FAIL_TO_TRANSLATE_A_MESSAGE = "Failed to translate the following message: ";
	
	private static final String NOT_FOUND_DELIMITER = "???";
	
	private static final String INTERPOLATION_PREFIX = "{";
	private static final String INTERPOLATION_PREFIX_ESCAPED = "\\" + INTERPOLATION_PREFIX;
	
	private static final String INTERPOLATION_DELIMITATON = "}";
	private static final String INTERPOLATION_DELIMITATON_ESCAPED = "\\" + INTERPOLATION_DELIMITATON;
	
	// "^.*?\\{|\\}.*?\\{|\\}.*?$";
	private static final String INTERPOLATION_REGEX = "^.*?" + INTERPOLATION_PREFIX_ESCAPED 	 + 
													  "|"    + INTERPOLATION_DELIMITATON_ESCAPED + 
													  ".*?"	 + INTERPOLATION_PREFIX_ESCAPED 	 + 
													  "|" 	 + INTERPOLATION_DELIMITATON_ESCAPED + 
													  ".*?$";
	
	private final String resourceBundlePath;
	
	private final Boolean suppressFailToTranslateException;
	
	private final Locale defaultLocale;
	// Lazy load
	private ResourceBundle resourceBundle;
	
	@Autowired
	private Translator(final RoxanaProperties properties, final MessageSource messageSource) {
		super();
		this.resourceBundlePath = properties.getMessageBundlePath();
		this.suppressFailToTranslateException = properties.getMessageBundleSuppressFailToTranslateException();
		this.defaultLocale = this.getRootingLocale(properties.getMessageBundleLocale());
	}
	
	public String translate(final String key, final Collection<Parameter> parameters) {
		Locale locale = this.getDefaultLocale();
		return this.interpolateKey(this.getResourceBundle(), locale, key, parameters);
	}
	
	public String translate(final String key, final Parameter...parameters) {
		Locale locale = this.getDefaultLocale();
		return this.interpolateKey(this.getResourceBundle(), locale, key, Arrays.asList(parameters));
	}
	
	public String translate(final String key, final Locale locale, final Collection<Parameter> parameters) {
		return this.interpolateKey(this.getResourceBundle(), locale, key, parameters);
	}
	
	public String translate(final String key, final Locale locale, final Parameter...parameters) {
		return this.interpolateKey(this.getResourceBundle(), locale, key, Arrays.asList(parameters));
	}
	
	private Locale getRootingLocale(@Nullable String languadeTag) {
		return Optional.ofNullable(languadeTag)
					.map(s -> Locale.forLanguageTag(s))
						.filter(l -> !StringUtils.isEmpty(l.getCountry()) && !StringUtils.isEmpty(l.getLanguage()))
						.orElseGet(Locale::getDefault);
	}
	
	// TODO Ver possibilidade de fazer um cache disso (guardar isso como cash em um hash map por exemplo).
	// TODO Ver lambda para tratar exception.
	private ResourceBundle getValidResourceBundle(Locale locale) {
		String bundlePath = Optional.of(this.getResourceBundlePath())
				.orElseThrow(MessageBundlePathNotDefined::new);

		try {
			return ResourceBundle.getBundle(bundlePath, locale);
		} catch (RuntimeErrorException e) {
			throw new MessageBundlePathNotDefined();
		}
	}
	
	private String getInterpolationReplaceRegex(String s) {
		return INTERPOLATION_PREFIX_ESCAPED + s + INTERPOLATION_DELIMITATON_ESCAPED;
	}
	
	// TODO Investigar por que roda em loop (debug).
	private String interpolateKey(ResourceBundle resourceBundle, Locale locale, String key, Collection<Parameter> parameters) {
		return Arrays.stream(key.split(INTERPOLATION_REGEX))
						.distinct()
						.filter(s -> !s.isEmpty())
						.reduce(key, (k, s) -> k.replaceAll(this.getInterpolationReplaceRegex(s), 
														 	Matcher.quoteReplacement(this.interpolateParameters(resourceBundle, locale, s, parameters))
															)
								);
	}

	//TODO Retirar try e catch e colocar lambda
	private String interpolateParameters(ResourceBundle resourceBundle, Locale locale, String key, Collection<Parameter> parameters) {
		return Optional.of(key).map(s -> {
			try {
				return resourceBundle.getString(s);
			} catch (Exception e) {
				if (!this.getSuppressFailToTranslateException()) {
					throw new FailToTranslateException(key, e);
				}
				LOG.error(ERROR_FAIL_TO_TRANSLATE_A_MESSAGE + key, e);
				return null;
			}
		}).map(s -> parameters.stream()
				.reduce(s, (s1, s2) -> s1.replaceAll(this.getInterpolationReplaceRegex(s2.getName()), 
														Matcher.quoteReplacement(s2.getFormattedValue(locale))
													  ), 
							(s1, s2) -> null
						)
				).orElse(this.getNotFoundInterpolationValue(key));
	}
	
	private String getNotFoundInterpolationValue(String keyNotFound) {
		return NOT_FOUND_DELIMITER + keyNotFound + NOT_FOUND_DELIMITER;
	}
	
	public Locale getDefaultLocale() {
		return this.defaultLocale;
	}
	
	// Lazy load.
	public ResourceBundle getResourceBundle() {
		return Optional.ofNullable(this.resourceBundle)
				.orElse(this.getValidResourceBundle(this.getDefaultLocale()));
	}
	
	public String getResourceBundlePath() {
		return this.resourceBundlePath;
	}
	
	public Boolean getSuppressFailToTranslateException() {
		return this.suppressFailToTranslateException;
	}
	
	public static String getInterpoledKeyOf(String key) {
		return INTERPOLATION_PREFIX + key + INTERPOLATION_DELIMITATON;
	}

}
