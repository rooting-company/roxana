package br.com.rooting.roxana.response.creator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import br.com.rooting.roxana.message.Message;
import br.com.rooting.roxana.message.MessageMapper;
import br.com.rooting.roxana.message.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.response.parameter.finder.MessageMapperEnumParameterFinder;
import br.com.rooting.roxana.response.parameter.finder.ParameterFinderStrategy;

public abstract class MessageCreator {
	
	public abstract Message create(MessageMapper mapper, Collection<Parameter> parameters);
	
	public Message create(MessageMapper mapper, Parameter...parameters) {
		return this.create(mapper, Arrays.asList(parameters));
	}
	
	public Message create(MessageMapperEnum enumMapper, Object...parametersValues) {
		ParameterFinderStrategy parameterFinder = new MessageMapperEnumParameterFinder(enumMapper, Arrays.asList(parametersValues));
		return this.create(enumMapper, parameterFinder.findParameters());
	}
	
	public Message create(MessageMapperEnum enumMapper) {
		return this.create(enumMapper, new ArrayList<Parameter>());
	}

}