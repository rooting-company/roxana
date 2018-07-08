package br.com.rooting.roxana.response;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class FilledResponseTest extends UnitTest<FilledResponse<?>> {
	
	// Testa se a classe é public, pois a mesma é usada 
	// para representar as respostas em serviços Rest externos.
	@Test
	void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	// Testa se a classe é filha de Response.
	@Test
	void testClassExtendsResponseTest() {
		assertTrue(Response.class.isAssignableFrom(this.getUnitTestClass()));
	}
	
	// Testa se a classe só tem um unico construtor
	// e se esse é package private para que somente classes do pacote possam extender.
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
	
	// Testa se o objeto do Filled Response pode ser null.
	@Test
	void filledObjectCanBeNullTest() {
		FilledResponse<Object> nullObject = new FilledResponse<>(null, new ArrayList<>());
		assertNull(nullObject.getObject());
	}
	
	// Testa se as messages da Resposta são imutaveis.
	@Test
	void responseMessagesAreUnmodifiable() {
		Response response = new Response(new ArrayList<>());
        Executable executable = () -> response.getMessages().add(mock(Message.class));
        assertThrows(UnsupportedOperationException.class, executable);
	}
	
}