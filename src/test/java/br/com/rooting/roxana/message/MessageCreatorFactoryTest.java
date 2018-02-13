package br.com.rooting.roxana.message;

import static br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy.FULLY;
import static br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy.TRANSLATED;
import static br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy.UNCHANGED;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.stereotype.Component;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.config.RoxanaPropertiesMockBuilder;

public class MessageCreatorFactoryTest extends UnitTest<MessageCreatorFactory> {
	
	private static final MessageFullyCreator MOCKED_MESSAGE_FULLY_CREATOR = mock(MessageFullyCreator.class);
	
	private static final MessageTranslatedCreator MOCKED_MESSAGE_TRANSLATED_CREATOR = mock(MessageTranslatedCreator.class);
	
	private static final MessageUnchangedCreator MOCKED_MESSAGE_UNCHANGED_CREATOR = mock(MessageUnchangedCreator.class);
	
	@Test
	public void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}

	@Test
	public void testClassIsASpringComponentTest() {
		assertTrue(this.getUnitTestClass().isAnnotationPresent(Component.class));
	}
	
	@Test
	public void testClassWasOnlyOnePackagePrivateConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		assertTrue(constructors.length == 1);
		assertTrue(isPackagePrivate(constructors[0].getModifiers()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void roxanaPropertiesCanNotBeNull() {
		new MessageCreatorFactory(null, 
								  MOCKED_MESSAGE_FULLY_CREATOR, 
								  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
								  MOCKED_MESSAGE_UNCHANGED_CREATOR);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void messageFullyCreatorCanNotBeNull() {
		new MessageCreatorFactory(this.getMockedRoxanaProperties(TRANSLATED), 
								  null, 
								  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
								  MOCKED_MESSAGE_UNCHANGED_CREATOR);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void messageTranslatedCreatorCanNotBeNull() {
		new MessageCreatorFactory(this.getMockedRoxanaProperties(TRANSLATED), 
								  MOCKED_MESSAGE_FULLY_CREATOR, 
								  null, 
								  MOCKED_MESSAGE_UNCHANGED_CREATOR);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void messageUnchangedCreatorCanNotBeNull() {
		new MessageCreatorFactory(this.getMockedRoxanaProperties(TRANSLATED), 
								  MOCKED_MESSAGE_FULLY_CREATOR, 
								  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
								  null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void responseStrategyCanNotBeNull() {
		new MessageCreatorFactory(this.getMockedRoxanaProperties(null), 
								  MOCKED_MESSAGE_FULLY_CREATOR, 
								  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
								  MOCKED_MESSAGE_UNCHANGED_CREATOR);
	}
	
	@Test
	public void getFullyMessageCreator() {
		MessageCreatorFactory factory = new MessageCreatorFactory(this.getMockedRoxanaProperties(FULLY), 
																  MOCKED_MESSAGE_FULLY_CREATOR, 
																  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
																  MOCKED_MESSAGE_UNCHANGED_CREATOR);
		MessageCreator messageCreator = factory.getMessageCreator();
		assertTrue(messageCreator instanceof MessageFullyCreator);
	}
	
	@Test
	public void getTranslatedMessageCreator() {
		MessageCreatorFactory factory = new MessageCreatorFactory(this.getMockedRoxanaProperties(TRANSLATED), 
																  MOCKED_MESSAGE_FULLY_CREATOR, 
																  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
																  MOCKED_MESSAGE_UNCHANGED_CREATOR);
		MessageCreator messageCreator = factory.getMessageCreator();
		assertTrue(messageCreator instanceof MessageTranslatedCreator);
	}
	
	@Test
	public void getUnchangedMessageCreator() {
		MessageCreatorFactory factory = new MessageCreatorFactory(this.getMockedRoxanaProperties(UNCHANGED), 
																  MOCKED_MESSAGE_FULLY_CREATOR, 
																  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
																  MOCKED_MESSAGE_UNCHANGED_CREATOR);
		MessageCreator messageCreator = factory.getMessageCreator();
		assertTrue(messageCreator instanceof MessageUnchangedCreator);
	}
	
	@Test
	public void allResponsesStrategyMustBeSupportedTest() {
		Stream.of(ResponseStrategy.values()).forEach(s -> {
			MessageCreatorFactory factory = new MessageCreatorFactory(this.getMockedRoxanaProperties(s), 
																	  MOCKED_MESSAGE_FULLY_CREATOR, 
																	  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
																	  MOCKED_MESSAGE_UNCHANGED_CREATOR);
			factory.getMessageCreator();
		});
		assertTrue(true);
	}
	
	private RoxanaProperties getMockedRoxanaProperties(final ResponseStrategy responseStrategy) {
		RoxanaPropertiesMockBuilder builder = new RoxanaPropertiesMockBuilder();
		builder.setResponseStrategy(responseStrategy);
		return builder.build();
	}
	
}