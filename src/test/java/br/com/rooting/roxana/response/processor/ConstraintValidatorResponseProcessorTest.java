package br.com.rooting.roxana.response.processor;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.config.RoxanaPropertiesMockBuilder;
import br.com.rooting.roxana.message.*;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.translator.MockedTranslator;
import br.com.rooting.roxana.translator.Translator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ConstraintValidatorResponseProcessorTest extends UnitTest<ConstraintValidatorResponseProcessor> {

	private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
	private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();
	
	@Test
	void testClassIsPackagePrivateTest() {
		assertTrue(isPackagePrivate(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	void testClassExtendsMessageCreatorTest() {
		assertTrue(ResponseProcessor.class.isAssignableFrom(this.getUnitTestClass()));
	}
	
	@Test
	void testClassWasOnlyOnePackagePrivateConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
        assertEquals(1, constructors.length);
		assertTrue(isPackagePrivate(constructors[0].getModifiers()));
	}
	
	@Test
	void roxanaPropertiesCanNotBeNullTest() {
		Executable executable = () -> new MultiBusinessExceptionResponseProcessor(null, mock(MessageCreatorFactory.class), mock(ResponseProcessorManager.class));
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void messageCreatorFactoryCanNotBeNullTest() {
		Executable executable = () -> new MultiBusinessExceptionResponseProcessor(mock(RoxanaProperties.class), null, mock(ResponseProcessorManager.class));
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void responseProcessorManagerCanNotBeNullTest() {
		Executable executable = () -> new MultiBusinessExceptionResponseProcessor(mock(RoxanaProperties.class), mock(MessageCreatorFactory.class), null);
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void suppressOthersExceptionsCanNotBeNullTest() {
		RoxanaProperties roxanaProperties = new RoxanaPropertiesMockBuilder()
												.withSuppressOthersExceptions(null)
												.build();

		Executable executable = () -> new MultiBusinessExceptionResponseProcessor(roxanaProperties,
													mock(MessageCreatorFactory.class), 
													mock(ResponseProcessorManager.class));
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void processConstraintValiolationExceptionTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		ConstraintValidatorResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		
		Set<ConstraintViolation<ConstraintValidatorTest>> violations = VALIDATOR.validate(new ConstraintValidatorTest());
		ConstraintViolationException constraintException = new ConstraintViolationException(violations);
		
		ResponseEntity<Response> responseEntity = processor.process(constraintException);
		
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		
		Response response = responseEntity.getBody();
		assertNotNull(response);
		
		List<Message> messages = response.getMessages();
		assertNotNull(messages);
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
	
	private ConstraintValidatorResponseProcessor getReponseProcessorForTest(final RoxanaProperties roxanaProperties,
																			final ResponseProcessorManager responseProcessorManager) {

		return new ConstraintValidatorResponseProcessor(roxanaProperties, 
														this.getMessageCreatorFactory(roxanaProperties), 
														responseProcessorManager);
	}

	private RoxanaProperties getRoxanaProperties(final Boolean suppressOthersExceptions) {
		return new RoxanaPropertiesMockBuilder()
				.withResponseStrategy(ResponseStrategy.FULLY)
				.withSuppressOthersExceptions(suppressOthersExceptions).build();
	}

	private Translator getTranslator() {
		return new MockedTranslator();
	}

	private MessageCreatorFactory getMessageCreatorFactory(final RoxanaProperties roxanaProperties) {
		return new MockedMessageCreatorFactory(roxanaProperties, this.getTranslator());
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