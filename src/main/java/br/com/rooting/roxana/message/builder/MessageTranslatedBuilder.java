package br.com.rooting.roxana.message.builder;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.MessageTranslated;
import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.translator.Translator;

@Component
public class MessageTranslatedBuilder extends MessageBuilder {

	private final Translator translator;
	
	@Autowired
	public MessageTranslatedBuilder(final Translator translator) {
		super();
		this.translator = translator;
	}
	
	@Override
	public MessageTranslated build(MessageMapper mapper, Collection<Parameter> parameters) {
		Locale locale = this.getTranslator().getDefaultLocale();
		String translation = this.getTranslator().translate(mapper.getKey(), locale, parameters);
		return new MessageTranslated(mapper.getSeverity(), translation);
	}
	
	private Translator getTranslator() {
		return this.translator;
	}
	
}
