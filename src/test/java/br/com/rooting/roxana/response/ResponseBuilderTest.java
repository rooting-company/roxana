package br.com.rooting.roxana.response;

import static java.lang.reflect.Modifier.isPublic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.Message;
import br.com.rooting.roxana.response.FilledResponse;
import br.com.rooting.roxana.response.Response;
import br.com.rooting.roxana.response.ResponseBuilder;

public class ResponseBuilderTest extends UnitTest<ResponseBuilder> {
	
	// Testa se a classe ResponseBuilder é final.
	@Test
	public void testClassIsFinalTest() {
		assertTrue(Modifier.isFinal(this.getUnitTestClass().getModifiers()));
	}
	
	// Testa se a classe ResponseBuilder só tem um construtor e se esse é publico.
	@Test
	public void testClassWasOnlyOnePublicConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		
		// O response builder só deve ter um construtor vazio, 
		// pois existem metodos staticos para supri a necessidade dos demais.
		assertTrue(constructors.length == 1);
		
		Constructor<?> onlyConstructor = constructors[0];
		
		// Cliente deve conseguir instanciar o builder normalmente.
		// Portanto o construtor precisa ser public e sem parametros.
		assertTrue(isPublic(onlyConstructor.getModifiers()));
		assertTrue(onlyConstructor.getParameterCount() == 0);
	}
	
	// Testa a criação de Response através do appendMessage.
	@Test
	public void buildAppendingOneMessageTest() {
		ResponseBuilder builder = new ResponseBuilder();
		Message mockedMessage = mock(Message.class);
		builder.appendMessage(mockedMessage);
		Response response = builder.build();
		
		assertTrue(response.getMessages() != null);
		assertTrue(response.getMessages().size() == 1);
		assertTrue(response.getMessages().contains(mockedMessage));
	}
	
	// Testa a criação de Response através appendMessages recebendo uma lista.
	@Test
	public void buildAppendingMessagesTest() {
		List<Message> messages = this.getMessageMockedList();
		ResponseBuilder builder = new ResponseBuilder();
		builder.appendMessages(messages);
		Response response = builder.build();
		
		assertTrue(response.getMessages() != null);
		assertTrue(response.getMessages().size() == messages.size());
		assertTrue(response.getMessages().containsAll(messages));
	}
	
	// Testa a criação de Response através appendMessages recebendo um var Args.
	@Test
	public void buildAppendingMessagesArgsTest() {
		ResponseBuilder builder = new ResponseBuilder();
		Message message_01 = mock(Message.class);
		Message message_02 = mock(Message.class);
		Message message_03 = mock(Message.class);
		
		builder.appendMessages(message_01, message_02);
		builder.appendMessages(message_03);
		Response response = builder.build();
		
		assertTrue(response.getMessages() != null);
		assertTrue(response.getMessages().size() == 3);
		assertTrue(response.getMessages().contains(message_01));
		assertTrue(response.getMessages().contains(message_02));
		assertTrue(response.getMessages().contains(message_03));
	}
	
	// Testa a criação de Response através de todos os tipos de appending.
	@Test
	public void buildAppedingAllTypeTest() {
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
		assertTrue(response.getMessages() != null);
		assertTrue(response.getMessages().size() == size);
		assertTrue(response.getMessages().containsAll(messages_01));
		assertTrue(response.getMessages().containsAll(messages_02));
		assertTrue(response.getMessages().contains(message_01));
		assertTrue(response.getMessages().contains(message_02));
		assertTrue(response.getMessages().contains(message_03));
		assertTrue(response.getMessages().contains(message_04));
	}
	
	// Testa a criação de Response sem nenhuma message.
	@Test
	public void buildEmptyMessagesTest() {
		ResponseBuilder builder = new ResponseBuilder();
		Response response = builder.build();
		
		assertTrue(response.getMessages() != null);
		assertTrue(response.getMessages().isEmpty());
	}
	
	// Testa se a exception é lançada ao dar appendMessage passando null.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenAppendingNullMessageTest() {
		ResponseBuilder builder = new ResponseBuilder();
		builder.appendMessage(null);
	}
	
	// Testa se a exception é lançada ao dar appendMessages (List) passando null.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenAppendingNullListMessagesTest() {
		ResponseBuilder builder = new ResponseBuilder();
		List<Message> nullMessageList = null;
		builder.appendMessages(nullMessageList);
	}
	
	// Testa se a exception é lançada ao dar appendMessages (List)
	// passando alguma message null dentro da lista.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenAppendingNullMessagesTest() {
		ResponseBuilder builder = new ResponseBuilder();
		List<Message> messages = new ArrayList<>();
		messages.add(mock(Message.class));
		messages.add(mock(Message.class));
		messages.add(null);
		builder.appendMessages(messages);
	}
	
	// Testa se a exception é lançada ao dar appendMessages (Args) passando alguma message null.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenAppendingNullMessagesArgsTest() {
		ResponseBuilder builder = new ResponseBuilder();
		builder.appendMessages(mock(Message.class), null);
	}
	
	// Testa a criação de FilledResponse usando buildFilled;
	@Test
	public void buildFilledTest() {
		ResponseBuilder builder = new ResponseBuilder();
		Object object = new Object();
		FilledResponse<Object> filledResponse = builder.buildFilled(object);
		assertEquals(object, filledResponse.getObject());
		assertNotNull(filledResponse.getMessages());
		assertTrue(filledResponse.getMessages().isEmpty());
	}
	
	// Testa a criação de FilledResponse com um objeto null.
	@Test
	public void buildFilledNullObjectTest() {
		ResponseBuilder builder = new ResponseBuilder();
		FilledResponse<Object> filledResponse = builder.buildFilled(null);
		assertNull(filledResponse.getObject());
		assertNotNull(filledResponse.getMessages());
		assertTrue(filledResponse.getMessages().isEmpty());
	}
	
	// Testa a criação de um FilledResponse que contenha também uma message.
	@Test
	public void buildFilledAppendingOneMessageTest() {
		ResponseBuilder builder = new ResponseBuilder();
		Object object = new Object();
		Message mockedMessage = mock(Message.class);
		
		builder.appendMessage(mockedMessage);
		FilledResponse<Object> responseFilled = builder.buildFilled(object);
		
		assertEquals(object, responseFilled.getObject());
		assertNotNull(responseFilled.getMessages());
		assertTrue(responseFilled.getMessages().size() == 1);
		assertTrue(responseFilled.getMessages().contains(mockedMessage));
	}
	
	// Testa a criação de um Response usando o metodo static buildWith.
	@Test
	public void buildWithTest() {
		List<Message> messages = this.getMessageMockedList();
		int size = messages.size();
		Response response = ResponseBuilder.buildWith(messages);
		
		assertNotNull(response.getMessages());
		assertTrue(response.getMessages().size() == size);
		assertTrue(response.getMessages().containsAll(messages));
	}
	
	// Testa a criação de um Response usando o metodo static buildWith que aceita Messages como var args.
	@Test
	public void buildWithArgsTest() {
		Message message_01 = mock(Message.class);
		Message message_02 = mock(Message.class);
		Message message_03 = mock(Message.class);
		Response response = ResponseBuilder.buildWith(message_01, message_02, message_03);
		
		assertTrue(response.getMessages() != null);
		assertTrue(response.getMessages().size() == 3);
		assertTrue(response.getMessages().contains(message_01));
		assertTrue(response.getMessages().contains(message_02));
		assertTrue(response.getMessages().contains(message_03));
	}
	
	@Test
	public void buildWithEmptyArgsTest() {
		Response resposeWithNoMessage = ResponseBuilder.buildWith();
		
		assertNotNull(resposeWithNoMessage.getMessages());
		assertTrue(resposeWithNoMessage.getMessages().isEmpty());
	}
	
	// Testa a criação de um FilledResponse usando o metodo static buildFilledWith.
	@Test
	public void buildFilledWithTest() {
		final Object object = new Object();
		List<Message> messages = this.getMessageMockedList();
		FilledResponse<Object> responseFilled = ResponseBuilder.buildFilledWith(object, messages);
		
		assertEquals(object, responseFilled.getObject());
		assertNotNull(responseFilled.getMessages());
		assertTrue(responseFilled.getMessages().size() == messages.size());
		assertTrue(responseFilled.getMessages().containsAll(messages));
	}
	
	// Testa a criaçao de um FilledResponse com um object null através 
	// do metodo static buildFilledWith.
	@Test
	public void buildFilledWithNullObjectTest() {
		List<Message> empty = new ArrayList<>();
		FilledResponse<Object> filledResponse = ResponseBuilder.buildFilledWith(null, empty);
		
		assertNull(filledResponse.getObject());
		assertNotNull(filledResponse.getMessages());
		assertTrue(filledResponse.getMessages().isEmpty());
	}
	
	// Testa a criação de um FilledResponse usando o metodo static 
	// buildFilledWith que recebe Messages como var args.
	@Test
	public void buildFilledWithArgsTest() {
		Message message_01 = mock(Message.class);
		Message message_02 = mock(Message.class);
		Message message_03 = mock(Message.class);
		final Object object = new Object();
		FilledResponse<Object> responseFilled = ResponseBuilder.buildFilledWith(object, message_01, message_02, message_03);
		
		assertEquals(object, responseFilled.getObject());
		assertNotNull(responseFilled.getMessages());
		assertTrue(responseFilled.getMessages().size() == 3);
		assertTrue(responseFilled.getMessages().contains(message_01));
		assertTrue(responseFilled.getMessages().contains(message_02));
		assertTrue(responseFilled.getMessages().contains(message_03));
	}
	
	// Testa a criaçao de um FilledResponse com um object null 
	// através do metodo static buildFilledWith que aceita um var args.
	@Test
	public void buildFilledWithEmptyArgsTest() {
		FilledResponse<Object> filledResponse = ResponseBuilder.buildFilledWith(null);
		assertNull(filledResponse.getObject());
		assertNotNull(filledResponse.getMessages());
		assertTrue(filledResponse.getMessages().isEmpty());
	}
	
	// Testa se é lançada a exception quando passado uma 
	// lista nula para o metodo static buildWith.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenBuildWithNullListMessagesTest() {
		List<Message> nullMessageList = null;
		ResponseBuilder.buildWith(nullMessageList);
	}
	
	// Testa se é lançada a exception quando é passado uma lista que 
	// contém messages nulas para o metodo static buildWith.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenBuildWithNullMessageTest() {
		List<Message> messages = new ArrayList<>();
		messages.add(mock(Message.class));
		messages.add(null);
		ResponseBuilder.buildWith(messages);
	}
	
	// Testa se é lançada a exception quando passado null como var args 
	// para o metodo static buildWith.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenBuildWithNullMessageArgsTest() {
		ResponseBuilder.buildWith(mock(Message.class), null, mock(Message.class));
	}
	
	// Testa se é lançada a exception quando passado uma 
	// lista nula para o metodo static buildFilledWith.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenBuildFilledWithNullListMessagesTest() {
		List<Message> nullMessageList = null;
		ResponseBuilder.buildFilledWith(new Object(), nullMessageList);
	}
	
	// Testa se é lançada a exception quando é passado uma lista que 
	// contém messages nulas para o metodo static buildFilledWith.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenBuildFilledWithNullMessageTest() {
		List<Message> messages = new ArrayList<>();
		messages.add(mock(Message.class));
		messages.add(null);
		ResponseBuilder.buildFilledWith(new Object(), messages);
	}
	
	// Testa se é lançada a exception quando passado null como var args 
	// para o metodo static buildFilledWith.
	@Test(expected = IllegalArgumentException.class)
	public void throwWhenBuildFilledWithNullMessageArgsTest() {
		ResponseBuilder.buildFilledWith(new Object(), mock(Message.class), null, mock(Message.class));
	}
	
	private List<Message> getMessageMockedList() {
		final Long quantidade = Math.round(Math.random() * 10);
		
		List<Message> mockedMessages = new ArrayList<>();
		for(int i = 0; i < quantidade; i++) {
			mockedMessages.add(mock(Message.class));
		}
		return mockedMessages;
	}
	
}