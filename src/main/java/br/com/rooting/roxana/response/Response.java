package br.com.rooting.roxana.response;

import br.com.rooting.roxana.message.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Response {

    private final List<Message> messages = new ArrayList<>();

    Response(final List<Message> messages) {
        if (messages == null) {
            throw new IllegalArgumentException();
        }

        this.messages.addAll(messages);
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

}