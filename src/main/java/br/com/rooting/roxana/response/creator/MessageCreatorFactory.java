package br.com.rooting.roxana.response.creator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.RoxanaProperties;
import br.com.rooting.roxana.RoxanaProperties.Business.ResponseEstrategy;

@Component
public class MessageCreatorFactory {
	
	private final ResponseEstrategy responseEstrategy;
	
	private final MessageCreatorInternatiolized internatiolizedCreator;
	
	private final MessageCreatorTranslated translatedCreator;
	
	private final MessageCreatorUnchaged unchagedCreator;
	
	@Autowired
	public MessageCreatorFactory(final RoxanaProperties roxanaProperties,
										  final MessageCreatorInternatiolized internatiolizedCreator,
										  final MessageCreatorTranslated translatedCreator,
										  final MessageCreatorUnchaged unchagedCreator) {
		this.responseEstrategy = roxanaProperties.getBusinessResponseEstrategy();
		this.internatiolizedCreator = internatiolizedCreator;
		this.translatedCreator = translatedCreator;
		this.unchagedCreator = unchagedCreator;
	}

	public MessageCreator getMessageCreator() {
		switch (this.getResponseEstrategy()) {
		case INTERNATIONALIZED:
			return this.getInternatiolizedCreator();
		
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

	private MessageCreatorInternatiolized getInternatiolizedCreator() {
		return internatiolizedCreator;
	}

	private MessageCreatorTranslated getTranslatedCreator() {
		return translatedCreator;
	}

	private MessageCreatorUnchaged getUnchagedCreator() {
		return unchagedCreator;
	}
	
}
