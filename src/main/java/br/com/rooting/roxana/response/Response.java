package br.com.rooting.roxana.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.lang.Nullable;

import br.com.rooting.roxana.message.Message;

public class Response {

	private final List<Message> messages = new ArrayList<>();
	
	Response(@Nullable final Collection<Message> collection) {
		super();
		this.messages.addAll(collection);
	}

	public List<Message> getMessages() {
		return Collections.unmodifiableList(messages);
	}

}