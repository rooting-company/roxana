package br.com.rooting.roxana.response.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.business.BusinessException;
import br.com.rooting.roxana.business.MultiBusinessException;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.message.builder.MessageBuilderFactory;

@Component
class MultiBusinessExceptionResponseProcessor extends BusinessExceptionResponseProcessor {
	
	@Autowired
	MultiBusinessExceptionResponseProcessor(final RoxanaProperties roxanaProperties,
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
			this.getLog().error(this.getErrorInternHandledMessage(), s);
			
		});
		return messagesDTO;
	}

}