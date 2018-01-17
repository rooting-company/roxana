package br.com.rooting.roxana.response;

import java.util.Collection;

import org.springframework.lang.Nullable;

import br.com.rooting.roxana.message.Message;

public class FilledResponse<Z> extends Response {

	private final Z object;
	
	FilledResponse(@Nullable final Z object, @Nullable final Collection<Message> collection) {
		super(collection);
		this.object = object;
	}

	public Z getObject() {
		return object;
	}
	
}