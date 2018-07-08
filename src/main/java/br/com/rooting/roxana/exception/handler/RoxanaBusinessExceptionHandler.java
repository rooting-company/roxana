package br.com.rooting.roxana.exception.handler;

import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.response.processor.ResponseProcessorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Convert no-treated exceptions in Roxana's Framework standardized RESTFUL
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
 * methods in this class, providing with the spring annotations, already mentioned.
 * </p>
 *
 * @author Bruno Costa
 * @see RestControllerAdvice
 * @see ExceptionHandler
 * @since 1.0
 */

@RestControllerAdvice
public class RoxanaBusinessExceptionHandler {

    @Autowired
    private ResponseProcessorManager responseProcessorManager;

    /**
     * Method responsible to treat {@link Exception} that are thrown by an
     * Spring application and it is not treated by any other specific
     * {@link ExceptionHandler}.
     * <p>
     * To provide backward compatibility, exceptions annotated with
     * {@link ResponseStatus} will not be affected by this method.
     *
     * @param e The no-treated {@link Exception} is redirected by
     *          <a href="https://spring.io/">The Spring framework</a>.
     * @return The standardized rest response is provided by the
     * {@link ResponseProcessorManager}.
     * @throws Exception The process of treatment can throw exceptions depending on
     *                   how Roxana Framework is configured. If the exception is
     *                   annotated with {@link ResponseStatus} the exception is
     *                   re-thrown.
     * @see ExceptionHandler
     */

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Response> processException(final Exception e) throws Exception {

        // If it is a spring exception, the treatment is delegated to Spring Framework.
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        return this.getResponseProcessorManager().getProcessedResponse(e);
    }

    /**
     * Method responsible to treat {@link HttpMessageNotReadableException} that
     * are thrown by The Spring framework, when the RESTful request is
     * not well formatted.
     *
     * @param e The no-treated {@link HttpMessageNotReadableException} is redirected
     *          by <a href="https://spring.io/">The Spring framework</a>.
     * @return The Response is a Bad Request
     * {@link HttpStatus#BAD_REQUEST} with message describing the
     * specific problem.
     * @see ExceptionHandler
     */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<String> processHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(e.getMostSpecificCause().getMessage());
    }

    /**
     * Return the {@link Autowired} {@link ResponseProcessorManager}.
     *
     * @return ResponseProcessorFactory Return the factory responsible to create
     * the response Processors.
     * @see ResponseProcessorManager
     */

    protected ResponseProcessorManager getResponseProcessorManager() {
        return this.responseProcessorManager;
    }

}