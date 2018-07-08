package br.com.rooting.roxana.config;

import br.com.rooting.roxana.config.RoxanaProperties.Business.ExceptionHandler;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.message.MessageFully;
import br.com.rooting.roxana.message.MessageTranslated;
import br.com.rooting.roxana.message.MessageUnchanged;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy.TRANSLATED;
import static br.com.rooting.roxana.config.RoxanaProperties.ROOT_NAME;

// TODO Create tests and exceptions to validate this class.
@Component
@ConfigurationProperties(ROOT_NAME)
public final class RoxanaProperties {

    public static final String ROOT_NAME = "roxana";

    @Valid
    private Business business = new Business();

    @Valid
    private MessageBundle messageBundle = new MessageBundle();

    private Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    private MessageBundle getMessageBundle() {
        return this.messageBundle;
    }

    public void setMessageBundle(MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    private ExceptionHandler getBusinessExceptionHandler() {
        return this.getBusiness().getExceptionHandler();
    }

    public Boolean getBusinessExceptionHandlerSuppressOthersExceptions() {
        return this.getBusinessExceptionHandler().getSuppressOthersExceptions();
    }

    public ResponseStrategy getBusinessResponseStrategy() {
        return this.getBusiness().getResponseStrategy();
    }

    public String getMessageBundleBaseName() {
        return this.getMessageBundle().getBaseName();
    }

    public String getMessageBundleLocale() {
        return this.getMessageBundle().getLocale();
    }

    public Boolean getMessageBundleSuppressFailsTranslations() {
        return this.getMessageBundle().getSuppressFailsTranslations();
    }

    public static class Business {

        @Valid
        @NotNull
        private final ExceptionHandler exceptionHandler = new ExceptionHandler();

        @Valid
        @NotBlank
        private ResponseStrategy responseStrategy = TRANSLATED;

        public Business() {
            super();
        }

        public ResponseStrategy getResponseStrategy() {
            return this.responseStrategy;
        }

        public void setResponseStrategy(ResponseStrategy responseStrategy) {
            this.responseStrategy = responseStrategy;
        }

        ExceptionHandler getExceptionHandler() {
            return this.exceptionHandler;
        }

        public enum ResponseStrategy {

            FULLY(MessageFully.class), TRANSLATED(MessageTranslated.class), UNCHANGED(MessageUnchanged.class);

            private final Class<?> concreteMessageClass;

            ResponseStrategy(final Class<?> concreteClass) {
                this.concreteMessageClass = concreteClass;
            }

            public Class<?> getConcreteMessageClass() {
                return this.concreteMessageClass;
            }
        }

        public static class ExceptionHandler {

            private Boolean suppressOthersExceptions = Boolean.TRUE;

            public ExceptionHandler() {
                super();
            }

            public Boolean getSuppressOthersExceptions() {
                return suppressOthersExceptions;
            }

            public void setSuppressOthersExceptions(Boolean suppressOthersExceptions) {
                this.suppressOthersExceptions = suppressOthersExceptions;
            }

        }
    }

    public static class MessageBundle {

        public static final String MESSAGE_BUNDLE_BASE_NAME_PROPERTY = "roxana.message-bundle.base-name";
        public static final String MESSAGE_BUNDLE_LOCALE_PROPERTY = "roxana.message-bundle.locale";
        private static final String DEFAULT_MESSAGE_BUNDLE_BASE_NAME = "messages";

        @NotBlank
        private String baseName = DEFAULT_MESSAGE_BUNDLE_BASE_NAME;

        private String locale;

        private Boolean suppressFailsTranslations = true;

        public MessageBundle() {
            super();
        }

        public String getBaseName() {
            return this.baseName;
        }

        public void setBaseName(String baseName) {
            this.baseName = baseName;
        }

        public String getLocale() {
            return this.locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public Boolean getSuppressFailsTranslations() {
            return this.suppressFailsTranslations;
        }

        public void setSuppressFailsTranslations(Boolean suppressFailsTranslations) {
            this.suppressFailsTranslations = suppressFailsTranslations;
        }

    }

}