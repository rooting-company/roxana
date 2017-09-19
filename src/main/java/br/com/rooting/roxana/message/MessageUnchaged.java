package br.com.rooting.roxana.message;

public final class MessageUnchaged implements Message {
	
	private final MessageSeverity severity;
	
	private final String key;
	
	public MessageUnchaged(final MessageSeverity severity,
					  final String key) {
		this.severity = severity;
		this.key = key;
	}

	@Override
	public MessageSeverity getSeverity() {
		return this.severity;
	}
	
	public String getKey() {
		return this.key;
	}
	
}
