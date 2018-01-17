 package br.com.rooting.roxana.response.processor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.business.BusinessException;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.response.parameterFinder.GenericParameterFinder;
import br.com.rooting.roxana.response.parameterFinder.ParameterFinderStrategy;
import br.com.rooting.roxana.translator.Translator;

@Primary
@Component
class BusinessExceptionResponseProcessor extends ResponseProcessor {

	@Autowired
	BusinessExceptionResponseProcessor(final RoxanaProperties roxanaProperties, 
									   final MessageCreatorFactory messageCreatorFactory, 
									   final ResponseProcessorManager responseCreatorFactory) {

				super(roxanaProperties, messageCreatorFactory, responseCreatorFactory);
	}

	@Override
	protected Boolean isAUnexpectedException(Exception e) {
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
							.orElse(Translator.getInterpoledKeyOf(e.getClass().getCanonicalName()));
		message.setKey(messageKey);
		message.setSeverity(businessAnnotation.severity());
		
		ParameterFinderStrategy parameterFinder = new GenericParameterFinder(e);
		message.setParameters(parameterFinder.findParameters());
		return message;
	}
	
}