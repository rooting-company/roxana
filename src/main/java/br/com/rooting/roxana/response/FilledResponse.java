package br.com.rooting.roxana.response;

import br.com.rooting.roxana.message.Message;
import org.springframework.lang.Nullable;

import java.util.List;

public class FilledResponse<Z> extends Response {

    private final Z object;

    FilledResponse(@Nullable final Z object, final List<Message> collection) {
        super(collection);
        this.object = object;
    }

    public Z getObject() {
        return object;
    }

}