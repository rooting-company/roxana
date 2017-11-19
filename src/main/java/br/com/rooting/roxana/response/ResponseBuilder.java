package br.com.rooting.roxana.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import br.com.rooting.roxana.message.Message;

public class ResponseBuilder<T extends Message> {
	
	private Collection<T> messages;
	
	public ResponseBuilder() {
		super();
	}
	
	public static <T extends Message> Response<T> buildWith(Collection<T> messages) {
		return new ResponseBuilder<T>().appendMessages(messages).build();
	}
	
	@SafeVarargs
	public static <T extends Message> Response<T> buildWith(T... messages) {
		return new ResponseBuilder<T>().appendMessages(Arrays.asList(messages)).build();
	}
	
	public static <Z, T extends Message> FilledResponse<Z, T> buildFilledWith(Z responseObject, Collection<T> messages) {
		return new ResponseBuilder<T>().appendMessages(messages).buildFilled(responseObject);
	}
	
	@SafeVarargs
	public static <Z, T extends Message> FilledResponse<Z, T> buildFilledWith(Z responseObject, T...messages) {
		return new ResponseBuilder<T>().appendMessages(Arrays.asList(messages)).buildFilled(responseObject);
	}
	
	public ResponseBuilder<T> appendMessage(T t) {
		this.getMessages().add(t);
		return this;
	}
	
	public ResponseBuilder<T> appendMessages(Collection<T> messages) {
		this.getMessages().addAll(messages);
		return this;
	}
	
	public Response<T> build() {
		return new Response<T>(this.getMessages());
	}
	
	public <Z> FilledResponse<Z, T> buildFilled(Z responseObject) {
		return new FilledResponse<Z, T>(responseObject, this.getMessages());
	}
	
	protected Collection<T> getMessages() {
		if(this.messages == null) {
			this.messages = new ArrayList<>();
		}
		return this.messages;
	}
}
