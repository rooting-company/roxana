package br.com.rooting.roxana.parameter;

public class UnsupportedParameterConversionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "Unsupported %s Parameter conversion to %s.class.";

    UnsupportedParameterConversionException(final ParameterType type, final Object unsupportedClass) {
        super(String.format(MESSAGE, type.name(), unsupportedClass.getClass().getCanonicalName()));
    }

}