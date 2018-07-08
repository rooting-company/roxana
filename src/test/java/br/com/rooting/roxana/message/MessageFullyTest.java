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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

// TODO Avaliar como evitar duplicacao de testes para classes de Message.
class MessageFullyTest extends UnitTest<MessageFully> {
	
	private static final ArrayList<Parameter> EMPTY_PARAMETER_LIST = new ArrayList<Parameter>();
	private static final String TRANSLATION = "translation";
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
		assertTrue(constructors.length == 1);
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
		assertTrue(message.getParameters().size() == parameters.size());
		assertTrue(message.getParameters().containsAll(parameters));
	}
	
	@Test
	void parametersAreUnmodifiableTest() {
		MessageFully message = new MessageFully(ERROR, KEY, PT_BR.getTag(), TRANSLATION, EMPTY_PARAMETER_LIST);
		assertThrows(UnsupportedOperationException.class, () -> message.getParameters().add(mock(Parameter.class)));
	}
	
}