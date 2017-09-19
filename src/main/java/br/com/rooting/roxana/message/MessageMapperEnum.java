package br.com.rooting.roxana.message;

import br.com.rooting.roxana.translator.Translator;

public interface MessageMapperEnum extends MessageMapper {
	
	public String name();
	
	@Override
	default String getMessageKey() {
		return Translator.getInterpoledKeyOf(this.getClass().getCanonicalName() + "." + this.name());
	}
}