package br.com.rooting.roxana.message;

public abstract class Message {

    private final MessageSeverity severity;

    Message(final MessageSeverity severity) throws IllegalArgumentException {
        if (severity == null) {
            throw new IllegalArgumentException();
        }

        this.severity = severity;
    }

    public MessageSeverity getSeverity() {
        return this.severity;
    }

}