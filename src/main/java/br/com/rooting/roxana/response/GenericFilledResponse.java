package br.com.rooting.roxana.response;

import java.util.Collection;

import org.springframework.lang.Nullable;

import br.com.rooting.roxana.message.Message;

public class GenericFilledResponse<Z, T extends Message> extends GenericResponse<T> {

	private final Z object;
	
	public GenericFilledResponse(@Nullable final Z object, @Nullable final Collection<T> collection) {
		super(collection);
		this.object = object;
	}

	public Z getObject() {
		return object;
	}
	
}
	