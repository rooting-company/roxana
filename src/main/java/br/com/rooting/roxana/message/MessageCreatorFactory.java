package br.com.rooting.roxana.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;

@Component
public class MessageCreatorFactory {
	
	private final ResponseStrategy responseStrategy;
	
	private final MessageFullyCreator fullyCreator;
	
	private final MessageTranslatedCreator translatedCreator;
	
	private final MessageUnchangedCreator unchagedCreator;
	
	@Autowired
	public MessageCreatorFactory(final RoxanaProperties roxanaProperties,
							     final MessageFullyCreator fullyCreator,
							     final MessageTranslatedCreator translatedCreator,
							     final MessageUnchangedCreator unchagedCreator) {
		
		this.responseStrategy = roxanaProperties.getBusinessResponseStrategy();
		this.fullyCreator = fullyCreator;
		this.translatedCreator = translatedCreator;
		this.unchagedCreator = unchagedCreator;
	}

	public MessageCreator getMessageCreator() {
		switch (this.getResponseStrategy()) {
		case FULLY:
			return this.getFullyCreator();
		
		case TRANSLATED:
			return this.getTranslatedCreator();
			
		case UNCHANGED:
			return this.getUnchangedCreator();

		default:
			return this.getTranslatedCreator();
		}
	}

	public ResponseStrategy getResponseStrategy() {
		return this.responseStrategy;
	}

	private MessageFullyCreator getFullyCreator() {
		return this.fullyCreator;
	}

	private MessageTranslatedCreator getTranslatedCreator() {
		return this.translatedCreator;
	}

	private MessageUnchangedCreator getUnchangedCreator() {
		return this.unchagedCreator;
	}
	
}