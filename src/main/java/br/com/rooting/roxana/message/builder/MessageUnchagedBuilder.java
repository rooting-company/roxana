package br.com.rooting.roxana.message.builder;

import java.util.Collection;

import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.MessageUnchaged;
import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.parameter.Parameter;

@Component
public class MessageUnchagedBuilder extends MessageBuilder {

	@Override
	public MessageUnchaged build(MessageMapper mapper, Collection<Parameter> parameters) {
		return new MessageUnchaged(mapper.getSeverity(), mapper.getKey());
	}
	
}