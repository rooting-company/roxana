package br.com.rooting.roxana.message;

import java.util.Collection;
import java.util.Collections;

import br.com.rooting.roxana.parameter.Parameter;

public final class MessageInternatiolized implements Message {
	
	private final MessageSeverity severity;

	private final String key;

	private final Collection<Parameter> parameters;

	private final String language;

	private final String translation;

	public MessageInternatiolized(final String key, 
			final MessageSeverity severity, 
			final String language, 
			final String translation, 
			final Collection<Parameter> parameters) {
		
		super();
		
		if(key == null
				|| severity == null
				|| language == null
				|| translation == null
				|| parameters == null) {
			throw new IllegalArgumentException();
		}
		
		this.key = key;
		this.severity = severity;
		this.parameters = parameters;
		this.language = language;
		this.translation = translation;
	}

	public MessageSeverity getSeverity() {
		return this.severity;
	}
	
	public String getKey() {
		return this.key;
	}

	public Collection<Parameter> getParameters() {
		return Collections.unmodifiableCollection(this.parameters);
	}

	public String getLanguage() {
		return this.language;
	}

	public String getTranslation() {
		return this.translation;
	}

}