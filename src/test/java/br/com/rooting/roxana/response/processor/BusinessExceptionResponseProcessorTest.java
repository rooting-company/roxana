package br.com.rooting.roxana.response.processor;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.business.BusinessException;
import br.com.rooting.roxana.business.parameter.Parameter;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.config.RoxanaPropertiesMockBuilder;
import br.com.rooting.roxana.message.Message;
import br.com.rooting.roxana.message.MessageCreatorFactory;
import br.com.rooting.roxana.message.MessageFully;
import br.com.rooting.roxana.message.MessageSeverity;
import br.com.rooting.roxana.message.MockedMessageCreatorFactory;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.translator.MockedTranslator;
import br.com.rooting.roxana.translator.Translator;

public class BusinessExceptionResponseProcessorTest extends UnitTest<BusinessExceptionResponseProcessor> {

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
		new BusinessExceptionResponseProcessor(null, mock(MessageCreatorFactory.class), mock(ResponseProcessorManager.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void messageCreatorFactoryCanNotBeNullTest() {
		new BusinessExceptionResponseProcessor(mock(RoxanaProperties.class), null, mock(ResponseProcessorManager.class));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void responseProcessorManagerCanNotBeNullTest() {
		new BusinessExceptionResponseProcessor(mock(RoxanaProperties.class), mock(MessageCreatorFactory.class), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void suppressOthersExceptionsCanNotBeNullTest() {
		RoxanaProperties roxanaProperties = new RoxanaPropertiesMockBuilder()
												.withSuppressOthersExceptions(null)
												.build();
		
		new BusinessExceptionResponseProcessor(roxanaProperties, 
												mock(MessageCreatorFactory.class), 
												mock(ResponseProcessorManager.class));
	}
	
	@Test(expected = NullPointerException.class)
	public void notSupressOthersExceptionTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		BusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		processor.process(new NullPointerException());
	}
	
	@Test
	public void processNotCustomBusinessExceptionTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		BusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		ResponseEntity<Response> responseEntity = processor.process(new NotCustomBusinessException("test"));
		
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		
		Response response = responseEntity.getBody();
		assertNotNull(response);
		
		List<Message> messages = response.getMessages();
		assertNotNull(messages);
		assertEquals(1, messages.size());
		
		MessageFully message = (MessageFully) messages.get(0);
		assertEquals(NotCustomBusinessException.KEY, message.getKey());
		assertEquals(MessageSeverity.ERROR, message.getSeverity());
		
		String translation = this.getTranslator().translate(NotCustomBusinessException.KEY, message.getParameters());
		assertEquals(translation, message.getTranslation());
	}
	
	@Test
	public void processCustomResponseCodeBusinessExceptionTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		BusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		ResponseEntity<Response> responseEntity = processor.process(new CustomResponseCodeBusinessException("test"));
		
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		
		Response response = responseEntity.getBody();
		assertNotNull(response);
		
		List<Message> messages = response.getMessages();
		assertNotNull(messages);
		assertEquals(1, messages.size());
		
		MessageFully message = (MessageFully) messages.get(0);
		assertEquals(CustomResponseCodeBusinessException.KEY, message.getKey());
		assertEquals(MessageSeverity.ERROR, message.getSeverity());
		
		String translation = this.getTranslator().translate(CustomResponseCodeBusinessException.KEY, message.getParameters());
		assertEquals(translation, message.getTranslation());
	}
	
	@Test
	public void processCustomSeverityBusinessExceptionTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		BusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		ResponseEntity<Response> responseEntity = processor.process(new CustomSeverityBusinessException("test"));
		
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		
		Response response = responseEntity.getBody();
		assertNotNull(response);
		
		List<Message> messages = response.getMessages();
		assertNotNull(messages);
		assertEquals(1, messages.size());
		
		MessageFully message = (MessageFully) messages.get(0);
		assertEquals(CustomSeverityBusinessException.KEY, message.getKey());
		assertEquals(MessageSeverity.INFO, message.getSeverity());
		
		String translation = this.getTranslator().translate(CustomSeverityBusinessException.KEY, message.getParameters());
		assertEquals(translation, message.getTranslation());
	}
	
	@Test
	public void processCustomMessageBusinessExceptionTest() throws Exception {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		BusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		ResponseEntity<Response> responseEntity = processor.process(new CustomMessageKeyExceptionTest("test"));
		
		assertNotNull(responseEntity);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
		
		Response response = responseEntity.getBody();
		assertNotNull(response);
		
		List<Message> messages = response.getMessages();
		assertNotNull(messages);
		assertEquals(1, messages.size());
		
		MessageFully message = (MessageFully) messages.get(0);
		assertEquals(CUSTOM_KEY, message.getKey());
		assertEquals(MessageSeverity.ERROR, message.getSeverity());
		
		String translation = this.getTranslator().translate(CUSTOM_KEY, message.getParameters());
		assertEquals(translation, message.getTranslation());
	}
	
	@Test
	public void isAUnexpectedExceptionTest() {
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		BusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		boolean isUnexpected = processor.isUnexpectedException(new IllegalArgumentException());
		assertEquals(true, isUnexpected);
	}
	
	@Test
	public void isNotUnexpectedExceptionTest() {
		
		@BusinessException
		class ExceptionTest extends RuntimeException {
			
			private static final long serialVersionUID = 1L;
			
		}
		
		RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
		BusinessExceptionResponseProcessor processor = this.getReponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
		boolean isUnexpected = processor.isUnexpectedException(new ExceptionTest());
		assertEquals(false, isUnexpected);
	}
	
	private BusinessExceptionResponseProcessor getReponseProcessorForTest(final RoxanaProperties roxanaProperties, 
																		  final ResponseProcessorManager responseProcessorManager) {
		return new BusinessExceptionResponseProcessor(roxanaProperties, 
					  this.getMessageCreatorFactory(roxanaProperties), 
					  responseProcessorManager);
	}
	
	private RoxanaProperties getRoxanaProperties(final Boolean suppressOthersExceptions) {
		return new RoxanaPropertiesMockBuilder()
					.withResponseStrategy(ResponseStrategy.FULLY)
					.withSuppressOthersExceptions(suppressOthersExceptions)
					.build();
	}
	
	private Translator getTranslator() {
		return new MockedTranslator();
	}
	
	private MessageCreatorFactory getMessageCreatorFactory(final RoxanaProperties roxanaProperties) {
		return new MockedMessageCreatorFactory(roxanaProperties, this.getTranslator());
	}
	
	@BusinessException
	private static class NotCustomBusinessException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		private static final String KEY = "{br.com.rooting.roxana.response.processor.BusinessExceptionResponseProcessorTest$NotCustomBusinessException}";
		
		@Parameter
		private final String parameterString;
		
		private NotCustomBusinessException(final String parameterString) {
			this.parameterString = parameterString;
		}
		
	}
	
	@BusinessException(responseCode = HttpStatus.BAD_REQUEST)
	private static class CustomResponseCodeBusinessException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		private static final String KEY = "{br.com.rooting.roxana.response.processor.BusinessExceptionResponseProcessorTest$CustomResponseCodeBusinessException}";
		
		@Parameter
		private final String parameterString;
		
		private CustomResponseCodeBusinessException(final String parameterString) {
			this.parameterString = parameterString;
		}
		
	}
	
	@BusinessException(severity = MessageSeverity.INFO)
	private static class CustomSeverityBusinessException extends RuntimeException {
		
		private static final long serialVersionUID = 1L;
		
		private static final String KEY = "{br.com.rooting.roxana.response.processor.BusinessExceptionResponseProcessorTest$CustomSeverityBusinessException}";
		
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