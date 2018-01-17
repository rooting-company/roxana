package br.com.rooting.roxana.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import br.com.rooting.roxana.message.Message;

public class ResponseBuilder {
	
	private Collection<Message> messages;
	
	public ResponseBuilder() {
		super();
	}
	
	public static Response buildWith(Collection<Message> messages) {
		return new ResponseBuilder().appendMessages(messages).build();
	}
	
	@SafeVarargs
	public static Response buildWith(Message... messages) {
		return new ResponseBuilder().appendMessages(Arrays.asList(messages)).build();
	}
	
	public static <Z> FilledResponse<Z> buildFilledWith(Z responseObject, Collection<Message> messages) {
		return new ResponseBuilder().appendMessages(messages).buildFilled(responseObject);
	}
	
	@SafeVarargs
	public static <Z> FilledResponse<Z> buildFilledWith(Z responseObject, Message...messages) {
		return new ResponseBuilder().appendMessages(Arrays.asList(messages)).buildFilled(responseObject);
	}
	
	public Response build() {
		return new Response(this.getMessages());
	}
	
	public <Z> FilledResponse<Z> buildFilled(Z responseObject) {
		return new FilledResponse<Z>(responseObject, this.getMessages());
	}
	
	public ResponseBuilder appendMessage(Message message) {
		this.getMessages().add(message);
		return this;
	}
	
	public ResponseBuilder appendMessages(Collection<Message> messages) {
		this.getMessages().addAll(messages);
		return this;
	}
	
	protected Collection<Message> getMessages() {
		if(this.messages == null) {
			this.messages = new ArrayList<>();
		}
		return this.messages;
	}
}