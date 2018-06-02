package br.com.rooting.roxana.response.processor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;

import br.com.rooting.roxana.business.BusinessException;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.parameter.finder.GenericParameterFinder;
import br.com.rooting.roxana.parameter.finder.ParameterFinderStrategy;
import br.com.rooting.roxana.translator.Translator;

class BusinessExceptionResponseProcessor extends ResponseProcessor {

	BusinessExceptionResponseProcessor(final RoxanaProperties roxanaProperties, 
									   final MessageCreatorFactory messageCreatorFactory, 
									   final ResponseProcessorManager responseCreatorManager) {

		super(roxanaProperties, messageCreatorFactory, responseCreatorManager);
	}

	@Override
	protected Boolean isUnexpectedException(Exception e) {
		return AnnotationUtils.findAnnotation(e.getClass(), BusinessException.class) == null;
	}
	
	@Override
	protected HttpStatus getResponseCode(Exception e) {
		return AnnotationUtils.findAnnotation(e.getClass(), BusinessException.class).responseCode();
	}
	
	@Override
	protected List<MessageResponseDTO> getMessagesResponseDTO(Exception e) {
		return Stream.of(this.getMessageResponseDTO(e))
					 .collect(Collectors.toList());
	}
	
	protected MessageResponseDTO getMessageResponseDTO(Exception e) {
		BusinessException businessAnnotation = AnnotationUtils.findAnnotation(e.getClass(), BusinessException.class);
		MessageResponseDTO message = new MessageResponseDTO();
		String messageKey = Optional.of(businessAnnotation.message())
									.filter(m -> !BusinessException.MESSAGE_NOT_DEFINED.equals(m))
									.orElse(Translator.getInterpoledKeyOf(e.getClass().getName()));
		message.setKey(messageKey);
		message.setSeverity(businessAnnotation.severity());
		
		ParameterFinderStrategy parameterFinder = new GenericParameterFinder(e);
		message.setParameters(parameterFinder.findParameters());
		return message;
	}
	
}