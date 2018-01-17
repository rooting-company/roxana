package br.com.rooting.roxana.response.processor;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.rooting.roxana.business.exception.UnexpectedException;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.message.Message;
import br.com.rooting.roxana.message.MessageCreator;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.response.ResponseBuilder;

abstract class ResponseProcessor {
	
	private static final String ERROR_INTERN_EXCEPTION_HANDLED = "Intern exception handled by {0}:";
	
	private final Logger log;
	
	private final String errorInternHandledMessage;
	
	private final Boolean suppressOthersExceptions;
	
	private final MessageCreatorFactory messageCreatorFactory;

	private final ResponseProcessorManager responseProcessorManager;
	
	ResponseProcessor(final RoxanaProperties roxanaProperties,
					  final MessageCreatorFactory messageCreatorFactory,
					  final ResponseProcessorManager responseProcessorManager) {
		
		this.log = LoggerFactory.getLogger(this.getClass());
		this.errorInternHandledMessage = MessageFormat.format(ERROR_INTERN_EXCEPTION_HANDLED, this.getClass().getCanonicalName());
		this.suppressOthersExceptions = roxanaProperties.getBusinessExceptionHandlerSuppressOthersExceptions();
		this.messageCreatorFactory = messageCreatorFactory;
		this.responseProcessorManager = responseProcessorManager;
	}
	
	protected abstract Boolean isAUnexpectedException(Exception e);
	
	protected abstract HttpStatus getResponseCode(Exception e);
	
	protected abstract List<MessageResponseDTO> getMessagesResponseDTO(Exception e);
	
	ResponseEntity<Response> process(final Exception e) throws Exception {
		if(!this.isAUnexpectedException(e)) {
			return this.formatResponse(this.getResponseCode(e), this.getMessagesResponseDTO(e));
		} else if (!this.getSuppressOthersExceptions()) {
			throw e;
		}
		
		// Se não tem, loga o erro real e retorna um erro generico.
		this.getLog().error(this.getErrorInternHandledMessage(), e);
		return this.getResponseProcessorFactory().getProcessedResponse(new UnexpectedException());
	}
	
	private ResponseEntity<Response> formatResponse(final HttpStatus responseCode,
													   final List<MessageResponseDTO> messagesResponseDTO) {
		
		return ResponseEntity.status(responseCode).body(this.getResponse(messagesResponseDTO));
	}
	
	private Response getResponse(final List<MessageResponseDTO> messagesResponseDTO) {
		return ResponseBuilder.buildWith(this.transformIntoMessages(messagesResponseDTO));
	}
	
	private List<Message> transformIntoMessages(final List<MessageResponseDTO> messagesResponseDTO) {
		return messagesResponseDTO.stream()
									.map(m -> this.getMessageCreator().create(m, m.getParameters()))
									.collect(Collectors.toList());
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
	
	private MessageCreator getMessageCreator() {
		return this.getMessageCreatorFactory().getMessageCreator();
	}
	
	private MessageCreatorFactory getMessageCreatorFactory() {
		return this.messageCreatorFactory;
	}
	
	private ResponseProcessorManager getResponseProcessorFactory() {
		return this.responseProcessorManager;
	}
	
}