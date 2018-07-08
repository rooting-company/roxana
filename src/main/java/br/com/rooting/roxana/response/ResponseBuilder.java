package br.com.rooting.roxana.response;

import br.com.rooting.roxana.message.Message;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public final class ResponseBuilder {

    private final List<Message> messages = new ArrayList<>();

    public ResponseBuilder() {
        super();
    }

    public static Response buildWith(List<Message> messages) {
        return new ResponseBuilder().appendMessages(messages).build();
    }

    @SafeVarargs
    public static Response buildWith(Message... messages) throws IllegalArgumentException {
        return new ResponseBuilder().appendMessages(asList(messages)).build();
    }

    public static <Z> FilledResponse<Z> buildFilledWith(@Nullable Z responseObject, List<Message> messages)
            throws IllegalArgumentException {

        return new ResponseBuilder().appendMessages(messages).buildFilled(responseObject);
    }

    @SafeVarargs
    public static <Z> FilledResponse<Z> buildFilledWith(@Nullable Z responseObject, Message... messages)
            throws IllegalArgumentException {

        return new ResponseBuilder().appendMessages(asList(messages)).buildFilled(responseObject);
    }

    public Response build() {
        return new Response(this.getMessages());
    }

    public <Z> FilledResponse<Z> buildFilled(@Nullable Z responseObject) {
        return new FilledResponse<>(responseObject, this.getMessages());
    }

    public ResponseBuilder appendMessage(Message message) throws IllegalArgumentException {
        if (message == null) {
            throw new IllegalArgumentException();
        }

        this.getMessages().add(message);
        return this;
    }

    public ResponseBuilder appendMessages(List<Message> messages) throws IllegalArgumentException {
        if (messages == null) {
            throw new IllegalArgumentException();
        }

        messages.forEach(this::appendMessage);
        return this;
    }

    public ResponseBuilder appendMessages(Message... messages) throws IllegalArgumentException {
        return this.appendMessages(asList(messages));
    }

    private List<Message> getMessages() {
        return this.messages;
    }

}