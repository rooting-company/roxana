package br.com.rooting.roxana.parameter.finder;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.MessageSeverity;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.UnsupportedParameterConversionException;
import br.com.rooting.roxana.parameter.mapper.CurrencyParam;
import br.com.rooting.roxana.parameter.mapper.CurrencyParam.CurrencyParams;
import br.com.rooting.roxana.parameter.mapper.DateParam;
import br.com.rooting.roxana.parameter.mapper.DateParam.DateParams;
import br.com.rooting.roxana.parameter.mapper.Param;
import br.com.rooting.roxana.parameter.mapper.Param.Params;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.com.rooting.roxana.message.MessageSeverity.SUCCESS;
import static br.com.rooting.roxana.parameter.ParameterType.*;
import static br.com.rooting.roxana.parameter.mapper.DateStyle.MEDIUM;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Locale.US;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MessageMapperEnumParameterFinderTest extends UnitTest<MessageMapperEnumParameterFinder> {
	
	private static final String STRING_PARAMETER_NAME_01 = "StringParameterName01";
	private static final String STRING_PARAMETER_NAME_02 = "StringParameterName02";
	
	private static final String CURRENCY_PARAMETER_NAME_01 = "CurrencyParameterName01";
	private static final String CURRENCY_PARAMETER_NAME_02 = "CurrencyParameterName02";
	
	private static final String DATE_PARAMETER_NAME_01 = "DateParameterName01";
	private static final String DATE_PARAMETER_NAME_02 = "DateParameterName02";
	
	@Test
	void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	void testClassExtendsMessageCreatorTest() {
		assertTrue(ParameterFinderStrategy.class.isAssignableFrom(this.getUnitTestClass()));
	}
	
	@Test
	void testClassWasOnlyOnePublicConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		assertEquals(1, constructors.length);
		assertTrue(isPublic(constructors[0].getModifiers()));
	}
	
	@Test
	void enumMapperCanNotBeNullTest() {
		Executable executable = () -> new MessageMapperEnumParameterFinder(null, new ArrayList<>());
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void valuesListCanNotBeNullTest() {
		Executable executable = () -> new MessageMapperEnumParameterFinder(mock(MessageMapperEnum.class), null);
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void enumMapperMustBeEnumTest() {
		Executable executable = () -> new MessageMapperEnumParameterFinder(mock(MessageMapperEnum.class), new ArrayList<>());
		assertThrows(IllegalArgumentException.class, executable);
	}
	
	@Test
	void findStringParamaterTest() {
		List<Object> parametersValues = new ArrayList<>();
		Object parameter_01 = new Object();
		parametersValues.add(parameter_01);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.STRING_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 1);
		
		Parameter stringParameter = parameters.get(0);
		assertEquals(STRING_PARAMETER_NAME_01, stringParameter.getName());
		assertEquals(parameter_01.toString(), stringParameter.getFormattedValue(US));
		assertEquals(STRING, stringParameter.getType());
	}
	
	@Test
	void findMultiStringParameterTest() {
		List<Object> parametersValues = new ArrayList<>();
		Object parameter_01 = new Object();
		Object parameter_02 = new Object();
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.MULTI_STRING_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(STRING_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals(parameter_01.toString(), stringParameter_01.getFormattedValue(US));
		assertEquals(STRING, stringParameter_01.getType());
		
		Parameter stringParameter_02 = parameters.get(1);
		assertEquals(STRING_PARAMETER_NAME_02, stringParameter_02.getName());
		assertEquals(parameter_02.toString(), stringParameter_02.getFormattedValue(US));
		assertEquals(STRING, stringParameter_02.getType());
	}
	
	@Test
	void findStringParametersTest() {
		List<Object> parametersValues = new ArrayList<>();
		Object parameter_01 = new Object();
		Object parameter_02 = new Object();
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.STRING_PARAMETERS, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(STRING_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals(parameter_01.toString(), stringParameter_01.getFormattedValue(US));
		assertEquals(STRING, stringParameter_01.getType());
		
		Parameter stringParameter_02 = parameters.get(1);
		assertEquals(STRING_PARAMETER_NAME_02, stringParameter_02.getName());
		assertEquals(parameter_02.toString(), stringParameter_02.getFormattedValue(US));
		assertEquals(STRING, stringParameter_02.getType());
	}
	
	@Test
	void findCurrencyParameterAsBigDecimalTest() {
		List<Object> parametersValues = new ArrayList<>();
		BigDecimal parameter_01 = new BigDecimal("32084.65");
		parametersValues.add(parameter_01);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.CURRENCY_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 1);
		
		Parameter stringParameter = parameters.get(0);
		assertEquals(CURRENCY_PARAMETER_NAME_01, stringParameter.getName());
		assertEquals("$32,084.65", stringParameter.getFormattedValue(US));
		assertEquals(CURRENCY, stringParameter.getType());
	}
	
	@Test
	void findMultiCurrencyParameterAsDoubleTest() {
		List<Object> parametersValues = new ArrayList<>();
		Double parameter_01 = 0.32D;
		double parameter_02 = 49834.98D;
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.MULTI_CURRENCY_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(CURRENCY_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals("$0.32", stringParameter_01.getFormattedValue(US));
		assertEquals(CURRENCY, stringParameter_01.getType());
		
		Parameter stringParameter_02 = parameters.get(1);
		assertEquals(CURRENCY_PARAMETER_NAME_02, stringParameter_02.getName());
		assertEquals("$49,834.98", stringParameter_02.getFormattedValue(US));
		assertEquals(CURRENCY, stringParameter_02.getType());
	}
	
	@Test
	void findCurrencyParametersAsFloatTest() {
		List<Object> parametersValues = new ArrayList<>();
		Float parameter_01 = -847.4F;
		float parameter_02 = 0.842F;
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.CURRENCY_PARAMETERS, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(CURRENCY_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals("($847.40)", stringParameter_01.getFormattedValue(US));
		assertEquals(CURRENCY, stringParameter_01.getType());
		
		Parameter stringParameter_02 = parameters.get(1);
		assertEquals(CURRENCY_PARAMETER_NAME_02, stringParameter_02.getName());
		assertEquals("$0.84", stringParameter_02.getFormattedValue(US));
		assertEquals(CURRENCY, stringParameter_02.getType());
	}
	
	@Test
	void unsupportedCurrencyParameterConversionTest () {
		List<Object> parametersValues = new ArrayList<>();
		Object invalidCurrencyParameter = new Object();
		parametersValues.add(invalidCurrencyParameter);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.CURRENCY_PARAMETER, parametersValues);
		assertThrows(UnsupportedParameterConversionException.class, () -> finder.findParameters());
	}
	
	@Test
	void findDateParameterAsLocalDateWithShortStyleTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDate parameter_01 = LocalDate.of(2018, Month.FEBRUARY, 12);
		LocalDate parameter_02 = LocalDate.of(2009, Month.MAY, 1);
		
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.MULTI_DATE_SHORT_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("2/12/18", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
		
		Parameter dateParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, dateParameter_02.getName());
		assertEquals("5/1/09 12:00 AM", dateParameter_02.getFormattedValue(US));
		assertEquals(DATE, dateParameter_02.getType());
	}
	
	@Test
	void findDateParameterAsLocalDateWithMediumStyleTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDate parameter_01 = LocalDate.of(1967, Month.DECEMBER, 3);
		LocalDate parameter_02 = LocalDate.of(2016, Month.AUGUST, 7);
		
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_MEDIUM_PARAMETERS, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("Dec 3, 1967", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
		
		Parameter dateParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, dateParameter_02.getName());
		assertEquals("Aug 7, 2016 12:00:00 AM", dateParameter_02.getFormattedValue(US));
		assertEquals(DATE, dateParameter_02.getType());
	}
	
	@Test
	void findDateParameterAsLocalDateWithPatternTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDate parameter_01 = LocalDate.of(1945, Month.JANUARY, 1);
		parametersValues.add(parameter_01);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_PATTERN_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("1945-01-01", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
	}
	
	@Test
	void findDateParameterAsLocalDateTimeWithShortStyleTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDateTime parameter_01 = LocalDateTime.of(2015, Month.OCTOBER, 28, 10, 30);
		LocalDateTime parameter_02 = LocalDateTime.of(2009, Month.NOVEMBER, 11, 16, 30);
		
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.MULTI_DATE_SHORT_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("10/28/15", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
		
		Parameter dateParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, dateParameter_02.getName());
		assertEquals("11/11/09 4:30 PM", dateParameter_02.getFormattedValue(US));
		assertEquals(DATE, dateParameter_02.getType());
	}
	
	@Test
	void findDateParameterAsLocalDateTimeWithMediumStyleTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDateTime parameter_01 = LocalDateTime.of(2007, Month.MAY, 18, 18, 0);
		LocalDateTime parameter_02 = LocalDateTime.of(1997, Month.JANUARY, 23, 14, 59);
		
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_MEDIUM_PARAMETERS, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("May 18, 2007", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
		
		Parameter dateParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, dateParameter_02.getName());
		assertEquals("Jan 23, 1997 2:59:00 PM", dateParameter_02.getFormattedValue(US));
		assertEquals(DATE, dateParameter_02.getType());
	}
	
	@Test
	void findDateParameterAsLocalDateTimeWithPatternTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDateTime parameter_01 = LocalDateTime.of(1937, Month.MARCH, 19, 10, 8);
		parametersValues.add(parameter_01);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_PATTERN_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("1937-03-19", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
	}
	
	@Test
	void findDateParameterAsDateWithShortStyleTest() throws ParseException {
		List<Object> parametersValues = new ArrayList<>();
		Date parameter_01 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("25/07/1978 04:20");
		Date parameter_02 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("09/01/2010 23:01");
		
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.MULTI_DATE_SHORT_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("7/25/78", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
		
		Parameter dateParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, dateParameter_02.getName());
		assertEquals("1/9/10 11:01 PM", dateParameter_02.getFormattedValue(US));
		assertEquals(DATE, dateParameter_02.getType());
	}
	
	@Test
	void findDateParameterAsDateWithMediumStyleTest() throws ParseException {
		List<Object> parametersValues = new ArrayList<>();
		Date parameter_01 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("16/05/2006 08:29");
		Date parameter_02 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("04/06/1999 10:12");
		
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_MEDIUM_PARAMETERS, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("May 16, 2006", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
		
		Parameter dateParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, dateParameter_02.getName());
		assertEquals("Jun 4, 1999 10:12:00 AM", dateParameter_02.getFormattedValue(US));
		assertEquals(DATE, dateParameter_02.getType());
	}
	
	@Test
	void findDateParameterAsDateWithPatternTest() throws ParseException {
		List<Object> parametersValues = new ArrayList<>();
		Date parameter_01 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("31/10/2017 18:30");
		parametersValues.add(parameter_01);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_PATTERN_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("2017-10-31", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
	}
	
	@Test
	void findDateParameterAsCalendarWithShortStyleTest() {
		List<Object> parametersValues = new ArrayList<>();
		Calendar parameter_01 = Calendar.getInstance();
		parameter_01.set(2003, Calendar.APRIL, 13, 23, 20, 0);
		
		Calendar parameter_02 = Calendar.getInstance();
		parameter_02.set(2011, Calendar.MARCH, 26, 1, 9, 0);
		
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.MULTI_DATE_SHORT_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("4/13/03", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
		
		Parameter dateParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, dateParameter_02.getName());
		assertEquals("3/26/11 1:09 AM", dateParameter_02.getFormattedValue(US));
		assertEquals(DATE, dateParameter_02.getType());
	}
	
	@Test
	void findDateParameterAsCalendarWithMediumStyleTest() {
		List<Object> parametersValues = new ArrayList<>();
		Calendar parameter_01 = Calendar.getInstance();
		parameter_01.set(1893, Calendar.JANUARY, 22, 20, 20, 0);
		
		Calendar parameter_02 = Calendar.getInstance();
		parameter_02.set(2008, Calendar.DECEMBER, 11, 0, 0, 31);
		
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_MEDIUM_PARAMETERS, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("Jan 22, 1893", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
		
		Parameter dateParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, dateParameter_02.getName());
		assertEquals("Dec 11, 2008 12:00:31 AM", dateParameter_02.getFormattedValue(US));
		assertEquals(DATE, dateParameter_02.getType());
	}
	
	@Test
	void findDateParameterAsCalendarWithPatternTest() {
		List<Object> parametersValues = new ArrayList<>();
		Calendar parameter_01 = Calendar.getInstance();
		parameter_01.set(2017, Calendar.NOVEMBER, 30, 19, 48, 0);
		parametersValues.add(parameter_01);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_PATTERN_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter dateParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, dateParameter_01.getName());
		assertEquals("2017-11-30", dateParameter_01.getFormattedValue(US));
		assertEquals(DATE, dateParameter_01.getType());
	}
	
	@Test
	void findParametersWithDefaultKeysTest() {
		List<Object> parametersValues = new ArrayList<>();
		String param = "test";
		LocalDateTime date_param = LocalDateTime.of(1992, 12, 11, 0, 0);
		BigDecimal currency_param = new BigDecimal("32084.65");
		
		parametersValues.add(param);
		parametersValues.add(date_param);
		parametersValues.add(currency_param);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DEFAULT_KEY_PARAMETERS, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter parameter = parameters.get(0);
		assertEquals("0", parameter.getName());
		assertEquals(param, parameter.getFormattedValue(US));
		assertEquals(STRING, parameter.getType());
		
		Parameter date_parameter = parameters.get(1);
		assertEquals("1", date_parameter.getName());
		assertEquals("12/11/92", date_parameter.getFormattedValue(US));
		assertEquals(DATE, date_parameter.getType());
		
		Parameter currency_parameter = parameters.get(2);
		assertEquals("2", currency_parameter.getName());
		assertEquals("$32,084.65", currency_parameter.getFormattedValue(US));
		assertEquals(CURRENCY, currency_parameter.getType());
	}
	
	@Test
	void missingParametersValuesTest() {
		List<Object> parametersValues = new ArrayList<>();
		Executable executable = () -> new MessageMapperEnumParameterFinder(MapperEnumTest.STRING_PARAMETER, parametersValues);
		assertThrows(MissingParametersValuesException.class, executable);
	}
	
	@Test
	void overflowingParametersValuesTest() {
		List<Object> parametersValues = new ArrayList<>();
		parametersValues.add(new Object());
		parametersValues.add(new Object());
		
		Executable executable = () -> new MessageMapperEnumParameterFinder(MapperEnumTest.STRING_PARAMETER, parametersValues);
		assertThrows(OverflowingParametersValuesException.class, executable);
	}
	
	@Test
	void ignoresIfMessageMapperHasOtherAnnotation() {
		List<Object> parametersValues = new ArrayList<>();
		Object parameter_01 = new Object();
		Object parameter_02 = new Object();
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(OthersAnnotationsMassageMapperEnum.OTHER_ANNOTATION_MESSAGE_MAPPER_ENUM, 
																					   parametersValues);
		List<Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(STRING_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals(parameter_01.toString(), stringParameter_01.getFormattedValue(US));
		assertEquals(STRING, stringParameter_01.getType());
			
		Parameter stringParameter_02 = parameters.get(1);
		assertEquals(STRING_PARAMETER_NAME_02, stringParameter_02.getName());
		assertEquals(parameter_02.toString(), stringParameter_02.getFormattedValue(US));
		assertEquals(STRING, stringParameter_02.getType());
	}
	
	private enum MapperEnumTest implements MessageMapperEnum {
		
		@Param(STRING_PARAMETER_NAME_01)
		STRING_PARAMETER(SUCCESS),
		
		@Param(STRING_PARAMETER_NAME_01)
		@Param(STRING_PARAMETER_NAME_02)
		MULTI_STRING_PARAMETER(SUCCESS),
		
		@Params(value = { @Param(STRING_PARAMETER_NAME_01),
						  @Param(STRING_PARAMETER_NAME_02) })
		STRING_PARAMETERS(SUCCESS),
		
		@CurrencyParam(CURRENCY_PARAMETER_NAME_01)
		CURRENCY_PARAMETER(SUCCESS),
		
		@CurrencyParam(CURRENCY_PARAMETER_NAME_01)
		@CurrencyParam(CURRENCY_PARAMETER_NAME_02)
		MULTI_CURRENCY_PARAMETER(SUCCESS),
		
		@CurrencyParams(value = { @CurrencyParam(CURRENCY_PARAMETER_NAME_01),
								  @CurrencyParam(CURRENCY_PARAMETER_NAME_02) })
		CURRENCY_PARAMETERS(SUCCESS),
		
		@DateParam(value = DATE_PARAMETER_NAME_01)
		@DateParam(value = DATE_PARAMETER_NAME_02, considerTime = true)
		MULTI_DATE_SHORT_PARAMETER(SUCCESS),
		
		@DateParams( value = { @DateParam(value = DATE_PARAMETER_NAME_01, style = MEDIUM),
							   @DateParam(value = DATE_PARAMETER_NAME_02, style = MEDIUM, considerTime = true)})
		DATE_MEDIUM_PARAMETERS(SUCCESS),
		
		@DateParam(value = DATE_PARAMETER_NAME_01, pattern = "yyyy-MM-dd")
		DATE_PATTERN_PARAMETER(SUCCESS),
		
		@Param
		@DateParam
		@CurrencyParam
		DEFAULT_KEY_PARAMETERS(SUCCESS);
		
		private final MessageSeverity severity;
		
		MapperEnumTest(final MessageSeverity severity) {
			this.severity = severity;
		}

		@Override
		public MessageSeverity getSeverity() {
			return this.severity;
		}
	}

	private enum OthersAnnotationsMassageMapperEnum implements MessageMapperEnum {
		
		@NotNull
		@Param(STRING_PARAMETER_NAME_01)
		@Param(STRING_PARAMETER_NAME_02)
		OTHER_ANNOTATION_MESSAGE_MAPPER_ENUM(SUCCESS);
		
		
		private final MessageSeverity severity;
		
		OthersAnnotationsMassageMapperEnum(final MessageSeverity severity) {
			this.severity = severity;
		}

		@Override
		public MessageSeverity getSeverity() {
			return this.severity;
		}

	}
}