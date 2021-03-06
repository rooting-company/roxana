package br.com.rooting.roxana.response.processor;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.exception.mapper.BusinessException;
import br.com.rooting.roxana.exception.mapper.MultiBusinessException;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;

@Component
public class ResponseProcessorManager {

    private final BusinessExceptionResponseProcessor businessExceptionRP;

    private final ConstraintValidatorResponseProcessor constraintValidatorRP;

    private final MultiBusinessExceptionResponseProcessor multiBusinessExceptionRP;

    @Autowired
    ResponseProcessorManager(final RoxanaProperties roxanaProperties,
                             final MessageCreatorFactory messageCreatorFactory) {

        if (roxanaProperties == null || messageCreatorFactory == null) {
            throw new IllegalArgumentException();
        }

        businessExceptionRP = new BusinessExceptionResponseProcessor(roxanaProperties, messageCreatorFactory, this);
        constraintValidatorRP = new ConstraintValidatorResponseProcessor(roxanaProperties, messageCreatorFactory, this);
        multiBusinessExceptionRP = new MultiBusinessExceptionResponseProcessor(roxanaProperties, messageCreatorFactory, this);
    }

    // "Factory Method"
    public ResponseEntity<Response> getProcessedResponse(final Exception exception) throws Exception {

        if (exception instanceof ConstraintViolationException) {
            return this.getConstraintValidatorRP().process(exception);
        }

        if (AnnotationUtils.findAnnotation(exception.getClass(), MultiBusinessException.class) != null) {
            return this.getMultiBusinessExceptionRP().process(exception);
        }

        if (AnnotationUtils.findAnnotation(exception.getClass(), BusinessException.class) != null) {
            return this.getBusinessExceptionRP().process(exception);
        }

        return this.getBusinessExceptionRP().process(this.getRealException(exception));
    }

    private Exception getRealException(final Exception exception) {

        // Frameworks as hibernate use disguised checked exceptions.
        // Lambda does not support checked exceptions, so the disguised of checked exception is used a lot.
        if (exception instanceof RuntimeException && exception.getCause() != null) {
            if (exception.getCause() instanceof Exception) {
                return (Exception) exception.getCause();
            }
        }
        return exception;
    }

    private BusinessExceptionResponseProcessor getBusinessExceptionRP() {
        return this.businessExceptionRP;
    }

    private ConstraintValidatorResponseProcessor getConstraintValidatorRP() {
        return this.constraintValidatorRP;
    }

    private MultiBusinessExceptionResponseProcessor getMultiBusinessExceptionRP() {
        return this.multiBusinessExceptionRP;
    }

}