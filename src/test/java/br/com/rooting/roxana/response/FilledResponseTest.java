package br.com.rooting.roxana.response;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.Message;

public class FilledResponseTest extends UnitTest<FilledResponse<?>> {
	
	// Testa se a classe é public, pois a mesma é usada 
	// para representar as respostas em serviços Rest externos.
	@Test
	public void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	// Testa se a classe é filha de Response.
	@Test
	public void testClassExtendsResponseTest() {
		assertTrue(Response.class.isAssignableFrom(this.getUnitTestClass()));
	}
	
	// Testa se a classe só tem um unico construtor
	// e se esse é package private para que somente classes do pacote possam extender.
	@Test
	public void testClassWasOnlyOnePackagePrivateConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		assertTrue(constructors.length == 1);
		assertTrue(isPackagePrivate(constructors[0].getModifiers()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void messagesCanNotBeNullTest() {
		new FilledResponse<Object>(new Object(), null);
	}
	
	// Testa se o objeto do Filled Response pode ser null.
	@Test
	public void filledObjectCanBeNullTest() {
		FilledResponse<Object> nullObject = new FilledResponse<>(null, new ArrayList<Message>());
		assertNull(nullObject.getObject());
	}
	
	// Testa se as messages da Resposta são imutaveis.
	@Test(expected = UnsupportedOperationException.class)
	public void responseMessagesAreUnmodifiable() {
		Response response = new Response(new ArrayList<Message>());
		response.getMessages().add(mock(Message.class));
	}
	
}