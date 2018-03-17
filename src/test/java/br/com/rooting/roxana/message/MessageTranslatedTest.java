package br.com.rooting.roxana.message;

import static br.com.rooting.roxana.message.MessageSeverity.ERROR;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;

public class MessageTranslatedTest extends UnitTest<MessageTranslated> {
	
	private static final String TRANSLATION = "translation";
	
	// Todos os tipos de mensagem devem ser publica,
	// para que o programador possa usa-lo diretamente caso queira.
	@Test
	public void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	// Testa se a classe é filha de Message.
	public void testClassExtendsMessageTest() {
		assertTrue(Message.class.isAssignableFrom(this.getUnitTestClass()));
	}
	
	@Test
	public void testClassIsFinalTest() {
		assertTrue(isFinal(this.getUnitTestClass().getModifiers()));
	}
	
	// Testa se a classe só tem um unico construtor
	// e se esse é package private para que somente a classe Creator devida possa cria-lo.
	@Test
	public void testClassWasOnlyOnePackagePrivateConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		assertTrue(constructors.length == 1);
		assertTrue(isPackagePrivate(constructors[0].getModifiers()));
	}	
	
	@Test(expected = IllegalArgumentException.class)
	public void serverityCanNotBeNullTest() {
		new MessageTranslated(null, TRANSLATION);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void translationCanNotBeNullTest() {
		new MessageTranslated(ERROR, null);
	}
	
	@Test
	public void instancionTest() {
		MessageTranslated message = new MessageTranslated(ERROR, TRANSLATION);
		assertEquals(ERROR, message.getSeverity());
		assertEquals(TRANSLATION, message.getTranslation());
	}
	
}