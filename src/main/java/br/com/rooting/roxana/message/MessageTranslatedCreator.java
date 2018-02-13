package br.com.rooting.roxana.message;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.translator.Translator;

@Component
public class MessageTranslatedCreator implements MessageCreator {

	private final Translator translator;
	
	@Autowired
	MessageTranslatedCreator(final Translator translator) throws IllegalArgumentException {
		super();
		
		if(translator == null) {
			throw new IllegalArgumentException();
		}
		
		this.translator = translator;
	}
	
	@Override
	public MessageTranslated create(MessageMapper mapper, List<Parameter> parameters) throws IllegalArgumentException {
		Locale locale = this.getTranslator().getLocale();
		String translation = this.getTranslator().translate(mapper.getKey(), locale, parameters);
		return new MessageTranslated(mapper.getSeverity(), translation);
	}
	
	@Override
	public MessageTranslated create(MessageMapper mapper, Parameter...parameters) throws IllegalArgumentException {
		return (MessageTranslated) MessageCreator.super.create(mapper, parameters);
	}
	
	@Override
	public MessageTranslated create(MessageMapperEnum enumMapper, List<Object> parametersValues)
			throws IllegalArgumentException {
		return (MessageTranslated) MessageCreator.super.create(enumMapper, parametersValues);
	}
	
	@Override
	public MessageTranslated create(MessageMapperEnum enumMapper, Object...parametersValues) throws IllegalArgumentException {
		return (MessageTranslated) MessageCreator.super.create(enumMapper, parametersValues);
	}
	
	@Override
	public MessageTranslated create(MessageMapperEnum enumMapper) throws IllegalArgumentException {
		return (MessageTranslated) MessageCreator.super.create(enumMapper);
	}
	
	private Translator getTranslator() {
		return this.translator;
	}
	
}