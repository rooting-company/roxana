package br.com.rooting.roxana.message.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import br.com.rooting.roxana.message.Message;
import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.response.parameterFinder.MessageMapperEnumParameterFinder;
import br.com.rooting.roxana.response.parameterFinder.ParameterFinderStrategy;

public abstract class MessageBuilder {
	
	public abstract Message build(MessageMapper mapper, Collection<Parameter> parameters);
	
	public Message build(MessageMapper mapper, Parameter...parameters) {
		return this.build(mapper, Arrays.asList(parameters));
	}
	
	public Message build(MessageMapperEnum enumMapper, Object...parametersValues) {
		ParameterFinderStrategy parameterFinder = new MessageMapperEnumParameterFinder(enumMapper, Arrays.asList(parametersValues));
		return this.build(enumMapper, parameterFinder.findParameters());
	}
	
	public Message build(MessageMapperEnum enumMapper) {
		return this.build(enumMapper, new ArrayList<Parameter>());
	}

}