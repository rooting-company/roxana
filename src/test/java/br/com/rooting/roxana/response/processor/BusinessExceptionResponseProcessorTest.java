package br.com.rooting.roxana.response.processor;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.config.RoxanaPropertiesMockBuilder;
import br.com.rooting.roxana.exception.mapper.BusinessException;
import br.com.rooting.roxana.message.*;
import br.com.rooting.roxana.parameter.mapper.Param;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.translator.MockedTranslator;
import br.com.rooting.roxana.translator.Translator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Constructor;
import java.util.List;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BusinessExceptionResponseProcessorTest extends UnitTest<BusinessExceptionResponseProcessor> {

    private static final String CUSTOM_KEY = "{custom.key}";

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
        Executable executable = () -> new BusinessExceptionResponseProcessor(null, mock(MessageCreatorFactory.class), mock(ResponseProcessorManager.class));
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void messageCreatorFactoryCanNotBeNullTest() {
        Executable executable = () -> new BusinessExceptionResponseProcessor(mock(RoxanaProperties.class), null, mock(ResponseProcessorManager.class));
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void responseProcessorManagerCanNotBeNullTest() {
        Executable executable = () -> new BusinessExceptionResponseProcessor(mock(RoxanaProperties.class), mock(MessageCreatorFactory.class), null);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void suppressOthersExceptionsCanNotBeNullTest() {
        RoxanaProperties roxanaProperties = new RoxanaPropertiesMockBuilder()
                .withSuppressOthersExceptions(null)
                .build();

        Executable executable = () -> new BusinessExceptionResponseProcessor(roxanaProperties,
                mock(MessageCreatorFactory.class),
                mock(ResponseProcessorManager.class));
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void notSuppressOthersExceptionTest() {
        RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
        BusinessExceptionResponseProcessor processor = this.getResponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
        assertThrows(NullPointerException.class, () -> processor.process(new NullPointerException()));
    }

    @Test
    void processNotCustomBusinessExceptionTest() throws Exception {
        RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
        BusinessExceptionResponseProcessor processor = this.getResponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
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
    void processCustomResponseCodeBusinessExceptionTest() throws Exception {
        RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
        BusinessExceptionResponseProcessor processor = this.getResponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
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
    void processCustomSeverityBusinessExceptionTest() throws Exception {
        RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
        BusinessExceptionResponseProcessor processor = this.getResponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
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
    void processCustomMessageBusinessExceptionTest() throws Exception {
        RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
        BusinessExceptionResponseProcessor processor = this.getResponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
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
    void isAUnexpectedExceptionTest() {
        RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
        BusinessExceptionResponseProcessor processor = this.getResponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
        boolean isUnexpected = processor.isUnexpectedException(new IllegalArgumentException());
        assertTrue(isUnexpected);
    }

    @Test
    void isNotUnexpectedExceptionTest() {

        @BusinessException
        class ExceptionTest extends RuntimeException {

            private static final long serialVersionUID = 1L;

        }

        RoxanaProperties roxanaProperties = this.getRoxanaProperties(false);
        BusinessExceptionResponseProcessor processor = this.getResponseProcessorForTest(roxanaProperties, mock(ResponseProcessorManager.class));
        boolean isUnexpected = processor.isUnexpectedException(new ExceptionTest());
        assertFalse(isUnexpected);
    }

    private BusinessExceptionResponseProcessor getResponseProcessorForTest(final RoxanaProperties roxanaProperties,
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

        @Param
        private final String parameterString;

        private NotCustomBusinessException(final String parameterString) {
            this.parameterString = parameterString;
        }

    }

    @BusinessException(responseCode = HttpStatus.BAD_REQUEST)
    private static class CustomResponseCodeBusinessException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private static final String KEY = "{br.com.rooting.roxana.response.processor.BusinessExceptionResponseProcessorTest$CustomResponseCodeBusinessException}";

        @Param
        private final String parameterString;

        private CustomResponseCodeBusinessException(final String parameterString) {
            this.parameterString = parameterString;
        }

    }

    @BusinessException(severity = MessageSeverity.INFO)
    private static class CustomSeverityBusinessException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private static final String KEY = "{br.com.rooting.roxana.response.processor.BusinessExceptionResponseProcessorTest$CustomSeverityBusinessException}";

        @Param
        private final String parameterString;

        private CustomSeverityBusinessException(final String parameterString) {
            this.parameterString = parameterString;
        }

    }

    @BusinessException(message = CUSTOM_KEY)
    private static class CustomMessageKeyExceptionTest extends RuntimeException {

        private static final long serialVersionUID = 1L;

        @Param
        private final String parameterString;

        private CustomMessageKeyExceptionTest(final String parameterString) {
            this.parameterString = parameterString;
        }

    }

}