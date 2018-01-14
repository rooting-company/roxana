package br.com.rooting.roxana.message.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseEstrategy;

@Component
public class MessageBuilderFactory {
	
	private final ResponseEstrategy responseEstrategy;
	
	private final MessageFullyBuilder fullyBuilder;
	
	private final MessageTranslatedBuilder translatedBuilder;
	
	private final MessageUnchangedBuilder unchagedBuilder;
	
	@Autowired
	public MessageBuilderFactory(final RoxanaProperties roxanaProperties,
							     final MessageFullyBuilder fullyBuilder,
							     final MessageTranslatedBuilder translatedBuilder,
							     final MessageUnchangedBuilder unchagedBuilder) {
		
		this.responseEstrategy = roxanaProperties.getBusinessResponseEstrategy();
		this.fullyBuilder = fullyBuilder;
		this.translatedBuilder = translatedBuilder;
		this.unchagedBuilder = unchagedBuilder;
	}

	public MessageBuilder getMessageBuilder() {
		switch (this.getResponseEstrategy()) {
		case FULLY:
			return this.getFullyBuilder();
		
		case TRANSLATED:
			return this.getTranslatedBuilder();
			
		case UNCHANGED:
			return this.getUnchangedBuilder();

		default:
			return this.getTranslatedBuilder();
		}
	}

	public ResponseEstrategy getResponseEstrategy() {
		return this.responseEstrategy;
	}

	private MessageFullyBuilder getFullyBuilder() {
		return this.fullyBuilder;
	}

	private MessageTranslatedBuilder getTranslatedBuilder() {
		return this.translatedBuilder;
	}

	private MessageUnchangedBuilder getUnchangedBuilder() {
		return this.unchagedBuilder;
	}
	
}