package br.com.rooting.roxana.message;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.mapper.MessageMapper;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.message.mapper.MockedMessageMapper;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.finder.MessageMapperEnumParameterFinder;
import br.com.rooting.roxana.parameter.finder.ParameterFinderStrategy;
import br.com.rooting.roxana.parameter.mapper.Param;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static br.com.rooting.roxana.message.MessageSeverity.*;
import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class MessageUnchangedCreatorTest extends UnitTest<MessageUnchangedCreator> {

    private static final String STRING_PARAMETER_NAME_01 = "StringParameterName01";
    private static final String STRING_PARAMETER_NAME_02 = "StringParameterName02";

    private static final String RANDOM_STRING_PARAMETER_VALUE_01 = "parameter01";
    private static final String RANDOM_STRING_PARAMETER_VALUE_02 = "parameter02";

    private static final String KEY = "mocked.key";

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
    void createTest() {
        MessageUnchangedCreator creator = new MessageUnchangedCreator();
        MessageMapper mapper = new MockedMessageMapper(KEY, INFO);
        List<Parameter> parameters = new ArrayList<>();

        MessageUnchanged message = creator.create(mapper, parameters);
        assertEquals(mapper.getSeverity(), message.getSeverity());
        assertEquals(mapper.getKey(), message.getKey());
        assertEquals(message.getParameters().size(), parameters.size());
        assertTrue(message.getParameters().containsAll(parameters));
    }

    @Test
    void createArgsTest() {
        MessageUnchangedCreator creator = new MessageUnchangedCreator();
        MessageMapper mapper = new MockedMessageMapper(KEY, INFO);
        Parameter parameter_01 = mock(Parameter.class);
        Parameter parameter_02 = mock(Parameter.class);

        MessageUnchanged message = creator.create(mapper, parameter_01, parameter_02);
        assertEquals(mapper.getSeverity(), message.getSeverity());
        assertEquals(mapper.getKey(), message.getKey());
        assertEquals(2, message.getParameters().size());
        assertTrue(message.getParameters().contains(parameter_01));
        assertTrue(message.getParameters().contains(parameter_02));
    }

    @Test
    void createBasedOnEnumMapTest() {
        List<Object> parametersValues = new ArrayList<>();
        parametersValues.add(RANDOM_STRING_PARAMETER_VALUE_01);
        parametersValues.add(RANDOM_STRING_PARAMETER_VALUE_02);

        ParameterFinderStrategy parameterFinder = new MessageMapperEnumParameterFinder(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS, parametersValues);
        List<Parameter> parameters = parameterFinder.findParameters();

        MessageUnchangedCreator creator = new MessageUnchangedCreator();

        MessageUnchanged message = creator.create(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS, parametersValues);
        assertEquals(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS.getSeverity(), message.getSeverity());
        assertEquals(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS.getKey(), message.getKey());
        assertEquals(message.getParameters().size(), parameters.size());
        assertTrue(message.getParameters().containsAll(parameters));
    }

    @Test
    void createBasedOnEnumMapArgsTest() {
        List<Object> parametersValues = new ArrayList<>();
        parametersValues.add(RANDOM_STRING_PARAMETER_VALUE_01);
        parametersValues.add(RANDOM_STRING_PARAMETER_VALUE_02);
        ParameterFinderStrategy parameterFinder = new MessageMapperEnumParameterFinder(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS, parametersValues);
        List<Parameter> parameters = parameterFinder.findParameters();

        MessageUnchangedCreator creator = new MessageUnchangedCreator();
        MessageUnchanged message = creator.create(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS,
                RANDOM_STRING_PARAMETER_VALUE_01,
                RANDOM_STRING_PARAMETER_VALUE_02);

        assertEquals(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS.getSeverity(), message.getSeverity());
        assertEquals(MapperEnumTest.MAPPER_WITH_STRING_PARAMETERS.getKey(), message.getKey());
        assertEquals(message.getParameters().size(), parameters.size());
        assertTrue(message.getParameters().containsAll(parameters));
    }

    @Test
    void createBasedOnEnumMapWithNoParameterTest() {
        MessageUnchangedCreator creator = new MessageUnchangedCreator();
        MessageUnchanged message = creator.create(MapperEnumTest.MAPPER_WITH_NO_PARAMETERS);

        assertEquals(MapperEnumTest.MAPPER_WITH_NO_PARAMETERS.getSeverity(), message.getSeverity());
        assertEquals(MapperEnumTest.MAPPER_WITH_NO_PARAMETERS.getKey(), message.getKey());
        assertTrue(message.getParameters().isEmpty());
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