package br.com.rooting.roxana.response.processor;

import java.util.List;

import br.com.rooting.roxana.message.MessageSeverity;
import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.parameter.Parameter;

final class MessageResponseDTO implements MessageMapper {

	private MessageSeverity severity;
	
	private String key;
	
	private List<Parameter> parameters;
	
	public MessageSeverity getSeverity() {
		return severity;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setSeverity(MessageSeverity severity) {
		this.severity = severity;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
}