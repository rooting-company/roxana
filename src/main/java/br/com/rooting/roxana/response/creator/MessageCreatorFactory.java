package br.com.rooting.roxana.response.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.RoxanaProperties;
import br.com.rooting.roxana.RoxanaProperties.Business.ResponseEstrategy;

@Component
public class MessageCreatorFactory {
	
	private final ResponseEstrategy responseEstrategy;
	
	private final MessageCreatorFully fullyCreator;
	
	private final MessageCreatorTranslated translatedCreator;
	
	private final MessageCreatorUnchaged unchagedCreator;
	
	@Autowired
	public MessageCreatorFactory(final RoxanaProperties roxanaProperties,
										  final MessageCreatorFully fullyCreator,
										  final MessageCreatorTranslated translatedCreator,
										  final MessageCreatorUnchaged unchagedCreator) {
		this.responseEstrategy = roxanaProperties.getBusinessResponseEstrategy();
		this.fullyCreator = fullyCreator;
		this.translatedCreator = translatedCreator;
		this.unchagedCreator = unchagedCreator;
	}

	public MessageCreator getMessageCreator() {
		switch (this.getResponseEstrategy()) {
		case FULLY:
			return this.getFullyCreator();
		
		case TRANSLATED:
			return this.getTranslatedCreator();
			
		case UNCHANGED:
			return this.getUnchagedCreator();

		default:
			return this.getTranslatedCreator();
		}
	}

	public ResponseEstrategy getResponseEstrategy() {
		return responseEstrategy;
	}

	private MessageCreatorFully getFullyCreator() {
		return fullyCreator;
	}

	private MessageCreatorTranslated getTranslatedCreator() {
		return translatedCreator;
	}

	private MessageCreatorUnchaged getUnchagedCreator() {
		return unchagedCreator;
	}
	
}
