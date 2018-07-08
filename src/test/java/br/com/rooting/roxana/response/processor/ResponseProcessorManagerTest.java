package br.com.rooting.roxana.response.processor;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.config.RoxanaPropertiesMockBuilder;
import br.com.rooting.roxana.exception.UnexpectedException;
import br.com.rooting.roxana.exception.mapper.BusinessException;
import br.com.rooting.roxana.exception.mapper.MultiBusinessException;
import br.com.rooting.roxana.message.*;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.translator.MockedTranslator;
import br.com.rooting.roxana.translator.Translator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.validation.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

import static br.com.rooting.roxana.translator.Translator.getInterpoledKeyOf;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ResponseProcessorManagerTest extends UnitTest<ResponseProcessorManager> {
	
	private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
	private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();

	@Test
	void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	void testClassIsASpringComponentTest() {
		assertTrue(this.getUnitTestClass().isAnnotationPresent(Component.class));
	}
	
	@Test
	void testClassWasOnlyOnePackagePrivateConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
        assertEquals(1, constructors.length);
		assertTrue(isPackagePrivate(constructors[0].getModifiers()));
	}
	
	@Test
	void roxanaPropertiesCanNotBeNullTest() {
		Executable executable = () -> new ResponseProcessorManager(null, mock(MessageCreatorFactory.class));
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void messageCreatorFactoryCanNotBeNullTest() {
		Executable executable = () -> new ResponseProcessorManager(mock(RoxanaProperties.class), null);
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void processGenericExceptionTest() throws Exception {
		String parameterValue = "test";
		
		ResponseProcessorManager manager = this.getResponseProcessorManagerForTest();
		ResponseEntity<Response> response = manager.getProcessedResponse(new MockedBusinessException(parameterValue));
		List<Message> messages = response.getBody().getMessages();
		
		assertEquals(1, messages.size());
		
		MessageFully message = (MessageFully) messages.get(0);
		assertEquals(this.getTranslator().translate(MockedBusinessException.ROXANA_KEY, message.getParameters()), message.getTranslation());
		assertEquals(1, message.getParameters().size());
		
		Parameter parameter_01 = message.getParameters().get(0);
		assertEquals(MockedBusinessException.PARAMETER_01_NAME, parameter_01.getName());
		assertEquals(parameterValue, parameter_01.getValue());
	}
	
	@Test
	void processMultiBusinessExceptionTest() throws Exception {
		String parameterValue_01 = "test01";
		String parameterValue_02 = "test02";
		
		MockedMultiBusinessException multi = new MockedMultiBusinessException(new MockedBusinessException(parameterValue_01),
																			  new MockedBusinessException(parameterValue_02));
		ResponseProcessorManager manager = this.getResponseProcessorManagerForTest();
		ResponseEntity<Response> response = manager.getProcessedResponse(multi);
		List<Message> messages = response.getBody().getMessages();
		
		assertEquals(2, messages.size());
		
		MessageFully message_01 = (MessageFully) messages.get(0);
		String translation_01 = this.getTranslator().translate(MockedBusinessException.ROXANA_KEY, message_01.getParameters());
		assertEquals(translation_01, message_01.getTranslation());
		assertEquals(1, message_01.getParameters().size());
		
		Parameter parameter_01_01 = message_01.getParameters().get(0);
		assertEquals(MockedBusinessException.PARAMETER_01_NAME, parameter_01_01.getName());
		assertEquals(parameterValue_01, parameter_01_01.getFormattedValue(Locale.CANADA));
		
		MessageFully message_02 = (MessageFully) messages.get(1);
		String translation_02 = this.getTranslator().translate(MockedBusinessException.ROXANA_KEY, message_02.getParameters());
		assertEquals(translation_02, message_02.getTranslation());
		assertEquals(1, message_02.getParameters().size());
		
		Parameter parameter_02_01 = message_02.getParameters().get(0);
		assertEquals(MockedBusinessException.PARAMETER_01_NAME, parameter_02_01.getName());
		assertEquals(parameterValue_02, parameter_02_01.getFormattedValue(Locale.CANADA));
	}
	
	@Test
	void processConstraintValidationExceptionTest() throws Exception {
		Set<ConstraintViolation<ConstraintValidatorTest>> violations = VALIDATOR.validate(new ConstraintValidatorTest());
		ConstraintViolationException constraintException = new ConstraintViolationException(violations);
		
		ResponseProcessorManager manager = this.getResponseProcessorManagerForTest();
		ResponseEntity<Response> response = manager.getProcessedResponse(constraintException);
		List<Message> messages = response.getBody().getMessages();
		
		assertEquals(2, messages.size());
		this.validateMinConstraintViolationMessage((MessageFully) messages.get(0));
		this.validateNotBlankConstraintViolationMessage((MessageFully) messages.get(1));
	}
	
	private void validateMinConstraintViolationMessage(final MessageFully message) {
		String translation = this.getTranslator().translate(ConstraintValidatorTest.MIN_KEY, message.getParameters());
		assertEquals(translation, message.getTranslation());
		assertEquals(MessageSeverity.ERROR, message.getSeverity());
		
		assertEquals(3, message.getParameters().size());
		
		Parameter parameter_01 = message.getParameters().get(0);
		assertEquals(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, parameter_01.getName());
		assertEquals("Positive Number", parameter_01.getValue());
		
		Parameter parameter_02 = message.getParameters().get(1);
		assertEquals(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, parameter_02.getName());
		assertEquals(-1, parameter_02.getValue());
		
		Parameter parameter_03 = message.getParameters().get(2);
		assertEquals("value", parameter_03.getName());
		assertEquals(1L, parameter_03.getValue());
	}
	
	private void validateNotBlankConstraintViolationMessage(final MessageFully message) {
		String translation = this.getTranslator().translate(ConstraintValidatorTest.NOT_BLANK_KEY, message.getParameters());
		assertEquals(translation, message.getTranslation());
		assertEquals(MessageSeverity.ERROR, message.getSeverity());
		
		assertEquals(2, message.getParameters().size());
		
		Parameter parameter_01 = message.getParameters().get(0);
		assertEquals(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, parameter_01.getName());
		assertEquals("Not Blank", parameter_01.getValue());
		
		Parameter parameter_02 = message.getParameters().get(1);
		assertEquals(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, parameter_02.getName());
		assertEquals("", parameter_02.getValue());
	}
	
	@Test
	void processWrappedGenericExceptionTest() throws Exception {
		String parameterValue = "test";
		RuntimeException runTimeException = new RuntimeException(new MockedBusinessException(parameterValue));
		ResponseProcessorManager manager = this.getResponseProcessorManagerForTest();
		ResponseEntity<Response> response = manager.getProcessedResponse(runTimeException);
		
		List<Message> messages = response.getBody().getMessages();
		
		assertEquals(1, messages.size());
		
		MessageFully message = (MessageFully) messages.get(0);
		assertEquals(this.getTranslator().translate(MockedBusinessException.ROXANA_KEY, message.getParameters()), message.getTranslation());
		assertEquals(1, message.getParameters().size());
		
		Parameter parameter_01 = message.getParameters().get(0);
		assertEquals(MockedBusinessException.PARAMETER_01_NAME, parameter_01.getName());
		assertEquals(parameterValue, parameter_01.getValue());
	}

		// TODO test if the not business exeception was looged correctly.
	@Test
	void processUnexpectedExceptionTest() throws Exception {
		ResponseProcessorManager manager = this.getResponseProcessorManagerForTest();
		ResponseEntity<Response> response = manager.getProcessedResponse(new NullPointerException());
		List<Message> messages = response.getBody().getMessages();
		
		assertEquals(1, messages.size());

		MessageFully message = (MessageFully) messages.get(0);
		assertEquals(UnexpectedException.ROXANA_KEY, message.getTranslation());
		assertEquals(0, message.getParameters().size());
	}
	
	private Translator getTranslator() {
		return new MockedTranslator();
	}

	private RoxanaProperties getRoxanaProperties() {
		RoxanaPropertiesMockBuilder builder = new RoxanaPropertiesMockBuilder();
		return builder.withResponseStrategy(ResponseStrategy.FULLY).build();
	}
	
	private MessageCreatorFactory getMessageCreatorFactory() {
        return new MockedMessageCreatorFactory(this.getRoxanaProperties(), this.getTranslator());
	}
	
	private ResponseProcessorManager getResponseProcessorManagerForTest() {
		return new ResponseProcessorManager(this.getRoxanaProperties(), this.getMessageCreatorFactory());
	}
	
	@BusinessException
	private static class MockedBusinessException extends Exception {
		
		private static final long serialVersionUID = 1L;
		
		private static final String ROXANA_KEY = getInterpoledKeyOf(MockedBusinessException.class.getName());
		
		private static final String PARAMETER_01_NAME = "parameter";
		
		@br.com.rooting.roxana.parameter.mapper.Param(PARAMETER_01_NAME)
		private final String parameter;
		
		private MockedBusinessException(final String parameter) {
			this.parameter = parameter;
		}
		
	}
	
	@MultiBusinessException
	private static class MockedMultiBusinessException extends Exception {
		
		private static final long serialVersionUID = 1L;

		private MockedMultiBusinessException(Exception... execeptions) {
			Stream.of(execeptions)
				  .forEach(this::addSuppressed);
		}
		
	}
	
	private static class ConstraintValidatorTest {
		
		private static final String PROPERTY_NAME_PARAMETER = "propertyName";
		private static final String INVALID_VALUE_PARAMETER = "invalidValue";
		
		private static final String MIN_KEY = "{javax.validation.constraints.Min.message}";
		private static final String NOT_BLANK_KEY = "{javax.validation.constraints.NotBlank.message}";
		
		@NotBlank
		private final String notBlank = "";
		
		@Min(1)
		private final Integer positiveNumber = -1;
		
	}
	
}