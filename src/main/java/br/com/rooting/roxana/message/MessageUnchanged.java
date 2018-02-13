package br.com.rooting.roxana.message;

import java.util.Collections;
import java.util.List;

import br.com.rooting.roxana.parameter.Parameter;

public final class MessageUnchanged extends Message {
	
	private final String key;
	
	private final List<Parameter> parameters;
	
	MessageUnchanged(final MessageSeverity severity,
					 final String key,
					 final List<Parameter> parameters) throws IllegalArgumentException {
		super(severity);
		
		if (key == null || parameters == null) {
			throw new IllegalArgumentException();
		}
		
		this.key = key;
		this.parameters = parameters;
	}

	public String getKey() {
		return this.key;
	}
	

	public List<Parameter> getParameters() {
		return Collections.unmodifiableList(this.parameters);
	}
	
}