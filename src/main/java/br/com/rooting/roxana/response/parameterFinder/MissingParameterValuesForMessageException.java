package br.com.rooting.roxana.response.parameterFinder;

public class MissingParameterValuesForMessageException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private static final String ERROR_MISSING_PARAMETER_VALUES_FOR_THE_MESSAGE = "Missing parameter values for the message: ";

	MissingParameterValuesForMessageException(final String messageKey) {
		super(ERROR_MISSING_PARAMETER_VALUES_FOR_THE_MESSAGE + messageKey);
	}
	
}