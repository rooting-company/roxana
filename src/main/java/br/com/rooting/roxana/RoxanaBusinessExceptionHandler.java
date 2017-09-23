package br.com.rooting.roxana;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.rooting.roxana.response.GenericResponse;
import br.com.rooting.roxana.response.processor.ResponseProcessorFactory;

/**
 * Convert no-treated exceptions in Roxana`s Framework standardized RESTful
 * responses.
 * 
 * <p>
 * <a href="https://spring.io/">The Spring framework</a> redirect any no-treated
 * {@link Exception} throws by the application, to specials classes annotated
 * with {@link RestControllerAdvice}, this is one of them.
 * </p>
 * 
 * <p>
 * The exceptions are treated by methods annotated with
 * {@link ExceptionHandler}}.
 * </p>
 * 
 * <p>
 * If you want to customize how a specific exception are treated, you must to
 * create your own {@link ControllerAdvice}} and specify the
 * {@link ExceptionHandler} methods.
 * </p>
 * 
 * <p>
 * It is important to say that if you want to customize the treatment of an
 * exception that is already treated by this handler, you must override the
 * methods in this class, providing with the spring annotations, already
 * mencioned.
 * </p>
 * 
 * @author Bruno Costa
 * @since 1.0
 * @see RestControllerAdvice
 * @see ExceptionHandler
 *
 */

@RestControllerAdvice
public class RoxanaBusinessExceptionHandler {

	@Autowired
	private ResponseProcessorFactory responseFactory;
	
	/**
	 * Method responsible to treat {@link Exception} that are thrown by an
	 * Spring application and it is not treated by any other specific
	 * {@link ExceptionHandler}.
	 * 
	 * To provide backward compatibility, exceptions annotated with
	 * {@link ResponseStatus} will not be affected by this method.
	 * 
	 * @param e
	 *            The no-treated {@link Exception} is redirected by
	 *            <a href="https://spring.io/">The Spring framework</a>.
	 * @return The standardized RESTful response is provided by the
	 *         {@link ResponseProcessorFactory}.
	 * @throws Exception
	 *             The process of treatment can throw exceptions depending on
	 *             how Roxana Framework is configured. If the exception is
	 *             annotated with {@link ResponseStatus} the exception is
	 *             re-thrown.
	 * 
	 * @see ExceptionHandler
	 */
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<GenericResponse<?>> processException(final Exception e) throws Exception {

		// Se tiver a annotation do spring deixa o framework spring tratar.
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
			throw e;
		}

		return this.getResponseFactory().getProcessedResponse(e);
	}
	
	/**
	 * Method responsible to treat {@link HttpMessageNotReadableException} that
	 * are thrown by The Spring framework, when the RESTful request is
	 * not well formatted.
	 * 
	 * @param e
	 *            The no-treated {@link HttpMessageNotReadableException} is redirected
	 *            by <a href="https://spring.io/">The Spring framework</a>.
	 * @return The Response is a Bad Request
	 *         {@link HttpStatus#BAD_REQUEST} with message describing the
	 *         specific problem.
	 * 
	 * @see ExceptionHandler
	 */
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<String> processHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest().body(e.getMostSpecificCause().getMessage());
	}

	/**
	 * Return the {@link Autowired} {@link ResponseProcessorFactory}.
	 * 
	 * @return ResponseProcessorFactory Return the factory responsible to create
	 *         the response Processors.
	 * 
	 * @see ResponseProcessorFactory
	 */
	
	protected ResponseProcessorFactory getResponseFactory() {
		return this.responseFactory;
	}

}