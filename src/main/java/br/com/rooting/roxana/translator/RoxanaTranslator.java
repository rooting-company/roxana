package br.com.rooting.roxana.translator;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.parameter.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;

@Component
public class RoxanaTranslator implements Translator {

    private static final Logger LOG = LoggerFactory.getLogger(RoxanaTranslator.class);

    private static final String ERROR_FAIL_TO_TRANSLATE_A_MESSAGE = "Failed to translate the following message: ";

    private static final String ESCAPED_CHARACTER = "\\";

    private static final String INTERPOLATION_PREFIX_ESCAPED = ESCAPED_CHARACTER + INTERPOLATION_PREFIX;

    private static final String INTERPOLATION_SUFFIX_ESCAPED = ESCAPED_CHARACTER + INTERPOLATION_SUFFIX;

    private static final String PARAMETER_INTERPOLATION_PREFIX_ESCAPED = ESCAPED_CHARACTER + PARAMETER_INTERPOLATION_PREFIX;

    private static final String PARAMETER_INTERPOLATION_SUFFIX_ESCAPED = ESCAPED_CHARACTER + PARAMETER_INTERPOLATION_SUFFIX;

    // "^.*?\\{|\\}.*?\\{|\\}.*?$";
    private static final String INTERPOLATION_REGEX = "^.*?" + INTERPOLATION_PREFIX_ESCAPED +
            "|" + INTERPOLATION_SUFFIX_ESCAPED +
            ".*?" + INTERPOLATION_PREFIX_ESCAPED +
            "|" + INTERPOLATION_SUFFIX_ESCAPED +
            ".*?$";

    private final Boolean suppressFailToTranslateException;

    private final Locale locale;

    private final ResourceBundle resourceBundle;

    @Autowired
    protected RoxanaTranslator(final RoxanaProperties properties) {
        super();
        this.suppressFailToTranslateException = properties.getMessageBundleSuppressFailsTranslations();
        this.locale = this.getRoxanaLocale(properties.getMessageBundleLocale());
        this.resourceBundle = this.getValidResourceBundle(properties.getMessageBundleBaseName(), this.getLocale());
    }

    @Override
    public String translate(final String key, final Locale locale, final List<Parameter> parameters) {
        return this.interpolateKey(this.getResourceBundle(), locale, key, parameters);
    }

    private Locale getRoxanaLocale(@Nullable String languageTag) {
        Locale locale = Optional.ofNullable(languageTag)
                .map(Locale::forLanguageTag)
                .orElseGet(Locale::getDefault);

        if (!this.isAValidLocale(locale)) {
            throw new InvalidMessageBundleLocaleException(languageTag);
        }
        return locale;
    }

    private boolean isAValidLocale(final Locale l) {
        return !StringUtils.isEmpty(l.getCountry())
                && !StringUtils.isEmpty(l.getLanguage());
    }

    private ResourceBundle getValidResourceBundle(final String resourceBundleBaseName, final Locale locale) {
        String bundlePath = Optional.ofNullable(resourceBundleBaseName)
                .orElseThrow(MessageBundleBaseNameNotDefinedException::new);

        try {
            return ResourceBundle.getBundle(bundlePath, locale);
        } catch (Exception e) {
            throw new FailToCreateRoxanaResourceBundleException(e);
        }
    }

    protected String getInterpolationReplaceRegex(String s) {
        return INTERPOLATION_PREFIX_ESCAPED + s + INTERPOLATION_SUFFIX_ESCAPED;
    }

    protected String interpolateKey(ResourceBundle resourceBundle, Locale locale, String key, List<Parameter> parameters) {
        String interpolated = Arrays.stream(key.split(INTERPOLATION_REGEX))
                .distinct()
                .filter(message -> !message.isEmpty())
                .reduce(key, (k, message) -> {
                    String replaceRegex = this.getInterpolationReplaceRegex(message);
                    String messageInterpolated = this.interpolateMessage(resourceBundle, locale, message, parameters);
                    messageInterpolated = Matcher.quoteReplacement(messageInterpolated);
                    return k.replaceAll(replaceRegex, messageInterpolated);
                });

        // In case it has some parameter directly in the key.
        return this.interpolateParameters(locale, interpolated, parameters);
    }

    protected String interpolateMessage(ResourceBundle resourceBundle, Locale locale, String message, List<Parameter> parameters) {
        try {
            String translation = resourceBundle.getString(message);
            translation = this.interpolateParameters(locale, translation, parameters);
            return translation;
        } catch (Exception e) {
            if (!this.getSuppressFailToTranslateException()) {
                throw new FailToTranslateException(message, e);
            }
            LOG.error(ERROR_FAIL_TO_TRANSLATE_A_MESSAGE + message, e);
            return this.getNotFoundValue(message);
        }
    }

    protected String interpolateParameters(final Locale locale, String message, final List<Parameter> parameters) {
        for (Parameter p : parameters) {
            message = this.interpolateParameter(locale, message, p);
        }
        return message;
    }

    protected String getParameterInterpolationReplaceRegex(String s) {
        return PARAMETER_INTERPOLATION_PREFIX_ESCAPED + s + PARAMETER_INTERPOLATION_SUFFIX_ESCAPED;
    }

    protected String interpolateParameter(final Locale locale, final String message, final Parameter parameter) {
        String replaceRegex = this.getParameterInterpolationReplaceRegex(parameter.getName());
        String formattedValue = Matcher.quoteReplacement(parameter.getFormattedValue(locale));
        return message.replaceAll(replaceRegex, formattedValue);
    }

    protected String getNotFoundValue(String keyNotFound) {
        return NOT_FOUND_DELIMITER + keyNotFound + NOT_FOUND_DELIMITER;
    }

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    protected ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    private Boolean getSuppressFailToTranslateException() {
        return this.suppressFailToTranslateException;
    }

}