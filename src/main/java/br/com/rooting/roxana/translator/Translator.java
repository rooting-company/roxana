package br.com.rooting.roxana.translator;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.parameter.Parameter;

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
	
	private final ResourceBundle resourceBundle;
	
	@Autowired
	private Translator(final RoxanaProperties properties) {
		super();
		this.resourceBundlePath = properties.getMessageBundlePath();
		this.suppressFailToTranslateException = properties.getMessageBundleSuppressFailsTranslations();
		this.defaultLocale = this.getRootingLocale(properties.getMessageBundleLocale());
		this.resourceBundle = this.getValidResourceBundle(this.getDefaultLocale());
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
						.map(Locale::forLanguageTag)
						.filter(this::isAValidLocale)
						.orElseGet(Locale::getDefault);
	}
	
	private boolean isAValidLocale(final Locale l) {
		if(!StringUtils.isEmpty(l.getCountry()) 
				&& !StringUtils.isEmpty(l.getLanguage())) {
			return true;
		}
		return false;
	}
	
	private ResourceBundle getValidResourceBundle(Locale locale) {
		String bundlePath = Optional.ofNullable(this.getResourceBundlePath())
									.orElseThrow(MessageBundlePathNotDefined::new);

		try {
			return ResourceBundle.getBundle(bundlePath, locale);
		} catch (Exception e) {
			throw new MessageBundlePathNotDefined();
		}
	}
	
	private String getInterpolationReplaceRegex(String s) {
		return INTERPOLATION_PREFIX_ESCAPED + s + INTERPOLATION_DELIMITATON_ESCAPED;
	}
	
	private String interpolateKey(ResourceBundle resourceBundle, Locale locale, String key, Collection<Parameter> parameters) {
		return Arrays.stream(key.split(INTERPOLATION_REGEX))
						.distinct()
						.filter(message -> !message.isEmpty())
						.reduce(key, (k, message) -> {
							String replaceRegex = this.getInterpolationReplaceRegex(message);
							String messageInterpolated = this.interpolateMessage(resourceBundle, locale, message, parameters);
							messageInterpolated = Matcher.quoteReplacement(messageInterpolated);
							return k.replaceAll(replaceRegex, messageInterpolated);
						});
	}

	private String interpolateMessage(ResourceBundle resourceBundle, Locale locale, String message, Collection<Parameter> parameters) {
		try {
			String translation = resourceBundle.getString(message);
			translation = this.interpolateParameters(locale, translation, parameters);
			return translation;
		} catch (Exception e) {
			if (!this.getSuppressFailToTranslateException()) {
				throw new FailToTranslateException(message, e);
			}
			LOG.error(ERROR_FAIL_TO_TRANSLATE_A_MESSAGE + message, e);
			return this.getNotFoundInterpolationValue(message);
		}
	}
	
	private String interpolateParameters(final Locale locale, String message, final Collection<Parameter> parameters) {
//		O codigo deste metodo tem o mesmo efeito do seguinte codigo:
		
//		return parameters.stream()
//				 .reduce(message, (s, p) -> this.interpolateParameter(locale, s, p), (s1, s2) -> null);
		
		for (Parameter p : parameters) {
			message = this.interpolateParameter(locale, message, p);
		}
		return message;
	}
	
	private String interpolateParameter(final Locale locale, final String message, final Parameter parameter) {
		String replaceRegex = this.getInterpolationReplaceRegex(parameter.getName());
		String formattedValue =  Matcher.quoteReplacement(parameter.getFormattedValue(locale));
		return message.replaceAll(replaceRegex, formattedValue);
	}
	
	private String getNotFoundInterpolationValue(String keyNotFound) {
		return NOT_FOUND_DELIMITER + keyNotFound + NOT_FOUND_DELIMITER;
	}
	
	public Locale getDefaultLocale() {
		return this.defaultLocale;
	}
	
	public ResourceBundle getResourceBundle() {
		return this.resourceBundle;
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