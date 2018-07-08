package br.com.rooting.roxana.response;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FilledResponseTest extends UnitTest<FilledResponse<?>> {

    @Test
    void testClassIsPublicTest() {
        assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassExtendsResponseTest() {
        assertTrue(Response.class.isAssignableFrom(this.getUnitTestClass()));
    }

    @Test
    void testClassWasOnlyOnePackagePrivateConstructorTest() {
        Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(isPackagePrivate(constructors[0].getModifiers()));
    }

    @Test
    void messagesCanNotBeNullTest() {
        Executable executable = () -> new FilledResponse<>(new Object(), null);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void filledObjectCanBeNullTest() {
        FilledResponse<Object> nullObject = new FilledResponse<>(null, new ArrayList<>());
        assertNull(nullObject.getObject());
    }

    @Test
    void responseMessagesAreUnmodifiable() {
        Response response = new Response(new ArrayList<>());
        Executable executable = () -> response.getMessages().add(mock(Message.class));
        assertThrows(UnsupportedOperationException.class, executable);
    }

}