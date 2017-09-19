package br.com.rooting.roxana.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import br.com.rooting.roxana.message.Message;

public class GenericResponseBuilder<T extends Message> {
	
	private Collection<T> messages;
	
	public GenericResponseBuilder() {
		super();
	}
	
	public static <T extends Message> GenericResponse<T> buildWith(Collection<T> messages) {
		return new GenericResponseBuilder<T>().appendMessages(messages).build();
	}
	
	@SafeVarargs
	public static <T extends Message> GenericResponse<T> buildWith(T... messages) {
		return new GenericResponseBuilder<T>().appendMessages(Arrays.asList(messages)).build();
	}
	
	public static <Z, T extends Message> GenericFilledResponse<Z, T> buildFilledWith(Z responseObject, Collection<T> messages) {
		return new GenericResponseBuilder<T>().appendMessages(messages).buildFilled(responseObject);
	}
	
	@SafeVarargs
	public static <Z, T extends Message> GenericFilledResponse<Z, T> buildFilledWith(Z responseObject, T...messages) {
		return new GenericResponseBuilder<T>().appendMessages(Arrays.asList(messages)).buildFilled(responseObject);
	}
	
	public GenericResponseBuilder<T> appendMessage(T t) {
		this.getMessages().add(t);
		return this;
	}
	
	public GenericResponseBuilder<T> appendMessages(Collection<T> messages) {
		this.getMessages().addAll(messages);
		return this;
	}
	
	public GenericResponse<T> build() {
		return new GenericResponse<T>(this.getMessages());
	}
	
	public <Z> GenericFilledResponse<Z, T> buildFilled(Z responseObject) {
		return new GenericFilledResponse<Z, T>(responseObject, this.getMessages());
	}
	
	protected Collection<T> getMessages() {
		if(this.messages == null) {
			this.messages = new ArrayList<>();
		}
		return this.messages;
	}
}
