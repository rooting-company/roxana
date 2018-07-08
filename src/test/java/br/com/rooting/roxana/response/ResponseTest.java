package br.com.rooting.roxana.response;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.Message;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ResponseTest extends UnitTest<Response> {

    @Test
    void testClassIsPublicTest() {
        assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassWasOnlyOnePackagePrivateConstructorTest() {
        Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(isPackagePrivate(constructors[0].getModifiers()));
    }

    @Test
    void messagesCanNotBeNullTest() {
        assertThrows(IllegalArgumentException.class, () -> new Response(null));
    }

    @Test
    void responseMessagesAreUnmodifiableTest() {
        Response response = new Response(new ArrayList<>());
        assertThrows(UnsupportedOperationException.class, () -> response.getMessages().add(mock(Message.class)));
    }

}