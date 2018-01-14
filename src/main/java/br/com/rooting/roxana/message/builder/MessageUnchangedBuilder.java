package br.com.rooting.roxana.message.builder;

import java.util.Collection;

import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.MessageUnchanged;
import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.parameter.Parameter;

@Component
public class MessageUnchangedBuilder extends MessageBuilder {

	@Override
	public MessageUnchanged build(MessageMapper mapper, Collection<Parameter> parameters) {
		return new MessageUnchanged(mapper.getSeverity(), mapper.getKey());
	}
	
}