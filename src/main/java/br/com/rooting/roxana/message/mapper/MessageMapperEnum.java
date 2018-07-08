package br.com.rooting.roxana.message.mapper;

import br.com.rooting.roxana.translator.Translator;

public interface MessageMapperEnum extends MessageMapper {

    String name();

    @Override
    default String getKey() {
        return Translator.getInterpolatedKeyOf(this.getClass().getName() + "." + this.name());
    }

}