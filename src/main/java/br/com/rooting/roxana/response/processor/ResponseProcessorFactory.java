package br.com.rooting.roxana.response.processor;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.annotation.BusinessConstraintValidator;
import br.com.rooting.roxana.annotation.MultiBusinessException;
import br.com.rooting.roxana.response.GenericResponse;

@Component
public class ResponseProcessorFactory {

	@Autowired
	private BusinessExceptionResponseProcessor businessExceptionResponseProcessor;

	@Autowired
	private ConstraintValidatorResponseProcessor constraintValidatorResponseProcessor;

	@Autowired
	private MultiBusinessExceptionResponseProcessor multiBusinessExceptionResponseProcessor;
	
	@Autowired
	private BusinessConstraintValidatorResponseProcessor businessConstraintValidatorResponseProcessor;

	public ResponseProcessor getResponseProcessor(final Exception e) {
		if (e instanceof ConstraintViolationException) {
			if(AnnotationUtils.findAnnotation(e.getClass(), BusinessConstraintValidator.class) != null) {
				return this.getBusinessConstraintValidatorResponseProcessor();
			}
			
			return this.getConstraintValidatorResponseProcessor();
		}
		
		if (AnnotationUtils.findAnnotation(e.getClass(), MultiBusinessException.class) != null) {
			return this.getMultiBusinessExceptionResponseProcessor();
		}

		return this.getBusinessExceptionResponseProcessor();
	}

	public ResponseEntity<GenericResponse<?>> getProcessedResponse(final Exception e) throws Exception {
		return this.getResponseProcessor(e).process(e);
	}

	private BusinessExceptionResponseProcessor getBusinessExceptionResponseProcessor() {
		return this.businessExceptionResponseProcessor;
	}

	private ConstraintValidatorResponseProcessor getConstraintValidatorResponseProcessor() {
		return this.constraintValidatorResponseProcessor;
	}

	private MultiBusinessExceptionResponseProcessor getMultiBusinessExceptionResponseProcessor() {
		return this.multiBusinessExceptionResponseProcessor;
	}

	private BusinessConstraintValidatorResponseProcessor getBusinessConstraintValidatorResponseProcessor() {
		return this.businessConstraintValidatorResponseProcessor;
	}
	
}
