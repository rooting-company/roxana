package br.com.rooting.roxana.response.processor;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.response.parameterFinder.GenericParameterFinder;
import br.com.rooting.roxana.response.parameterFinder.ParameterFinderStrategy;

@Component
class BusinessConstraintValidatorResponseProcessor extends ConstraintValidatorResponseProcessor {

	@Autowired
	BusinessConstraintValidatorResponseProcessor(final RoxanaProperties roxanaProperties,
												 final MessageCreatorFactory messageCreatorFactory, 
												 final ResponseProcessorManager responseProcessorFactory) {
		super(roxanaProperties, messageCreatorFactory, responseProcessorFactory);
	}
	
	@Override
	protected ParameterFinderStrategy getParameterFinderStrategy(ConstraintViolation<?> c) {
		return new GenericParameterFinder(c);
	}

}