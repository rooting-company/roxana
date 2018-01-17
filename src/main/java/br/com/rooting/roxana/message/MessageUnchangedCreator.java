package br.com.rooting.roxana.message;

import java.util.Collection;

import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.parameter.Parameter;

@Component
public class MessageUnchangedCreator extends MessageCreator {

	@Override
	public MessageUnchanged create(MessageMapper mapper, Collection<Parameter> parameters) {
		return new MessageUnchanged(mapper.getSeverity(), mapper.getKey());
	}
	
}