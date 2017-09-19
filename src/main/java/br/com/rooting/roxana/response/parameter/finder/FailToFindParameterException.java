package br.com.rooting.roxana.response.parameter.finder;

public class FailToFindParameterException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	FailToFindParameterException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
