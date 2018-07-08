package br.com.rooting.roxana.message;

import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.finder.MessageMapperEnumParameterFinder;
import br.com.rooting.roxana.parameter.finder.ParameterFinderStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface MessageCreator {

    Message create(MessageMapper mapper, List<Parameter> parameters) throws IllegalArgumentException;

    default Message create(MessageMapper mapper, Parameter... parameters) throws IllegalArgumentException {
        return this.create(mapper, Arrays.asList(parameters));
    }

    default Message create(MessageMapperEnum enumMapper, List<Object> parametersValues)
            throws IllegalArgumentException {
        ParameterFinderStrategy parameterFinder = new MessageMapperEnumParameterFinder(enumMapper, parametersValues);
        return this.create(enumMapper, parameterFinder.findParameters());
    }

    default Message create(MessageMapperEnum enumMapper, Object... parametersValues) throws IllegalArgumentException {
        return this.create(enumMapper, Arrays.asList(parametersValues));
    }

    default Message create(MessageMapperEnum enumMapper) throws IllegalArgumentException {
        return this.create(enumMapper, new ArrayList<Parameter>());
    }

}