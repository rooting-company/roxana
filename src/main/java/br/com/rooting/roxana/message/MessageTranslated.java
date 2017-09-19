package br.com.rooting.roxana.message;

public final class MessageTranslated implements Message {
	
	private final MessageSeverity severity;
	
	private final String translation;
	
	public MessageTranslated(final MessageSeverity severity,
					  final String translation) {
		this.severity = severity;
		this.translation = translation;
	}

	@Override
	public MessageSeverity getSeverity() {
		return this.severity;
	}
	
	public String getTranslation() {
		return this.translation;
	}

}