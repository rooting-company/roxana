package br.com.rooting.roxana.message.mapper;

import br.com.rooting.roxana.message.MessageSeverity;

public class MockedMessageMapper implements MessageMapper {

	private final MessageSeverity severity;
	
	private final String key;
	
	public MockedMessageMapper(final String key, final MessageSeverity severity) {
		this.key = key;
		this.severity = severity;
	}
	
	@Override
	public MessageSeverity getSeverity() {
		return this.severity;
	}

	@Override
	public String getKey() {
		return this.key;
	}

}