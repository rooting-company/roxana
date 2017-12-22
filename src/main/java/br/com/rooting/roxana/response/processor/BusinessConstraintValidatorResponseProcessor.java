package br.com.rooting.roxana.response.processor;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.RoxanaProperties;
import br.com.rooting.roxana.response.creator.MessageCreatorFactory;
import br.com.rooting.roxana.response.parameter.finder.GenericParameterFinder;
import br.com.rooting.roxana.response.parameter.finder.ParameterFinderStrategy;

@Component
public class BusinessConstraintValidatorResponseProcessor extends ConstraintValidatorResponseProcessor {

	@Autowired
	BusinessConstraintValidatorResponseProcessor(final RoxanaProperties roxanaProperties,
												 final MessageCreatorFactory messageFactory, 
												 final ResponseProcessorFactory responseFactory) {
		super(roxanaProperties, messageFactory, responseFactory);
	}
	
	@Override
	protected ParameterFinderStrategy getParameterFinderStrategy(ConstraintViolation<?> c) {
		return new GenericParameterFinder(c);
	}

}