package br.com.rooting.roxana.message;

import br.com.rooting.roxana.UnitTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageTest extends UnitTest<Message> {

    @Test
    void testClassIsPublicTest() {
        assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassIsAbstractTest() {
        assertTrue(isAbstract(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassWasOnlyOnePackagePrivateConstructorTest() {
        Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(isPackagePrivate(constructors[0].getModifiers()));
    }

}