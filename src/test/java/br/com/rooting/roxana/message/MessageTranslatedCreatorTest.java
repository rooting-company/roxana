package br.com.rooting.roxana.message;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.message.mapper.MockedMessageMapper;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.finder.MessageMapperEnumParameterFinder;
import br.com.rooting.roxana.parameter.finder.ParameterFinderStrategy;
import br.com.rooting.roxana.parameter.mapper.Param;
import br.com.rooting.roxana.translator.MockedTranslator;
import br.com.rooting.roxana.translator.Translator;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static br.com.rooting.roxana.message.MessageSeverity.*;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MessageTranslatedCreatorTest extends UnitTest<MessageTranslatedCreator> {

    private static final String STRING_PARAMETER_NAME_01 = "StringParameterName01";
    private static final String STRING_PARAMETER_NAME_02 = "StringParameterName02";

    private static final String RANDOM_STRING_PARAMETER_VALUE_01 = "parameter01";
    private static final String RANDOM_STRING_PARAMETER_VALUE_02 = "parameter02";

    private static final String KEY = "key";

    @Test
    void testClassIsPackagePrivateTest() {
        assertTrue(isPackagePrivate(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassExtendsMessageCreatorTest() {
        assertTrue(MessageCreator.class.isAssignableFrom(this.getUnitTestClass()));
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
    void translatorCanNotBeNullTest() {
        assertThrows(IllegalArgumentException.class, () -> new MessageTranslatedCreator(null));
    }

    @Test
    void createTest() {
        Translator translator = new MockedTranslator();
        MessageTranslatedCreator creator = new MessageTranslatedCreator(translator);

        MessageMapper mapper = new MockedMessageMapper(KEY, INFO);
        List<Parameter> parameters = new ArrayList<>();

        MessageTranslated message = creator.create(mapper, parameters);
        assertEquals(mapper.getSeverity(), message.getSeverity());
        assertEquals(translator.translate(mapper.getKey(), parameters), message.getTranslation());
    }

    @Test
    void createArgsTest() {
        Translator translator = new MockedTranslator();

        MessageTranslatedCreator creator = new MessageTranslatedCreator(translator);
        MessageMapper mapper = new MockedMessageMapper(KEY, INFO);
        Parameter parameter_01 = mock(Parameter.class);
        Parameter parameter_02 = mock(Parameter.class);

        MessageTranslated message = creator.create(mapper, parameter_01, parameter_02);
        assertEquals(mapper.getSeverity(), message.getSeverity());
        assertEquals(translator.translate(mapper.getKey(), parameter_01, parameter_02), message.getTranslation());
    }

    @Test
    void createBasedOnEnumMapTest() {
        List<Object> parametersValues = new ArrayList<>();
        parametersValues.add(RANDOM_STRING_PARAMETER_VALUE_01);
        parametersValues.add(RANDOM_STRING_PARAMETER_VALUE_02);
        ParameterFinderStrategy parameterFinder = new MessageMapperEnumParameterFinder(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS, parametersValues);
        List<Parameter> parameters = parameterFinder.findParameters();

        Translator translator = new MockedTranslator();
        MessageTranslatedCreator creator = new MessageTranslatedCreator(translator);

        MessageTranslated message = creator.create(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS, parametersValues);
        assertEquals(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS.getSeverity(), message.getSeverity());
        assertEquals(translator.translate(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS.getKey(), parameters), message.getTranslation());
    }

    @Test
    void createBasedOnEnumMapArgsTest() {
        List<Object> parametersValues = new ArrayList<>();
        parametersValues.add(RANDOM_STRING_PARAMETER_VALUE_01);
        parametersValues.add(RANDOM_STRING_PARAMETER_VALUE_02);
        ParameterFinderStrategy parameterFinder = new MessageMapperEnumParameterFinder(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS, parametersValues);
        List<Parameter> parameters = parameterFinder.findParameters();

        Translator translator = new MockedTranslator();
        MessageTranslatedCreator creator = new MessageTranslatedCreator(translator);

        MessageTranslated message = creator.create(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS,
                RANDOM_STRING_PARAMETER_VALUE_01,
                RANDOM_STRING_PARAMETER_VALUE_02);

        assertEquals(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS.getSeverity(), message.getSeverity());
        assertEquals(translator.translate(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS.getKey(), parameters), message.getTranslation());
    }

    @Test
    void createBasedOnEnumMapWithNoParameterTest() {
        Translator translator = new MockedTranslator();
        MessageTranslatedCreator creator = new MessageTranslatedCreator(translator);
        MessageTranslated message = creator.create(MapperEnumTest.MAPPER_WITH_NO_PARAMETERS);

        assertEquals(MapperEnumTest.MAPPER_WITH_NO_PARAMETERS.getSeverity(), message.getSeverity());
        assertEquals(translator.translate(MapperEnumTest.MAPPER_WITH_NO_PARAMETERS.getKey(), new ArrayList<>()), message.getTranslation());
    }

    private enum MapperEnumTest implements MessageMapperEnum {
        MAPPER_WITH_NO_PARAMETERS(ERROR),

        @Param(STRING_PARAMETER_NAME_01)
        @Param(STRING_PARAMETER_NAME_02)
        MAPPER_WITH_STRING_PARAMETERS(SUCCESS);

        private final MessageSeverity severity;

        MapperEnumTest(final MessageSeverity severity) {
            this.severity = severity;
        }

        @Override
        public MessageSeverity getSeverity() {
            return this.severity;
        }
    }

}