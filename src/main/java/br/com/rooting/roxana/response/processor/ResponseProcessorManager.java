package br.com.rooting.roxana.response.processor;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.annotation.BusinessConstraintValidator;
import br.com.rooting.roxana.annotation.MultiBusinessException;
import br.com.rooting.roxana.response.Response;

@Component
public class ResponseProcessorManager {

	@Autowired
	private BusinessExceptionResponseProcessor businessExceptionRP;

	@Autowired
	private ConstraintValidatorResponseProcessor constraintValidatorRP;

	@Autowired
	private MultiBusinessExceptionResponseProcessor multiBusinessExceptionRP;
	
	@Autowired
	private BusinessConstraintValidatorResponseProcessor businessConstraintValidatorRP;

	// Internal Factory Method
	private ResponseProcessor getResponseProcessor(final Exception e) {
		
		if (e instanceof ConstraintViolationException) {
			if(AnnotationUtils.findAnnotation(e.getClass(), BusinessConstraintValidator.class) != null) {
				return this.getBusinessConstraintValidatorRP();
			}
			
			return this.getConstraintValidatorRP();
		}
		
		if (AnnotationUtils.findAnnotation(e.getClass(), MultiBusinessException.class) != null) {
			return this.getMultiBusinessExceptionRP();
		}

		return this.getBusinessExceptionRP();
	}
	
	// Unico metodo externo.
	public ResponseEntity<Response<?>> getProcessedResponse(Exception e) throws Exception {
		e = this.getRealException(e);
		return this.getResponseProcessor(e).process(e);
	}
	
	private Exception getRealException(final Exception e) {
		
		// Frameworks como hibernate utilizam de mascaramento de checked exception.
		// Lambda não suporta checked exceptions, portanto mascamento de checked 
		// exceptions também é bastante usado.
		if (e instanceof RuntimeException && e.getCause() != null) {
			if (e.getCause() instanceof Exception) {
				return (Exception) e.getCause();
			}
		}
		return e;
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

	private BusinessConstraintValidatorResponseProcessor getBusinessConstraintValidatorRP() {
		return this.businessConstraintValidatorRP;
	}
	
}
