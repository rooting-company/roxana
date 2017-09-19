package br.com.rooting.roxana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.rooting.roxana.response.GenericResponse;
import br.com.rooting.roxana.response.processor.ResponseProcessorFactory;

@RestControllerAdvice
public class RoxanaBusinessExceptionHandler {

	@Autowired
	private ResponseProcessorFactory responseFactory;
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<GenericResponse<?>> processException(final Exception e) throws Exception {

		// Se tiver a annotation do spring deixa o framework spring tratar.
		// Se estiver configurado para não supender lanca o erro.
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}

		return this.getResponseFactory().getProcessedResponse(e);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<String> processHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest().body(e.getMostSpecificCause().getMessage());
	}

	protected ResponseProcessorFactory getResponseFactory() {
		return this.responseFactory;
	}

}
