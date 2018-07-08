package br.com.rooting.roxana.response;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ResponseBuilderTest extends UnitTest<ResponseBuilder> {

    @Test
    void testClassIsFinalTest() {
        assertTrue(Modifier.isFinal(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassWasOnlyOnePublicConstructorTest() {
        Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();

        assertEquals(1, constructors.length);

        Constructor<?> onlyConstructor = constructors[0];

        assertTrue(isPublic(onlyConstructor.getModifiers()));
        assertEquals(0, onlyConstructor.getParameterCount());
    }

    @Test
    void buildAppendingOneMessageTest() {
        ResponseBuilder builder = new ResponseBuilder();
        Message mockedMessage = mock(Message.class);
        builder.appendMessage(mockedMessage);
        Response response = builder.build();

        assertNotNull(response.getMessages());
        assertEquals(1, response.getMessages().size());
        assertTrue(response.getMessages().contains(mockedMessage));
    }

    @Test
    void buildAppendingMessagesTest() {
        List<Message> messages = this.getMessageMockedList();
        ResponseBuilder builder = new ResponseBuilder();
        builder.appendMessages(messages);
        Response response = builder.build();

        assertNotNull(response.getMessages());
        assertEquals(response.getMessages().size(), messages.size());
        assertTrue(response.getMessages().containsAll(messages));
    }

    @Test
    void buildAppendingMessagesArgsTest() {
        ResponseBuilder builder = new ResponseBuilder();
        Message message_01 = mock(Message.class);
        Message message_02 = mock(Message.class);
        Message message_03 = mock(Message.class);

        builder.appendMessages(message_01, message_02);
        builder.appendMessages(message_03);
        Response response = builder.build();

        assertNotNull(response.getMessages());
        assertEquals(3, response.getMessages().size());
        assertTrue(response.getMessages().contains(message_01));
        assertTrue(response.getMessages().contains(message_02));
        assertTrue(response.getMessages().contains(message_03));
    }

    @Test
    void buildAppendingAllTypeTest() {
        List<Message> messages_01 = this.getMessageMockedList();
        List<Message> messages_02 = this.getMessageMockedList();
        Message message_01 = mock(Message.class);
        Message message_02 = mock(Message.class);
        Message message_03 = mock(Message.class);
        Message message_04 = mock(Message.class);
        int size = messages_01.size() + messages_02.size() + 4;

        ResponseBuilder builder = new ResponseBuilder();
        builder.appendMessage(message_01);
        builder.appendMessages(messages_01);
        builder.appendMessage(message_02);
        builder.appendMessages(message_03, message_04);
        builder.appendMessages(messages_02);

        Response response = builder.build();
        assertNotNull(response.getMessages());
        assertEquals(response.getMessages().size(), size);
        assertTrue(response.getMessages().containsAll(messages_01));
        assertTrue(response.getMessages().containsAll(messages_02));
        assertTrue(response.getMessages().contains(message_01));
        assertTrue(response.getMessages().contains(message_02));
        assertTrue(response.getMessages().contains(message_03));
        assertTrue(response.getMessages().contains(message_04));
    }

    @Test
    void buildEmptyMessagesTest() {
        ResponseBuilder builder = new ResponseBuilder();
        Response response = builder.build();

        assertNotNull(response.getMessages());
        assertTrue(response.getMessages().isEmpty());
    }

    @Test
    void throwWhenAppendingNullMessageTest() {
        ResponseBuilder builder = new ResponseBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.appendMessage(null));
    }

    @Test
    void throwWhenAppendingNullListMessagesTest() {
        ResponseBuilder builder = new ResponseBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.appendMessages((List<Message>) null));
    }

    @Test
    void throwWhenAppendingNullMessagesTest() {
        ResponseBuilder builder = new ResponseBuilder();
        List<Message> messages = new ArrayList<>();
        messages.add(mock(Message.class));
        messages.add(mock(Message.class));
        messages.add(null);
        assertThrows(IllegalArgumentException.class, () -> builder.appendMessages(messages));
    }

    @Test
    void throwWhenAppendingNullMessagesArgsTest() {
        ResponseBuilder builder = new ResponseBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.appendMessages(mock(Message.class), null));
    }

    @Test
    void buildFilledTest() {
        ResponseBuilder builder = new ResponseBuilder();
        Object object = new Object();
        FilledResponse<Object> filledResponse = builder.buildFilled(object);
        assertEquals(object, filledResponse.getObject());
        assertNotNull(filledResponse.getMessages());
        assertTrue(filledResponse.getMessages().isEmpty());
    }

    @Test
    void buildFilledNullObjectTest() {
        ResponseBuilder builder = new ResponseBuilder();
        FilledResponse<Object> filledResponse = builder.buildFilled(null);
        assertNull(filledResponse.getObject());
        assertNotNull(filledResponse.getMessages());
        assertTrue(filledResponse.getMessages().isEmpty());
    }

    @Test
    void buildFilledAppendingOneMessageTest() {
        ResponseBuilder builder = new ResponseBuilder();
        Object object = new Object();
        Message mockedMessage = mock(Message.class);

        builder.appendMessage(mockedMessage);
        FilledResponse<Object> responseFilled = builder.buildFilled(object);

        assertEquals(object, responseFilled.getObject());
        assertNotNull(responseFilled.getMessages());
        assertEquals(1, responseFilled.getMessages().size());
        assertTrue(responseFilled.getMessages().contains(mockedMessage));
    }

    @Test
    void buildWithTest() {
        List<Message> messages = this.getMessageMockedList();
        int size = messages.size();
        Response response = ResponseBuilder.buildWith(messages);

        assertNotNull(response.getMessages());
        assertEquals(response.getMessages().size(), size);
        assertTrue(response.getMessages().containsAll(messages));
    }

    @Test
    void buildWithArgsTest() {
        Message message_01 = mock(Message.class);
        Message message_02 = mock(Message.class);
        Message message_03 = mock(Message.class);
        Response response = ResponseBuilder.buildWith(message_01, message_02, message_03);

        assertNotNull(response.getMessages());
        assertEquals(3, response.getMessages().size());
        assertTrue(response.getMessages().contains(message_01));
        assertTrue(response.getMessages().contains(message_02));
        assertTrue(response.getMessages().contains(message_03));
    }

    @Test
    void buildWithEmptyArgsTest() {
        Response responseWithNoMessage = ResponseBuilder.buildWith();

        assertNotNull(responseWithNoMessage.getMessages());
        assertTrue(responseWithNoMessage.getMessages().isEmpty());
    }

    @Test
    void buildFilledWithTest() {
        final Object object = new Object();
        List<Message> messages = this.getMessageMockedList();
        FilledResponse<Object> responseFilled = ResponseBuilder.buildFilledWith(object, messages);

        assertEquals(object, responseFilled.getObject());
        assertNotNull(responseFilled.getMessages());
        assertEquals(responseFilled.getMessages().size(), messages.size());
        assertTrue(responseFilled.getMessages().containsAll(messages));
    }

    @Test
    void buildFilledWithNullObjectTest() {
        List<Message> empty = new ArrayList<>();
        FilledResponse<Object> filledResponse = ResponseBuilder.buildFilledWith(null, empty);

        assertNull(filledResponse.getObject());
        assertNotNull(filledResponse.getMessages());
        assertTrue(filledResponse.getMessages().isEmpty());
    }

    @Test
    void buildFilledWithArgsTest() {
        Message message_01 = mock(Message.class);
        Message message_02 = mock(Message.class);
        Message message_03 = mock(Message.class);
        final Object object = new Object();
        FilledResponse<Object> responseFilled = ResponseBuilder.buildFilledWith(object, message_01, message_02, message_03);

        assertEquals(object, responseFilled.getObject());
        assertNotNull(responseFilled.getMessages());
        assertEquals(3, responseFilled.getMessages().size());
        assertTrue(responseFilled.getMessages().contains(message_01));
        assertTrue(responseFilled.getMessages().contains(message_02));
        assertTrue(responseFilled.getMessages().contains(message_03));
    }

    @Test
    void buildFilledWithEmptyArgsTest() {
        FilledResponse<Object> filledResponse = ResponseBuilder.buildFilledWith(null);
        assertNull(filledResponse.getObject());
        assertNotNull(filledResponse.getMessages());
        assertTrue(filledResponse.getMessages().isEmpty());
    }

    @Test
    void throwWhenBuildWithNullListMessagesTest() {
        assertThrows(IllegalArgumentException.class, () -> ResponseBuilder.buildWith((List<Message>) null));
    }

    @Test
    void throwWhenBuildWithNullMessageTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(mock(Message.class));
        messages.add(null);
        assertThrows(IllegalArgumentException.class, () -> ResponseBuilder.buildWith(messages));
    }

    @Test
    void throwWhenBuildWithNullMessageArgsTest() {
        Executable executable = () -> ResponseBuilder.buildWith(mock(Message.class), null, mock(Message.class));
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void throwWhenBuildFilledWithNullListMessagesTest() {
        Executable executable = () -> ResponseBuilder.buildFilledWith(new Object(), (List<Message>) null);
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    void throwWhenBuildFilledWithNullMessageTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(mock(Message.class));
        messages.add(null);
        assertThrows(IllegalArgumentException.class, () -> ResponseBuilder.buildFilledWith(new Object(), messages));
    }

    @Test
    void throwWhenBuildFilledWithNullMessageArgsTest() {
        Executable executable = () -> ResponseBuilder.buildFilledWith(new Object(), mock(Message.class), null, mock(Message.class));
        assertThrows(IllegalArgumentException.class, executable);
    }

    private List<Message> getMessageMockedList() {
        final Long quantity = Math.round(Math.random() * 10);

        List<Message> mockedMessages = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            mockedMessages.add(mock(Message.class));
        }
        return mockedMessages;
    }

}