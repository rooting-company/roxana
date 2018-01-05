package br.com.rooting.roxana.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.lang.Nullable;

import br.com.rooting.roxana.message.Message;

public class Response<T extends Message> {

	private final List<T> messages = new ArrayList<>();
	
	Response(@Nullable final Collection<T> collection) {
		super();
		this.messages.addAll(collection);
	}

	public List<T> getMessages() {
		return Collections.unmodifiableList(messages);
	}

}