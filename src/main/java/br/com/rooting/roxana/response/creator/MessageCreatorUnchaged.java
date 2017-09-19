package br.com.rooting.roxana.response.creator;

import java.util.Collection;

import org.springframework.stereotype.Component;

import br.com.rooting.roxana.message.MessageMapper;
import br.com.rooting.roxana.message.MessageUnchaged;
import br.com.rooting.roxana.parameter.Parameter;

@Component
public class MessageCreatorUnchaged extends MessageCreator {

	@Override
	public MessageUnchaged create(MessageMapper mapper, Collection<Parameter> parameters) {
		return new MessageUnchaged(mapper.getSeverity(), mapper.getMessageKey());
	}
	
}
