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
import br.com.rooting.roxana.message.builder.MessageBuilder;
import br.com.rooting.roxana.message.builder.MessageBuilderFactory;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.response.ResponseBuilder;

abstract class ResponseProcessor {
	
	private static final String ERROR_INTERN_EXCEPTION_HANDLED = "Intern exception handled by {0}:";
	
	private final Logger log;
	
	private final String errorInternHandledMessage;
	
	private final Boolean suppressOthersExceptions;
	
	private final MessageBuilderFactory messageBuilderFactory;

	private final ResponseProcessorManager responseProcessorManager;
	
	ResponseProcessor(final RoxanaProperties roxanaProperties,
					  final MessageBuilderFactory messageBuilderFactory,
					  final ResponseProcessorManager responseProcessorManager) {
		
		this.log = LoggerFactory.getLogger(this.getClass());
		this.errorInternHandledMessage = MessageFormat.format(ERROR_INTERN_EXCEPTION_HANDLED, this.getClass().getCanonicalName());
		this.suppressOthersExceptions = roxanaProperties.getBusinessExceptionHandlerSuppressOthersExceptions();
		this.messageBuilderFactory = messageBuilderFactory;
		this.responseProcessorManager = responseProcessorManager;
	}
	
	protected abstract Boolean isAUnexpectedException(Exception e);
	
	protected abstract HttpStatus getResponseCode(Exception e);
	
	protected abstract List<MessageResponseDTO> getMessagesResponseDTO(Exception e);
	
	ResponseEntity<Response<Message>> process(final Exception e) throws Exception {
		if(!this.isAUnexpectedException(e)) {
			return this.formatResponse(this.getResponseCode(e), this.getMessagesResponseDTO(e));
		} else if (!this.getSuppressOthersExceptions()) {
			throw e;
		}
		
		// Se não tem, loga o erro real e retorna um erro generico.
		this.getLog().error(this.getErrorInternHandledMessage(), e);
		return this.getResponseProcessorFactory().getProcessedResponse(new UnexpectedException());
	}
	
	private ResponseEntity<Response<Message>> formatResponse(final HttpStatus responseCode,
													   final List<MessageResponseDTO> messagesResponseDTO) {
		
		return ResponseEntity.status(responseCode).body(this.getResponse(messagesResponseDTO));
	}
	
	private Response<Message> getResponse(final List<MessageResponseDTO> messagesResponseDTO) {
		return ResponseBuilder.buildWith(this.transformIntoMessages(messagesResponseDTO));
	}
	
	private List<Message> transformIntoMessages(final List<MessageResponseDTO> messagesResponseDTO) {
		return messagesResponseDTO.stream()
									.map(m -> this.getMessageBuilder().build(m, m.getParameters()))
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
	
	private MessageBuilder getMessageBuilder() {
		return this.getMessageBuilderFactory().getMessageBuilder();
	}
	
	private MessageBuilderFactory getMessageBuilderFactory() {
		return this.messageBuilderFactory;
	}
	
	private ResponseProcessorManager getResponseProcessorFactory() {
		return this.responseProcessorManager;
	}
	
}