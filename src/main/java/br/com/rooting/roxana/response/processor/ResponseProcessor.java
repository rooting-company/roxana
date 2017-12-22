package br.com.rooting.roxana.response.processor;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.rooting.roxana.RoxanaProperties;
import br.com.rooting.roxana.business.exception.UnexpectedException;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.response.ResponseBuilder;
import br.com.rooting.roxana.response.creator.MessageCreator;
import br.com.rooting.roxana.response.creator.MessageCreatorFactory;

public abstract class ResponseProcessor {
	
	private static final String ERROR_INTERN_EXCEPTION_HANDLED = "Intern exception handled by {0}:";
	
	private final Logger log;
	
	private final String errorInternHandledMessage;
	
	private final Boolean suppressOthersExceptions;
	
	private final MessageCreatorFactory messageFactory;

	private final ResponseProcessorFactory responseFactory;
	
	protected ResponseProcessor(final RoxanaProperties roxanaProperties,
								final MessageCreatorFactory messageFactory,
								final ResponseProcessorFactory responsefactory) {
		
		this.log = LoggerFactory.getLogger(this.getClass());
		this.errorInternHandledMessage = MessageFormat.format(ERROR_INTERN_EXCEPTION_HANDLED, this.getClass().getCanonicalName());
		this.suppressOthersExceptions = roxanaProperties.getBusinessExceptionHandlerSuppressOthersExceptions();
		this.messageFactory = messageFactory;
		this.responseFactory = responsefactory;
	}
	
	protected abstract Boolean isAUnexpectedException(Exception e);
	
	protected abstract HttpStatus getResponseCode(Exception e);
	
	protected abstract List<MessageResponseDTO> getMessagesResponseDTO(Exception e);
	
	public ResponseEntity<Response<?>> process(Exception e) throws Exception {
		if(!this.isAUnexpectedException(e)) {
			return this.formatResponse(this.getResponseCode(e), this.getMessagesResponseDTO(e));
		} else if (!this.getSuppressOthersExceptions()) {
			throw e;
		}
		
		// Se não tem, loga o erro real e retorna um erro generico.
		this.getLog().error(this.getErrorInternHandledMessage(), e);
		return this.getResponseFactory().getProcessedResponse(new UnexpectedException());
	}
	
	private ResponseEntity<Response<?>> formatResponse(HttpStatus responseCode, List<MessageResponseDTO> messagesResponseDTO) {
		MessageCreator messageCreator = this.getMessageFactory().getMessageCreator();
		return ResponseEntity
				.status(responseCode)
				.body(ResponseBuilder.buildWith(messagesResponseDTO
														.stream()
														.map(m -> messageCreator.create(m, m.getParameters()))
														.collect(Collectors.toList())
													 )
												);
	}

	protected Logger getLog() {
		return this.log;
	}
	
	protected String getErrorInternHandledMessage() {
		return this.errorInternHandledMessage;
	}
	
	private Boolean getSuppressOthersExceptions() {
		return this.suppressOthersExceptions;
	}
	
	private MessageCreatorFactory getMessageFactory() {
		return this.messageFactory;
	}
	
	private ResponseProcessorFactory getResponseFactory() {
		return this.responseFactory;
	}
	
}