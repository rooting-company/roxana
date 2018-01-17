package br.com.rooting.roxana.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.response.parameterFinder.MessageMapperEnumParameterFinder;
import br.com.rooting.roxana.response.parameterFinder.ParameterFinderStrategy;

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