package br.com.rooting.roxana.translator;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaPropertiesMockBuilder;
import br.com.rooting.roxana.parameter.Parameter;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static br.com.rooting.roxana.translator.LocaleTagEnum.PT_BR;
import static br.com.rooting.roxana.translator.TranslationEnum.TRANSLATION_REPEATED_PARAMS;
import static br.com.rooting.roxana.translator.TranslationEnum.TRANSLATION_STRING_DATE_CURRENCY_PARAMS;
import static br.com.rooting.roxana.translator.Translator.NOT_FOUND_DELIMITER;
import static br.com.rooting.roxana.translator.Translator.getInterpolatedKeyOf;
import static org.junit.jupiter.api.Assertions.*;

class RoxanaTranslatorTest extends UnitTest<RoxanaTranslator> {

    private static final String INVALID_LOCALE_TAG = "INVALID_LOCALE_TAG";
    private static final String INVALID_MESSAGE_BUNDLE_BASE_NAME = "INVALID_MESSAGE_BUNDLE_BASE_NAME";
    private static final String NOT_DEFINED_KEY = "NOT.DEFINED.KEY";
    private static final String NO_TRANSLATED_STRING = " ,this part is always fixed, ";

    @Test
    void testClassIsPublicTest() {
        assertTrue(Modifier.isPublic(this.getUnitTestClass().getModifiers()));
    }

    @Test
    void testClassImplementsTranslatorTest() {
        assertTrue(Translator.class.isAssignableFrom(this.getUnitTestClass()));
    }

    @Test
    void testClassIsASpringComponentTest() {
        assertTrue(this.getUnitTestClass().isAnnotationPresent(Component.class));
    }

    @Test
    void testClassWasOnlyOneProtectedConstructorTest() {
        Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
        assertEquals(1, constructors.length);
        assertTrue(Modifier.isProtected(constructors[0].getModifiers()));
    }

    @Test
    void translationTest() {
        Stream.of(LocaleTagEnum.values()).forEach(locale -> {
            RoxanaPropertiesMockBuilder propertiesBuilder = new RoxanaPropertiesMockBuilder();
            propertiesBuilder.withLocale(locale.getTag());

            RoxanaTranslator translator = new RoxanaTranslator(propertiesBuilder.build());

            Stream.of(TranslationEnum.values()).forEach(translation -> {
                String key = getInterpolatedKeyOf(translation.getKey());
                assertEquals(translation.getTranslation(locale), translator.translate(key, translation.getParameters()));
                assertEquals(translation.getTranslation(locale), translator.translate(key, translation.getParameters()));
            });
        });
    }

    @Test
    void compositeTranslationTest() {
        Stream.of(LocaleTagEnum.values()).forEach(locale -> {
            RoxanaPropertiesMockBuilder propertiesBuilder = new RoxanaPropertiesMockBuilder();
            propertiesBuilder.withLocale(locale.getTag());

            RoxanaTranslator translator = new RoxanaTranslator(propertiesBuilder.build());

            String key = getInterpolatedKeyOf(TRANSLATION_STRING_DATE_CURRENCY_PARAMS.getKey()) +
                    NO_TRANSLATED_STRING +
                    getInterpolatedKeyOf(TRANSLATION_REPEATED_PARAMS.getKey());

            List<Parameter> parameters = new ArrayList<>();
            parameters.addAll(TRANSLATION_STRING_DATE_CURRENCY_PARAMS.getParameters());
            parameters.addAll(TRANSLATION_REPEATED_PARAMS.getParameters());

            String translation = TRANSLATION_STRING_DATE_CURRENCY_PARAMS.getTranslation(locale) +
                    NO_TRANSLATED_STRING +
                    TRANSLATION_REPEATED_PARAMS.getTranslation(locale);

            assertEquals(translation, translator.translate(key, parameters));
            assertEquals(translation, translator.translate(key, parameters.toArray(new Parameter[0])));
        });
    }

    @Test
    void onlyParameterInterpolationTest() {
        RoxanaProperties roxanaProperties = new RoxanaPropertiesMockBuilder()
                .withLocale("pt-BR")
                .build();

        RoxanaTranslator translator = new RoxanaTranslator(roxanaProperties);

        String key = "These two parameters will be interpolated but de rest do not: [um], [dois]";
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(Parameter.create("um", "The First Parameter"));
        parameters.add(Parameter.create("dois", "The Second Parameter"));

        String translation = translator.translate(key, parameters);
        assertEquals("These two parameters will be interpolated but de rest do not: The First Parameter, The Second Parameter", translation);
    }

//	@Test
//	void scapeKeyCharactersTest() {
//		RoxanaProperties roxanaProperties = new RoxanaPropertiesMockBuilder()
//														.withLocale("pt-BR")
//														.build();
//		
//		RoxanaTranslator translator = new RoxanaTranslator(roxanaProperties);
//		
//		String key = "/{i.am.not.will.be.interpolated/}";
//		String translation = translator.translate(key);
//		assertEquals("{i.am.not.will.be.interpolated}", translation);
//	}
//	
//	@Test
//	void scapeParameterCharactersTest() {
//		
//	}

    // TODO Validate if the real error was logged correctly.
    @Test
    void suppressFailToTranslateTest() {
        RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
        roxPropMockBuilder.withLocale(PT_BR.getTag());
        roxPropMockBuilder.withSuppressFailsTranslations(true);

        RoxanaTranslator translator = new RoxanaTranslator(roxPropMockBuilder.build());
        String failTranslation = NOT_FOUND_DELIMITER + NOT_DEFINED_KEY + NOT_FOUND_DELIMITER;
        assertEquals(failTranslation, translator.translate(getInterpolatedKeyOf(NOT_DEFINED_KEY)));
    }

    @Test
    void throwFailToTranslateTest() {
        RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
        roxPropMockBuilder.withSuppressFailsTranslations(false);

        RoxanaTranslator translator = new RoxanaTranslator(roxPropMockBuilder.build());
        assertThrows(FailToTranslateException.class, () -> translator.translate(getInterpolatedKeyOf(NOT_DEFINED_KEY)));
    }

    @Test
    void throwMessageBundleBaseNameNotDefinedExceptionTest() {
        RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
        roxPropMockBuilder.withBaseName(null);
        assertThrows(MessageBundleBaseNameNotDefinedException.class, () -> new RoxanaTranslator(roxPropMockBuilder.build()));
    }

    @Test
    void throwFailToCreateRoxanaResourceBundleExceptionTest() {
        RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
        roxPropMockBuilder.withBaseName(INVALID_MESSAGE_BUNDLE_BASE_NAME);
        assertThrows(FailToCreateRoxanaResourceBundleException.class, () -> new RoxanaTranslator(roxPropMockBuilder.build()));
    }

    @Test
    void useDefaultLocaleTest() {
        RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
        roxPropMockBuilder.withLocale(null);

        RoxanaTranslator translator = new RoxanaTranslator(roxPropMockBuilder.build());
        assertEquals(Locale.getDefault(), translator.getLocale());
    }

    @Test
    void throwInvalidMessageBundleLocaleExceptionTest() {
        RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
        roxPropMockBuilder.withLocale(INVALID_LOCALE_TAG);
        assertThrows(InvalidMessageBundleLocaleException.class, () -> new RoxanaTranslator(roxPropMockBuilder.build()));
    }

}