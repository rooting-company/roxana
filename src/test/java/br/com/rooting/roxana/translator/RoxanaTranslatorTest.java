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
import static br.com.rooting.roxana.translator.Translator.getInterpoledKeyOf;
import static org.junit.jupiter.api.Assertions.*;

class RoxanaTranslatorTest extends UnitTest<RoxanaTranslator> {

	private static final String INVALID_LOCALE_TAG 							= "INVALID_LOCALE_TAG";
	private static final String INVALID_MESSAGE_BUNDLE_BASE_NAME 			= "INVALID_MESSAGE_BUNDLE_BASE_NAME";
	private static final String NOT_DEFINED_KEY 							= "NOT.DEFINED.KEY";
	private static final String NO_TRANSLATED_STRING 						= " ,esta parte é sempre fixa, ";
	
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
		// Não pode ser public, pois deve-se usar o @Autowired para criar.
		// Precisa ser protected, por que usuários do framework podem extender
		assertTrue(Modifier.isProtected(constructors[0].getModifiers()));
	}
	
	// Testa a traduçao em diversas mensagens em diversas linguagens.
	@Test
	void translationTest() {
		Stream.of(LocaleTagEnum.values()).forEach(locale -> {
			RoxanaPropertiesMockBuilder propertiesBuilder = new RoxanaPropertiesMockBuilder();
			propertiesBuilder.withLocale(locale.getTag());
			
			RoxanaTranslator translator = new RoxanaTranslator(propertiesBuilder.build());
			
			Stream.of(TranslationEnum.values()).forEach(translation -> {
				String key = getInterpoledKeyOf(translation.getKey());
				assertEquals(translation.getTranslation(locale), translator.translate(key, translation.getParameters()));
				assertEquals(translation.getTranslation(locale), translator.translate(key, translation.getParameters()));
			});
		});
	}
	
	// Teste a tradução de mensagens compostas por outras.
	@Test
	void compositeTranslationTest() {
		Stream.of(LocaleTagEnum.values()).forEach(locale -> {
			RoxanaPropertiesMockBuilder propertiesBuilder = new RoxanaPropertiesMockBuilder();
			propertiesBuilder.withLocale(locale.getTag());
			
			RoxanaTranslator translator = new RoxanaTranslator(propertiesBuilder.build());
			
			StringBuilder keyBuilder = new StringBuilder();
			keyBuilder.append(getInterpoledKeyOf(TRANSLATION_STRING_DATE_CURRENCY_PARAMS.getKey()));
			keyBuilder.append(NO_TRANSLATED_STRING);
			keyBuilder.append(getInterpoledKeyOf(TRANSLATION_REPEATED_PARAMS.getKey()));
			String key = keyBuilder.toString();
			
			List<Parameter> parameters = new ArrayList<>();
			parameters.addAll(TRANSLATION_STRING_DATE_CURRENCY_PARAMS.getParameters());
			parameters.addAll(TRANSLATION_REPEATED_PARAMS.getParameters());
			
			StringBuilder translationBuilder = new StringBuilder();
			translationBuilder.append(TRANSLATION_STRING_DATE_CURRENCY_PARAMS.getTranslation(locale));
			translationBuilder.append(NO_TRANSLATED_STRING);
			translationBuilder.append(TRANSLATION_REPEATED_PARAMS.getTranslation(locale));
			String translation = translationBuilder.toString();
			
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
		
		String key = "These two parameters will be interpoled but de rest do not: [um], [dois]";
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(Parameter.create("um", "The First Parameter"));
		parameters.add(Parameter.create("dois", "The Second Parameter"));
		
		String translation = translator.translate(key, parameters);
		assertEquals("These two parameters will be interpoled but de rest do not: The First Parameter, The Second Parameter", translation);
	}
	
//	@Test
//	void scapeKeyCharactersTest() {
//		RoxanaProperties roxanaProperties = new RoxanaPropertiesMockBuilder()
//														.withLocale("pt-BR")
//														.build();
//		
//		RoxanaTranslator translator = new RoxanaTranslator(roxanaProperties);
//		
//		String key = "/{i.am.not.will.be.interpoled/}";
//		String translation = translator.translate(key);
//		assertEquals("{i.am.not.will.be.interpoled}", translation);
//	}
//	
//	@Test
//	void scapeParameterCharactersTest() {
//		
//	}
	
	// Testa se a falha ao traduzir é escondida.
	// TODO Validar se o verdadeiro erro foi logado.
	@Test
	void supressFailToTranslateTest() {
		RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
		roxPropMockBuilder.withLocale(PT_BR.getTag());
		roxPropMockBuilder.withSupressFailsTranslations(true);
		
		RoxanaTranslator translator = new RoxanaTranslator(roxPropMockBuilder.build());
		String failTranslation = NOT_FOUND_DELIMITER + NOT_DEFINED_KEY + NOT_FOUND_DELIMITER;
		assertEquals(failTranslation, translator.translate(getInterpoledKeyOf(NOT_DEFINED_KEY)));
	}
	
	// Testa se a exception é lançada ao falha na tradução
	@Test
	void throwFailToTranslateTest() {
		RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
		roxPropMockBuilder.withSupressFailsTranslations(false);
		
		RoxanaTranslator translator = new RoxanaTranslator(roxPropMockBuilder.build());
		assertThrows(FailToTranslateException.class, () -> translator.translate(getInterpoledKeyOf(NOT_DEFINED_KEY)));
	}
	
	// Testa se a exception é lançada quando o base name do message bundle não foi definido.
	@Test
	void throwMessageBundleBaseNameNotDefinedExceptionTest() {
		RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
		roxPropMockBuilder.withBaseName(null);
		assertThrows(MessageBundleBaseNameNotDefinedException.class, () -> new RoxanaTranslator(roxPropMockBuilder.build()));
	}
	
	// Testa se exception é lançada quando não foi possivel entrar o bundle corretamente.
	@Test
	void throwFailToCreateRoxanaResourceBundleExceptionTest() {
		RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
		roxPropMockBuilder.withBaseName(INVALID_MESSAGE_BUNDLE_BASE_NAME);
		assertThrows(FailToCreateRoxanaResourceBundleException.class, () -> new RoxanaTranslator(roxPropMockBuilder.build()));
	}
	
	// Testa se o tradutor irá usar o locale default quando não for informado outro.
	@Test
	void useDefaultLocaleTest() {
		RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
		roxPropMockBuilder.withLocale(null);
		
		RoxanaTranslator translator = new RoxanaTranslator(roxPropMockBuilder.build());
		assertEquals(Locale.getDefault(), translator.getLocale());
	}
	
	// Testa se exception é lançada quando a tag informada como locale é invalida.
	@Test
	void throwInvalidMessageBundleLocaleExceptionTest() {
		RoxanaPropertiesMockBuilder roxPropMockBuilder = new RoxanaPropertiesMockBuilder();
		roxPropMockBuilder.withLocale(INVALID_LOCALE_TAG);
		assertThrows(InvalidMessageBundleLocaleException.class, () -> new RoxanaTranslator(roxPropMockBuilder.build()));
	}

}