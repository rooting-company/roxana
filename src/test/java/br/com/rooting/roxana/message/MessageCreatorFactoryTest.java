package br.com.rooting.roxana.message;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.config.RoxanaPropertiesMockBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import static br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy.*;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MessageCreatorFactoryTest extends UnitTest<MessageCreatorFactory> {
	
	private static final MessageFullyCreator MOCKED_MESSAGE_FULLY_CREATOR = mock(MessageFullyCreator.class);
	
	private static final MessageTranslatedCreator MOCKED_MESSAGE_TRANSLATED_CREATOR = mock(MessageTranslatedCreator.class);
	
	private static final MessageUnchangedCreator MOCKED_MESSAGE_UNCHANGED_CREATOR = mock(MessageUnchangedCreator.class);
	
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
		Executable executable = () -> new MessageCreatorFactory(null,
															    MOCKED_MESSAGE_FULLY_CREATOR,
															    MOCKED_MESSAGE_TRANSLATED_CREATOR,
															    MOCKED_MESSAGE_UNCHANGED_CREATOR);
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void messageFullyCreatorCanNotBeNullTest() {
		Executable executable = () -> new MessageCreatorFactory(this.getMockedRoxanaProperties(TRANSLATED),
															    null,
															    MOCKED_MESSAGE_TRANSLATED_CREATOR,
															    MOCKED_MESSAGE_UNCHANGED_CREATOR);
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void messageTranslatedCreatorCanNotBeNullTest() {
		Executable executable = () -> new MessageCreatorFactory(this.getMockedRoxanaProperties(TRANSLATED),
															    MOCKED_MESSAGE_FULLY_CREATOR,
															    null,
															    MOCKED_MESSAGE_UNCHANGED_CREATOR);
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void messageUnchangedCreatorCanNotBeNullTest() {
		Executable executable = () -> new MessageCreatorFactory(this.getMockedRoxanaProperties(TRANSLATED),
															  	MOCKED_MESSAGE_FULLY_CREATOR,
															  	MOCKED_MESSAGE_TRANSLATED_CREATOR,
															  	null);
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void responseStrategyCanNotBeNullTest() {
		Executable executable = () -> new MessageCreatorFactory(this.getMockedRoxanaProperties(null),
															    MOCKED_MESSAGE_FULLY_CREATOR,
															    MOCKED_MESSAGE_TRANSLATED_CREATOR,
															    MOCKED_MESSAGE_UNCHANGED_CREATOR);
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void getFullyMessageCreatorTest() {
		MessageCreatorFactory factory = new MessageCreatorFactory(this.getMockedRoxanaProperties(FULLY), 
																  MOCKED_MESSAGE_FULLY_CREATOR, 
																  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
																  MOCKED_MESSAGE_UNCHANGED_CREATOR);
		MessageCreator messageCreator = factory.getMessageCreator();
		assertTrue(messageCreator instanceof MessageFullyCreator);
	}
	
	@Test
	void getTranslatedMessageCreatorTest() {
		MessageCreatorFactory factory = new MessageCreatorFactory(this.getMockedRoxanaProperties(TRANSLATED), 
																  MOCKED_MESSAGE_FULLY_CREATOR, 
																  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
																  MOCKED_MESSAGE_UNCHANGED_CREATOR);
		MessageCreator messageCreator = factory.getMessageCreator();
		assertTrue(messageCreator instanceof MessageTranslatedCreator);
	}
	
	@Test
	void getUnchangedMessageCreatorTest() {
		MessageCreatorFactory factory = new MessageCreatorFactory(this.getMockedRoxanaProperties(UNCHANGED), 
																  MOCKED_MESSAGE_FULLY_CREATOR, 
																  MOCKED_MESSAGE_TRANSLATED_CREATOR, 
																  MOCKED_MESSAGE_UNCHANGED_CREATOR);
		MessageCreator messageCreator = factory.getMessageCreator();
		assertTrue(messageCreator instanceof MessageUnchangedCreator);
	}
	
	@Test
	void allResponsesStrategyMustBeSupportedTest() {
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
		builder.withResponseStrategy(responseStrategy);
		return builder.build();
	}
	
}