package br.com.rooting.roxana.message;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.parameter.Parameter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static br.com.rooting.roxana.message.MessageSeverity.ERROR;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MessageUnchangedTest extends UnitTest<MessageUnchanged> {
	
	private static final ArrayList<Parameter> EMPTY_PARAMETER_LIST = new ArrayList<>();
	private static final String KEY = "key";
	
	// Todos os tipos de mensagem devem ser publica,
	// para que o programador possa usa-lo diretamente caso queira.
	@Test
	void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	// Testa se a classe é filha de Message.
	@Test
	void testClassExtendsMessageTest() {
		assertTrue(Message.class.isAssignableFrom(this.getUnitTestClass()));
	}
	
	@Test
	void testClassIsFinalTest() {
		assertTrue(isFinal(this.getUnitTestClass().getModifiers()));
	}
	
	// Testa se a classe só tem um unico construtor
	// e se esse é package private para que somente a classe Creator devida possa cria-lo.
	@Test
	void testClassWasOnlyOnePackagePrivateConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		assertEquals(1, constructors.length);
		assertTrue(isPackagePrivate(constructors[0].getModifiers()));
	}
	
	@Test
	void serverityCanNotBeNullTest() {
		assertThrows(IllegalArgumentException.class, () -> new MessageUnchanged(null, KEY, EMPTY_PARAMETER_LIST));
	}
	
	@Test
	void keyCanNotBeNullTest() {
		assertThrows(IllegalArgumentException.class, () -> new MessageUnchanged(ERROR, null, EMPTY_PARAMETER_LIST));
	}
	
	@Test
	void parametersCanNotBeNullTest() {
		assertThrows(IllegalArgumentException.class, () -> new MessageUnchanged(ERROR, KEY, null));
	}
	
	@Test
	void instancionTest() {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(mock(Parameter.class));
		parameters.add(mock(Parameter.class));
		parameters.add(mock(Parameter.class));
		
		MessageUnchanged message = new MessageUnchanged(ERROR, KEY, parameters);
		
		assertEquals(ERROR, message.getSeverity());
		assertEquals(KEY, message.getKey());
		assertEquals(message.getParameters().size(), parameters.size());
		assertTrue(message.getParameters().containsAll(parameters));
	}
	
	@Test
	void parametersAreUnmodifiableTest() {
		MessageUnchanged message = new MessageUnchanged(ERROR,
														KEY,
														EMPTY_PARAMETER_LIST);
		assertThrows(UnsupportedOperationException.class, () -> message.getParameters().add(mock(Parameter.class)));
	}
	
}
