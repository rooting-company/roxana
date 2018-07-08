package br.com.rooting.roxana.message;

import br.com.rooting.roxana.parameter.Parameter;

import java.util.Collections;
import java.util.List;

public final class MessageFully extends Message {

    private final String key;

    private final List<Parameter> parameters;

    private final String language;

    private final String translation;

    MessageFully(final MessageSeverity severity,
                 final String key,
                 final String language,
                 final String translation,
                 final List<Parameter> parameters) throws IllegalArgumentException {
        super(severity);

        if (key == null || language == null || translation == null || parameters == null) {
            throw new IllegalArgumentException();
        }

        this.key = key;
        this.parameters = parameters;
        this.language = language;
        this.translation = translation;
    }

    public String getKey() {
        return this.key;
    }

    public List<Parameter> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    public String getLanguage() {
        return this.language;
    }

    public String getTranslation() {
        return this.translation;
    }

}