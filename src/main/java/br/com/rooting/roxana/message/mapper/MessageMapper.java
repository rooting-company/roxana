package br.com.rooting.roxana.message.mapper;

import br.com.rooting.roxana.message.MessageSeverity;

public interface MessageMapper {

    MessageSeverity getSeverity();

    String getKey();

}