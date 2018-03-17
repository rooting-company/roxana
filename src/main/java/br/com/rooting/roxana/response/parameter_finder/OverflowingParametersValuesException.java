package br.com.rooting.roxana.response.parameter_finder;

public class OverflowingParametersValuesException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private static final String OVERFLOWING_PARAMETER_VALUES_FOR_THE_MESSAGE = "Overflowing parameter values for the message %s. "
																				+ "Expected %d parameters, obteting %d. values.";

	OverflowingParametersValuesException(final String messageKey, 
										 final Integer exceptedParameterQuantity,
										 final Integer parameterValueQuantity) {
		
		super(String.format(OVERFLOWING_PARAMETER_VALUES_FOR_THE_MESSAGE, messageKey, exceptedParameterQuantity, parameterValueQuantity));
	}

}