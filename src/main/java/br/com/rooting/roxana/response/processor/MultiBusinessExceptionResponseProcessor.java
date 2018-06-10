package br.com.rooting.roxana.response.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.exception.mapper.BusinessException;
import br.com.rooting.roxana.exception.mapper.MultiBusinessException;
import br.com.rooting.roxana.message.MessageCreatorFactory;

class MultiBusinessExceptionResponseProcessor extends BusinessExceptionResponseProcessor {
	
	MultiBusinessExceptionResponseProcessor(final RoxanaProperties roxanaProperties,
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
		return AnnotationUtils.findAnnotation(e.getClass(), MultiBusinessException.class).responseCode();
	}
	
	@Override
	protected List<MessageResponseDTO> getMessagesResponseDTO(Exception e) {
		List<MessageResponseDTO> messagesDTO = new ArrayList<>();
		Stream.of(e.getSuppressed()).forEach(s -> {
			if(s instanceof Exception) {
				if(AnnotationUtils.findAnnotation(s.getClass(), BusinessException.class) != null) {
					messagesDTO.add(super.getMessageResponseDTO((Exception) s));
				}
			}
		});
		return messagesDTO;
	}

}