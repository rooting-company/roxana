package br.com.rooting.roxana.business.exception;

import org.springframework.http.HttpStatus;

import br.com.rooting.roxana.annotation.BusinessException;

@BusinessException(responseCode = HttpStatus.INTERNAL_SERVER_ERROR)
public class UnexpectedException extends Exception {

	private static final long serialVersionUID = 1L;

}