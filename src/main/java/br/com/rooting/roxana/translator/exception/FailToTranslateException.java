package br.com.rooting.roxana.translator.exception;

public class FailToTranslateException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String MESSAGE = "Failed to translate the following message: ";

	public FailToTranslateException(String key, Exception e) {
		super(MESSAGE + key, e);
	}

}