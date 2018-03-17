package br.com.rooting.roxana.translator;

public class FailToTranslateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "Failed to translate the following message \"%s\"";

	FailToTranslateException(String key, Exception e) {
		super(String.format(MESSAGE, key), e);
	}

}