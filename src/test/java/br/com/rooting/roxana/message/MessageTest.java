package br.com.rooting.roxana.message;

import br.com.rooting.roxana.UnitTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageTest extends UnitTest<Message> {
	
	// Todos os tipos de mensagem devem ser publica,
	// para que o programador possa usa-lo diretamente caso queira.
	@Test
	void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	void testClassIsAbstractTest() {
		assertTrue(isAbstract(this.getUnitTestClass().getModifiers()));
	}
	
	// Testa se a classe só tem um unico construtor
	// e se esse é package private para que somente classes do pacote possam extende-la.
	@Test
	void testClassWasOnlyOnePackagePrivateConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		assertTrue(constructors.length == 1);
		assertTrue(isPackagePrivate(constructors[0].getModifiers()));
	}
	
}