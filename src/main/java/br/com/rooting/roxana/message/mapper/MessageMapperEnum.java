package br.com.rooting.roxana.message.mapper;

import br.com.rooting.roxana.translator.Translator;

public interface MessageMapperEnum extends MessageMapper {
	
	public String name();
	
	@Override
	default String getKey() {
		return Translator.getInterpoledKeyOf(this.getClass().getCanonicalName() + "." + this.name());
	}
}