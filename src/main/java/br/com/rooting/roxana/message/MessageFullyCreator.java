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
class MessageFullyCreator implements MessageCreator {

	private final Translator translator;
	
	@Autowired
	MessageFullyCreator(final Translator translator) throws IllegalArgumentException {
		super();
		
		if(translator == null) {
			throw new IllegalArgumentException();
		}
		
		this.translator = translator;
	}
	
	@Override
	public MessageFully create(MessageMapper mapper, List<Parameter> parameters) throws IllegalArgumentException {
		Locale locale = this.getTranslator().getLocale();
		String language = locale.toLanguageTag();
		String translation = this.getTranslator().translate(mapper.getKey(), locale, parameters);
		return new MessageFully(mapper.getSeverity(), mapper.getKey(), language, translation, parameters);
	}
	
	@Override
	public MessageFully create(MessageMapper mapper, Parameter...parameters) throws IllegalArgumentException {
		return (MessageFully) MessageCreator.super.create(mapper, parameters);
	}
	
	@Override
	public MessageFully create(MessageMapperEnum enumMapper, List<Object> parametersValues)
			throws IllegalArgumentException {
		return (MessageFully) MessageCreator.super.create(enumMapper, parametersValues);
	}
	
	@Override
	public MessageFully create(MessageMapperEnum enumMapper, Object...parametersValues) throws IllegalArgumentException {
		return (MessageFully) MessageCreator.super.create(enumMapper, parametersValues);
	}
	
	@Override
	public MessageFully create(MessageMapperEnum enumMapper) throws IllegalArgumentException {
		return (MessageFully) MessageCreator.super.create(enumMapper);
	}
	
	private Translator getTranslator() {
		return this.translator;
	}

}