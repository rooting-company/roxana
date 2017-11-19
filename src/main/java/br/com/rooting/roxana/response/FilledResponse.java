package br.com.rooting.roxana.response;

import java.util.Collection;

import org.springframework.lang.Nullable;

import br.com.rooting.roxana.message.Message;

public class FilledResponse<Z, T extends Message> extends Response<T> {

	private final Z object;
	
	public FilledResponse(@Nullable final Z object, @Nullable final Collection<T> collection) {
		super(collection);
		this.object = object;
	}

	public Z getObject() {
		return object;
	}
	
}
	