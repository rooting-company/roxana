package br.com.rooting.roxana.message;

public final class MessageUnchanged extends Message {
	
	private final String key;
	
	MessageUnchanged(final MessageSeverity severity,
					 final String key) {
		super(severity);
		
		if (key == null) {
			throw new IllegalArgumentException();
		}
		
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}
	
}