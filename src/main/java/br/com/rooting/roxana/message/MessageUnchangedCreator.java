package br.com.rooting.roxana.message;

import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class MessageUnchangedCreator implements MessageCreator {

    MessageUnchangedCreator() {
    }

    @Override
    public MessageUnchanged create(MessageMapper mapper, List<Parameter> parameters) {
        return new MessageUnchanged(mapper.getSeverity(), mapper.getKey(), parameters);
    }

    @Override
    public MessageUnchanged create(MessageMapper mapper, Parameter... parameters) throws IllegalArgumentException {
        return (MessageUnchanged) MessageCreator.super.create(mapper, parameters);
    }

    @Override
    public MessageUnchanged create(MessageMapperEnum enumMapper, List<Object> parametersValues)
            throws IllegalArgumentException {
        return (MessageUnchanged) MessageCreator.super.create(enumMapper, parametersValues);
    }

    @Override
    public MessageUnchanged create(MessageMapperEnum enumMapper, Object... parametersValues) throws IllegalArgumentException {
        return (MessageUnchanged) MessageCreator.super.create(enumMapper, parametersValues);
    }

    @Override
    public MessageUnchanged create(MessageMapperEnum enumMapper) throws IllegalArgumentException {
        return (MessageUnchanged) MessageCreator.super.create(enumMapper);
    }

}