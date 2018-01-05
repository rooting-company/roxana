package br.com.rooting.roxana.message.builder;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.MessageFully;
import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.translator.Translator;

@Component
public class MessageFullyBuilder extends MessageBuilder {

	private final Translator translator;
	
	@Autowired
	public MessageFullyBuilder(final Translator translator) {
		super();
		this.translator = translator;
	}
	
	@Override
	public MessageFully build(MessageMapper mapper, Collection<Parameter> parameters) {
		Locale locale = this.getTranslator().getDefaultLocale();
		String language = locale.toLanguageTag();
		String translation = this.getTranslator().translate(mapper.getKey(), locale, parameters);
		return new MessageFully(mapper.getKey(), mapper.getSeverity(), language, translation, parameters);
	}
	
	private Translator getTranslator() {
		return this.translator;
	}

}