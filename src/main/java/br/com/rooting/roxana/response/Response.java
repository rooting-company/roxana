package br.com.rooting.roxana.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.rooting.roxana.message.Message;

public class Response {

	private final List<Message> messages = new ArrayList<>();
	
	Response(final List<Message> messages) {
		if (messages == null) {
			throw new IllegalArgumentException();
		}
		
		this.messages.addAll(messages);
	}

	public List<Message> getMessages() {
		return Collections.unmodifiableList(messages);
	}

}