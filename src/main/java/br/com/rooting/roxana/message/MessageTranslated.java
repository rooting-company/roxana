package br.com.rooting.roxana.message;

public final class MessageTranslated extends Message {
	
	private final String translation;
	
	MessageTranslated(final MessageSeverity severity, final String translation) throws IllegalArgumentException {
		super(severity);

		if (translation == null) {
			throw new IllegalArgumentException();
		}

		this.translation = translation;
	}

	public String getTranslation() {
		return this.translation;
	}

}