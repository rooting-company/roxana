package br.com.rooting.roxana.exception;

import br.com.rooting.roxana.exception.mapper.BusinessException;
import br.com.rooting.roxana.translator.Translator;
import org.springframework.http.HttpStatus;

@BusinessException(responseCode = HttpStatus.INTERNAL_SERVER_ERROR)
public class UnexpectedException extends Exception {

    public static final String ROXANA_KEY = Translator.getInterpolatedKeyOf(UnexpectedException.class.getName());

    private static final long serialVersionUID = 1L;

}