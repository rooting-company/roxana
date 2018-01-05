package br.com.rooting.roxana.response.processor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.RoxanaProperties;
import br.com.rooting.roxana.annotation.BusinessException;
import br.com.rooting.roxana.annotation.MultiBusinessException;
import br.com.rooting.roxana.response.creator.MessageCreatorFactory;

@Component
class MultiBusinessExceptionResponseProcessor extends BusinessExceptionResponseProcessor {
	
	@Autowired
	MultiBusinessExceptionResponseProcessor(final RoxanaProperties roxanaProperties,
											final MessageCreatorFactory messageFactory, 
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
	
	// TODO Simplificar
	@Override
	protected List<MessageResponseDTO> getMessagesResponseDTO(Exception e) {
		return Stream.of(e.getSuppressed())
					.map(t -> {
						if(t instanceof Exception) {
							if(AnnotationUtils.findAnnotation(t.getClass(), BusinessException.class) != null) {
								return super.getMessageResponseDTO((Exception) t);
							}
						}
						this.getLog().error(this.getErrorInternHandledMessage(), t);
						return null;
					})
					.filter(Objects::nonNull)
					.collect(Collectors.toList());
	}

}