package br.com.rooting.roxana;

import static br.com.rooting.roxana.RoxanaProperties.ROOT_NAME;
import static br.com.rooting.roxana.RoxanaProperties.Business.ResponseEstrategy.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.RoxanaProperties.Business.ExceptionHandler;
import br.com.rooting.roxana.RoxanaProperties.Business.ResponseEstrategy;

@Component
@ConfigurationProperties(ROOT_NAME)
public final class RoxanaProperties {
	
	public static final String ROOT_NAME = "roxana";

	private Business business = new Business();

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
	
	public ResponseEstrategy getBusinessResponseEstrategy() {
		return this.getBusiness().getResponseEstrategy();
	}
	
	public String getMessageBundlePath() {
		return this.getMessageBundle().getPath();
	}
	
	public String getMessageBundleLocale() {
		return this.getMessageBundle().getLocale();
	}
	
	public Boolean getMessageBundleSuppressFailToTranslateException() {
		return this.getMessageBundle().getSuppressFailToTranslateException();
	}
	
	public static class Business {
		
		private ResponseEstrategy responseEstrategy = TRANSLATED;
		
		private ExceptionHandler exceptionHandler = new ExceptionHandler();

		public ResponseEstrategy getResponseEstrategy() {
			return responseEstrategy;
		}

		public void setResponseEstrategy(ResponseEstrategy responseEstrategy) {
			this.responseEstrategy = responseEstrategy;
		}

		public ExceptionHandler getExceptionHandler() {
			return exceptionHandler;
		}
		
		public enum ResponseEstrategy {
			FULLY, TRANSLATED, UNCHANGED
		}
		
		public static class ExceptionHandler {

			private Boolean suppressOthersExceptions = true;
			
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

		private String path = DEFAULT_MESSAGE_BUNDLE_PATH;

		private String locale;

		private Boolean suppressFailToTranslateException = true;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getLocale() {
			return locale;
		}

		public void setLocale(String locale) {
			this.locale = locale;
		}

		public Boolean getSuppressFailToTranslateException() {
			return suppressFailToTranslateException;
		}

		public void setSuppressFailToTranslateException(Boolean suppressFailToTranslateException) {
			this.suppressFailToTranslateException = suppressFailToTranslateException;
		}
	}

}
