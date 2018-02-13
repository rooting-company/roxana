package br.com.rooting.roxana.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.response.parameter_finder.MessageMapperEnumParameterFinder;
import br.com.rooting.roxana.response.parameter_finder.ParameterFinderStrategy;

public interface MessageCreator {
	
	public Message create(MessageMapper mapper, List<Parameter> parameters) throws IllegalArgumentException;
	
	public default Message create(MessageMapper mapper, Parameter...parameters) throws IllegalArgumentException {
		return this.create(mapper, Arrays.asList(parameters));
	}
	
	public default Message create(MessageMapperEnum enumMapper, List<Object> parametersValues)
			throws IllegalArgumentException {
		ParameterFinderStrategy parameterFinder = new MessageMapperEnumParameterFinder(enumMapper, parametersValues);
		return this.create(enumMapper, parameterFinder.findParameters());
	}
	
	public default Message create(MessageMapperEnum enumMapper, Object...parametersValues) throws IllegalArgumentException {
		return this.create(enumMapper, Arrays.asList(parametersValues));
	}
	
	public default Message create(MessageMapperEnum enumMapper) throws IllegalArgumentException {
		return this.create(enumMapper, new ArrayList<Parameter>());
	}

}