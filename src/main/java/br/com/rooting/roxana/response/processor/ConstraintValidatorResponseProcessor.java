 package br.com.rooting.roxana.response.processor;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.message.MessageSeverity;
import br.com.rooting.roxana.parameter.finder.ConstraintValidationParameterFinder;
import br.com.rooting.roxana.parameter.finder.ParameterFinderStrategy;

class ConstraintValidatorResponseProcessor extends ResponseProcessor {

	ConstraintValidatorResponseProcessor(final RoxanaProperties roxanaProperties,
										 final MessageCreatorFactory messageCreatorFactory, 
									   	 final ResponseProcessorManager responseCreatorManager) {
		
		super(roxanaProperties, messageCreatorFactory, responseCreatorManager);
	}

	@Override
	protected Boolean isUnexpectedException(Exception e) {
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