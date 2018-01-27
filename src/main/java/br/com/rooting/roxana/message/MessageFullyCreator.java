package br.com.rooting.roxana.message;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.translator.Translator;

@Component
public class MessageFullyCreator extends MessageCreator {

	private final Translator translator;
	
	@Autowired
	public MessageFullyCreator(final Translator translator) {
		super();
		this.translator = translator;
	}
	
	@Override
	public MessageFully create(MessageMapper mapper, Collection<Parameter> parameters) {
		Locale locale = this.getTranslator().getLocale();
		String language = locale.toLanguageTag();
		String translation = this.getTranslator().translate(mapper.getKey(), locale, parameters);
		return new MessageFully(mapper.getKey(), mapper.getSeverity(), language, translation, parameters);
	}
	
	private Translator getTranslator() {
		return this.translator;
	}

}