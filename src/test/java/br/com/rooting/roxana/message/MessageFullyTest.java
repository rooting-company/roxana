package br.com.rooting.roxana.message;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.parameter.Parameter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static br.com.rooting.roxana.message.MessageSeverity.ERROR;
import static br.com.rooting.roxana.translator.LocaleTagEnum.PT_BR;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

// TODO To avail how avoid test duplications for the message classes.
class MessageFullyTest extends UnitTest<MessageFully> {

    private static final ArrayList<Parameter> EMPTY_PARAMETER_LIST = new ArrayList<>();
    private static final String TRANSLATION = "translation";
    private static final String KEY = "key";

    @Test
    void testClassIsPublicTest() {
        assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassExtendsMessageTest() {
        assertTrue(Message.class.isAssignableFrom(this.getUnitTestClass()));
    }

    @Test
    void testClassIsFinalTest() {
        assertTrue(isFinal(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassWasOnlyOnePackagePrivateConstructorTest() {
        Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(isPackagePrivate(constructors[0].getModifiers()));
    }

    @Test
    void serverityCanNotBeNullTest() {
        Executable executable = () -> new MessageFully(null, PT_BR.getTag(), KEY, TRANSLATION, EMPTY_PARAMETER_LIST);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void keyCanNotBeNullTest() {
        Executable executable = () -> new MessageFully(ERROR, null, PT_BR.getTag(), TRANSLATION, EMPTY_PARAMETER_LIST);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void languageCanNotBeNullTest() {
        Executable executable = () -> new MessageFully(ERROR, KEY, null, TRANSLATION, EMPTY_PARAMETER_LIST);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void translationCanNotBeNullTest() {
        Executable executable = () -> new MessageFully(ERROR, KEY, PT_BR.getTag(), null, EMPTY_PARAMETER_LIST);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void parametersCanNotBeNullTest() {
        Executable executable = () -> new MessageFully(ERROR, KEY, PT_BR.getTag(), TRANSLATION, null);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void instancionTest() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(mock(Parameter.class));
        parameters.add(mock(Parameter.class));
        parameters.add(mock(Parameter.class));
        MessageFully message = new MessageFully(ERROR, KEY, PT_BR.getTag(), TRANSLATION, parameters);

        assertEquals(ERROR, message.getSeverity());
        assertEquals(KEY, message.getKey());
        assertEquals(PT_BR.getTag(), message.getLanguage());
        assertEquals(TRANSLATION, message.getTranslation());
        assertEquals(message.getParameters().size(), parameters.size());
        assertTrue(message.getParameters().containsAll(parameters));
    }

    @Test
    void parametersAreUnmodifiableTest() {
        MessageFully message = new MessageFully(ERROR, KEY, PT_BR.getTag(), TRANSLATION, EMPTY_PARAMETER_LIST);
        assertThrows(UnsupportedOperationException.class, () -> message.getParameters().add(mock(Parameter.class)));
    }

}