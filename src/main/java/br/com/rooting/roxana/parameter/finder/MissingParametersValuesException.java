package br.com.rooting.roxana.parameter.finder;

public class MissingParametersValuesException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String MISSING_PARAMETER_VALUES_FOR_THE_MESSAGE = "Missing parameter values for the message %s. "
            + "Expected %d parameters, obtained %d. values.";

    MissingParametersValuesException(final String messageKey,
                                     final Integer exceptedParameterQuantity,
                                     final Integer parameterValueQuantity) {

        super(String.format(MISSING_PARAMETER_VALUES_FOR_THE_MESSAGE, messageKey, exceptedParameterQuantity, parameterValueQuantity));
    }

}