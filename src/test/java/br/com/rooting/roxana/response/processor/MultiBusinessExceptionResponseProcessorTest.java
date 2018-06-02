package br.com.rooting.roxana.response.processor;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.business.BusinessException;
import br.com.rooting.roxana.business.MultiBusinessException;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.config.RoxanaPropertiesMockBuilder;
import br.com.rooting.roxana.message.Message;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.message.MessageFully;
import br.com.rooting.roxana.message.MessageSeverity;
import br.com.rooting.roxana.message.MockedMessageCreatorFactory;
import br.com.rooting.roxana.parameter.mapper.Parameter;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.translator.MockedTranslator;
import br.com.rooting.roxana.translator.Translator;

public class MultiBusinessExceptionResponseProcessorTest extends UnitTest<MultiBusinessExceptionResponseProcessor> {

	private static final String CUSTOM_KEY = "{custom.key}";
	
	@Test
	public void testClassIsPackagePrivateTest() {
		assertTrue(isPackagePrivate(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	public void testClassExtendsMessageCreatorTest() {
		assertTrue(ResponseProcessor.class.isAssignableFrom(this.getUnitTestClass()));
	}
	
	@Test
	public void testClassWasOnlyOnePackagePrivateConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		assertTrue(constructors.length == 1);
		assertTrue(isPackagePrivate(constructors[0].getModifiers()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void roxanaPropertiesCanNotBeNullTest() {
		new MultiBusinessExceptionResponseProcessor(null, mock(MessageCreatorFactory.class), mock(ResponseProcessorManager.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void messageCreatorFactoryCanNotBeNullTest() {
		new MultiBusinessExceptionResponseProcessor(mock(RoxanaProperties.class), null, mock(ResponseProcessorManager.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void responseProcessorManagerCanNotBeNullTest() {
		new MultiBusinessExceptionResponseProcessor(mock(RoxanaProperties.class), mock(MessageCreatorFactory.class), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void suppressOthersExceptionsCanNotBeNullTest() {
		RoxanaProperties roxanaProperties = new RoxanaPropertiesMockBuilder()
												.withSuppressOthersExceptions(null)
												.build();
		
		new MultiBusinessExceptionResponseProcessor(roxanaProperties, 
													mock(MessageCreatorFactory.class), 
													mock(ResponseProcessorManager.class));
	}
	
	@Test
	public void processNotCustomMultiBusinessExceptionTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		MultiBusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		
		NotCustomBusinessException notCustomBusinessException = new NotCustomBusinessException("test");
		CustomResponseCodeBusinessException customResponseCodeBusinessException = new CustomResponseCodeBusinessException("test");
		CustomSeverityBusinessException customSeverityBusinessException = new CustomSeverityBusinessException("test");
		CustomMessageKeyExceptionTest customMessageKeyExceptionTest = new CustomMessageKeyExceptionTest("test");
		
		NotCustomMultiBusinessException notCustomMultiBusinessException = new NotCustomMultiBusinessException(notCustomBusinessException,
																												customResponseCodeBusinessException,
																												customSeverityBusinessException,
																												customMessageKeyExceptionTest);
		
		ResponseEntity<Response> responseEntity = processor.process(notCustomMultiBusinessException);
		
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		
		Response response = responseEntity.getBody();
		assertNotNull(response);
		
		List<Message> messages = response.getMessages();
		assertNotNull(messages);
		assertEquals(4, messages.size());
		
		MessageFully messageCustomResponseCode = (MessageFully) messages.get(0);
		assertEquals(CustomResponseCodeBusinessException.KEY, messageCustomResponseCode.getKey());
		assertEquals(MessageSeverity.ERROR, messageCustomResponseCode.getSeverity());
		
		String translationCustomResponseCode = this.getTranslator().translate(CustomResponseCodeBusinessException.KEY, messageCustomResponseCode.getParameters());
		assertEquals(translationCustomResponseCode, messageCustomResponseCode.getTranslation());
		
		MessageFully messageCustomSeverity = (MessageFully) messages.get(1);
		assertEquals(CustomSeverityBusinessException.KEY, messageCustomSeverity.getKey());
		assertEquals(MessageSeverity.INFO, messageCustomSeverity.getSeverity());
		
		String translationCustomSeverity = this.getTranslator().translate(CustomSeverityBusinessException.KEY, messageCustomSeverity.getParameters());
		assertEquals(translationCustomSeverity, messageCustomSeverity.getTranslation());
		
		MessageFully messageNotCustom = (MessageFully) messages.get(2);
		assertEquals(NotCustomBusinessException.KEY, messageNotCustom.getKey());
		assertEquals(MessageSeverity.ERROR, messageNotCustom.getSeverity());
		
		String translationNotCustom = this.getTranslator().translate(NotCustomBusinessException.KEY, messageNotCustom.getParameters());
		assertEquals(translationNotCustom, messageNotCustom.getTranslation());
		
		MessageFully messageCustomMessageKey = (MessageFully) messages.get(3);
		assertEquals(CUSTOM_KEY, messageCustomMessageKey.getKey());
		assertEquals(MessageSeverity.ERROR, messageCustomMessageKey.getSeverity());
		
		String translationCustomMessageKey = this.getTranslator().translate(CUSTOM_KEY, messageCustomMessageKey.getParameters());
		assertEquals(translationCustomMessageKey, messageCustomMessageKey.getTranslation());
	}
	
	@Test
	public void processCustomResponseCodeMultiBusinessExceptionTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		MultiBusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		
		NotCustomBusinessException notCustomBusinessException = new NotCustomBusinessException("test");
		CustomResponseCodeBusinessException customResponseCodeBusinessException = new CustomResponseCodeBusinessException("test");
		CustomSeverityBusinessException customSeverityBusinessException = new CustomSeverityBusinessException("test");
		CustomMessageKeyExceptionTest customMessageKeyExceptionTest = new CustomMessageKeyExceptionTest("test");
		
		CustomResponseCodeMultiException customResponseCodeMultiBusinessException = new CustomResponseCodeMultiException(notCustomBusinessException,
																												customResponseCodeBusinessException,
																												customSeverityBusinessException,
																												customMessageKeyExceptionTest);
		
		ResponseEntity<Response> responseEntity = processor.process(customResponseCodeMultiBusinessException);
		
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		
		Response response = responseEntity.getBody();
		assertNotNull(response);
		
		List<Message> messages = response.getMessages();
		assertNotNull(messages);
		assertEquals(4, messages.size());
		
		MessageFully messageCustomResponseCode = (MessageFully) messages.get(0);
		assertEquals(CustomResponseCodeBusinessException.KEY, messageCustomResponseCode.getKey());
		assertEquals(MessageSeverity.ERROR, messageCustomResponseCode.getSeverity());
		
		String translationCustomResponseCode = this.getTranslator().translate(CustomResponseCodeBusinessException.KEY, messageCustomResponseCode.getParameters());
		assertEquals(translationCustomResponseCode, messageCustomResponseCode.getTranslation());
		
		MessageFully messageCustomSeverity = (MessageFully) messages.get(1);
		assertEquals(CustomSeverityBusinessException.KEY, messageCustomSeverity.getKey());
		assertEquals(MessageSeverity.INFO, messageCustomSeverity.getSeverity());
		
		String translationCustomSeverity = this.getTranslator().translate(CustomSeverityBusinessException.KEY, messageCustomSeverity.getParameters());
		assertEquals(translationCustomSeverity, messageCustomSeverity.getTranslation());
		
		MessageFully messageNotCustom = (MessageFully) messages.get(2);
		assertEquals(NotCustomBusinessException.KEY, messageNotCustom.getKey());
		assertEquals(MessageSeverity.ERROR, messageNotCustom.getSeverity());
		
		String translationNotCustom = this.getTranslator().translate(NotCustomBusinessException.KEY, messageNotCustom.getParameters());
		assertEquals(translationNotCustom, messageNotCustom.getTranslation());
		
		MessageFully messageCustomMessageKey = (MessageFully) messages.get(3);
		assertEquals(CUSTOM_KEY, messageCustomMessageKey.getKey());
		assertEquals(MessageSeverity.ERROR, messageCustomMessageKey.getSeverity());
		
		String translationCustomMessageKey = this.getTranslator().translate(CUSTOM_KEY, messageCustomMessageKey.getParameters());
		assertEquals(translationCustomMessageKey, messageCustomMessageKey.getTranslation());
	}
	
	@Test
	public void onlyBusinessExceptionAreConsideredTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		MultiBusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		
		NotCustomMultiBusinessException multiBusinessExpcetion = new NotCustomMultiBusinessException(new NullPointerException(), 
																									 new IllegalArgumentException());
		
		ResponseEntity<Response> responseEntity = processor.process(multiBusinessExpcetion);
		
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		
		Response response = responseEntity.getBody();
		assertNotNull(response);
		
		List<Message> messages = response.getMessages();
		assertNotNull(messages);
		assertEquals(0, messages.size());
	}
	
	private MultiBusinessExceptionResponseProcessor getReponseProcessorForTest(final RoxanaProperties roxanaProperties, 
																				final ResponseProcessorManager responseProcessorManager) {
		
		return new MultiBusinessExceptionResponseProcessor(roxanaProperties, 
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
	
	@MultiBusinessException
	private static class NotCustomMultiBusinessException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		private NotCustomMultiBusinessException(final Exception... businessException) {
			Stream.of(businessException)
				  .forEach(this::addSuppressed);
		}
	}
	
	@MultiBusinessException(responseCode = HttpStatus.BAD_REQUEST)
	private static class CustomResponseCodeMultiException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		private CustomResponseCodeMultiException(final Exception... businessException) {
			Stream.of(businessException)
				  .forEach(this::addSuppressed);
		}
	}
	
	@BusinessException
	private static class NotCustomBusinessException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		private static final String KEY = "{br.com.rooting.roxana.response.processor.MultiBusinessExceptionResponseProcessorTest$NotCustomBusinessException}";
		
		@Parameter
		private final String parameterString;
		
		private NotCustomBusinessException(final String parameterString) {
			this.parameterString = parameterString;
		}
		
	}
	
	@BusinessException(responseCode = HttpStatus.BAD_REQUEST)
	private static class CustomResponseCodeBusinessException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		private static final String KEY = "{br.com.rooting.roxana.response.processor.MultiBusinessExceptionResponseProcessorTest$CustomResponseCodeBusinessException}";
		
		@Parameter
		private final String parameterString;
		
		private CustomResponseCodeBusinessException(final String parameterString) {
			this.parameterString = parameterString;
		}
		
	}
	
	@BusinessException(severity = MessageSeverity.INFO)
	private static class CustomSeverityBusinessException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		private static final String KEY = "{br.com.rooting.roxana.response.processor.MultiBusinessExceptionResponseProcessorTest$CustomSeverityBusinessException}";
		
		@Parameter
		private final String parameterString;
		
		private CustomSeverityBusinessException(final String parameterString) {
			this.parameterString = parameterString;
		}
		
	}
	
	@BusinessException(message = CUSTOM_KEY)
	private static class CustomMessageKeyExceptionTest extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		@Parameter
		private final String parameterString;
		
		private CustomMessageKeyExceptionTest(final String parameterString) {
			this.parameterString = parameterString;
		}
		
	}
	
}