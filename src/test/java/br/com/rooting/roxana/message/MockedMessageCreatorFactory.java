package br.com.rooting.roxana.message;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.translator.Translator;

public class MockedMessageCreatorFactory extends MessageCreatorFactory {

    public MockedMessageCreatorFactory(final RoxanaProperties roxanaProperties,
                                       final Translator translator)
            throws IllegalArgumentException {

        super(roxanaProperties, new MessageFullyCreator(translator),
                new MessageTranslatedCreator(translator),
                new MessageUnchangedCreator());
    }

}