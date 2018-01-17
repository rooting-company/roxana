package br.com.rooting.roxana.config;

import static br.com.rooting.roxana.config.RoxanaProperties.ROOT_NAME;
import static br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy.TRANSLATED;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.config.RoxanaProperties.Business.ExceptionHandler;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.message.MessageFully;
import br.com.rooting.roxana.message.MessageTranslated;
import br.com.rooting.roxana.message.MessageUnchanged;

// TODO Fazer com as constraints validations funcionem
// TODO Criar testes e exceptions para validar a classe.
@Component
@ConfigurationProperties(ROOT_NAME)
public final class RoxanaProperties {
	
	public static final String ROOT_NAME = "roxana";
	
	@Valid
	private Business business = new Business();

	@Valid
	private MessageBundle messageBundle = new MessageBundle();

	public Business getBusiness() {
		return this.business;
	}

	public MessageBundle getMessageBundle() {
		return this.messageBundle;
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
	
	public String getMessageBundlePath() {
		return this.getMessageBundle().getPath();
	}
	
	public String getMessageBundleLocale() {
		return this.getMessageBundle().getLocale();
	}
	
	public Boolean getMessageBundleSuppressFailsTranslations() {
		return this.getMessageBundle().getSuppressFailsTranslations();
	}
	
	public static class Business {
		
		@Valid
		@NotBlank
		private ResponseStrategy responseStrategy = TRANSLATED;
		
		@Valid
		@NotNull
		private ExceptionHandler exceptionHandler = new ExceptionHandler();
		
		Business() {
			super();
		}

		public ResponseStrategy getResponseStrategy() {
			return this.responseStrategy;
		}
		
		public void setResponseStrategy(ResponseStrategy responseStrategy) {
			this.responseStrategy = responseStrategy;
		}

		public ExceptionHandler getExceptionHandler() {
			return this.exceptionHandler;
		}
		
		public enum ResponseStrategy {
			
			FULLY(MessageFully.class), TRANSLATED(MessageTranslated.class), UNCHANGED(MessageUnchanged.class);
			
			private final Class<?> concreteMessageClass;
			
			private ResponseStrategy(final Class<?> concreteClass) {
				this.concreteMessageClass = concreteClass;
			}
			
			public Class<?> getConcreteMessageClass() {
				return this.concreteMessageClass;
			}
		}
		
		public static class ExceptionHandler {
			
			private Boolean suppressOthersExceptions = Boolean.TRUE;
			
			ExceptionHandler() {
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
		
		private static final String DEFAULT_MESSAGE_BUNDLE_PATH = "messages";
		
		public static final String MESSAGE_BUNDLE_PATH_PROPERTY = "roxana.message-bundle.path";
		
		@NotBlank
		private String path = DEFAULT_MESSAGE_BUNDLE_PATH;

		private String locale;

		private Boolean suppressFailsTranslations = true;
		
		MessageBundle() {
			super();
		}

		public String getPath() {
			return this.path;
		}

		public void setPath(String path) {
			this.path = path;
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