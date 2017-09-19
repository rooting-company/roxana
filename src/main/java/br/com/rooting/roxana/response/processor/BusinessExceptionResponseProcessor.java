 package br.com.rooting.roxana.response.processor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.RoxanaProperties;
import br.com.rooting.roxana.annotation.BusinessException;
import br.com.rooting.roxana.response.creator.MessageCreatorFactory;
import br.com.rooting.roxana.response.parameter.finder.GenericParameterFinder;
import br.com.rooting.roxana.response.parameter.finder.ParameterFinderStrategy;
import br.com.rooting.roxana.translator.Translator;

@Component
public class BusinessExceptionResponseProcessor extends ResponseProcessor {

	@Autowired
	BusinessExceptionResponseProcessor(final RoxanaProperties roxanaProperties, 
									   final MessageCreatorFactory responseAbstractFactory, 
									   final ResponseProcessorFactory factory) {

				super(roxanaProperties, responseAbstractFactory, factory);
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
		message.setMessageKey(messageKey);
		message.setSeverity(businessAnnotation.severity());
		
		ParameterFinderStrategy parameterFinder = new GenericParameterFinder(e);
		message.setParameters(parameterFinder.findParameters());
		return message;
	}
	
}
