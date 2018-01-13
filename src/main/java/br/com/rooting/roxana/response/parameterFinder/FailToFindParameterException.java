package br.com.rooting.roxana.response.parameterFinder;

public class FailToFindParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	FailToFindParameterException(final String message, final Throwable cause) {
		super(message, cause);
	}

}