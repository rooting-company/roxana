package br.com.rooting.roxana.message;

import br.com.rooting.roxana.UnitTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static br.com.rooting.roxana.message.MessageSeverity.ERROR;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;

class MessageTranslatedTest extends UnitTest<MessageTranslated> {

    private static final String TRANSLATION = "translation";

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
    void severityCanNotBeNullTest() {
        assertThrows(IllegalArgumentException.class, () -> new MessageTranslated(null, TRANSLATION));
    }

    @Test
    void translationCanNotBeNullTest() {
        assertThrows(IllegalArgumentException.class, () -> new MessageTranslated(ERROR, null));
    }

    @Test
    void instantiationTest() {
        MessageTranslated message = new MessageTranslated(ERROR, TRANSLATION);
        assertEquals(ERROR, message.getSeverity());
        assertEquals(TRANSLATION, message.getTranslation());
    }

}