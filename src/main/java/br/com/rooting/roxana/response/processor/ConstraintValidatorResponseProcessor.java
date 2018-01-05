 package br.com.rooting.roxana.response.processor;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.message.MessageSeverity;
import br.com.rooting.roxana.message.builder.MessageBuilderFactory;
import br.com.rooting.roxana.response.parameter.finder.ConstraintValidationParameterFinder;
import br.com.rooting.roxana.response.parameter.finder.ParameterFinderStrategy;

@Primary
@Component
class ConstraintValidatorResponseProcessor extends ResponseProcessor {

	@Autowired
	ConstraintValidatorResponseProcessor(final RoxanaProperties roxanaProperties,
										 final MessageBuilderFactory messageFactory, 
									   	 final ResponseProcessorManager responseFactory) {
		
		super(roxanaProperties, messageFactory, responseFactory);
	}

	@Override
	protected Boolean isAUnexpectedException(Exception e) {
		return false;
	}
	
	@Override
	protected HttpStatus getResponseCode(Exception e) {
		return HttpStatus.UNPROCESSABLE_ENTITY;
	}
	
	@Override
	protected List<MessageResponseDTO> getMessagesResponseDTO(Exception e) {
		ConstraintViolationException constraintExpection = (ConstraintViolationException) e;
		return constraintExpection.getConstraintViolations().stream()
															.map(this::getMessageResponseDTO)
															.collect(Collectors.toList());
	}
	
	protected MessageResponseDTO getMessageResponseDTO(ConstraintViolation<?> c) {
		MessageResponseDTO dto = new MessageResponseDTO();
		dto.setKey(c.getMessageTemplate());
		dto.setSeverity(MessageSeverity.ERROR);
		dto.setParameters(this.getParameterFinderStrategy(c).findParameters());
		return dto;
	}
	
	protected ParameterFinderStrategy getParameterFinderStrategy(ConstraintViolation<?> c) {
		return new ConstraintValidationParameterFinder(c);
	}
	
}