package br.com.rooting.roxana.response.creator;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.MessageFully;
import br.com.rooting.roxana.message.MessageMapper;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.translator.Translator;

@Component
public class MessageCreatorFully extends MessageCreator {

	private final Translator translator;
	
	@Autowired
	public MessageCreatorFully(final Translator translator) {
		super();
		this.translator = translator;
	}
	
	@Override
	public MessageFully create(MessageMapper mapper, Collection<Parameter> parameters) {
		Locale locale = this.getTranslator().getDefaultLocale();
		String language = locale.toLanguageTag();
		String translation = this.getTranslator().translate(mapper.getMessageKey(), locale, parameters);
		return new MessageFully(mapper.getMessageKey(), mapper.getSeverity(), language, translation, parameters);
	}
	
	private Translator getTranslator() {
		return this.translator;
	}

}