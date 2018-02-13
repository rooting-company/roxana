package br.com.rooting.roxana.response.parameter_finder;

import static br.com.rooting.roxana.message.MessageSeverity.SUCCESS;
import static br.com.rooting.roxana.parameter.ParameterType.CURRENCY;
import static br.com.rooting.roxana.parameter.ParameterType.DATE;
import static br.com.rooting.roxana.parameter.ParameterType.STRING;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Locale.US;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.message.MessageSeverity;
import br.com.rooting.roxana.message.mapper.MessageMapperEnum;
import br.com.rooting.roxana.message.mapper.parameter.CurrencyMessageParameter;
import br.com.rooting.roxana.message.mapper.parameter.CurrencyMessageParameters;
import br.com.rooting.roxana.message.mapper.parameter.DateMessageParameter;
import br.com.rooting.roxana.message.mapper.parameter.DateMessageParameters;
import br.com.rooting.roxana.message.mapper.parameter.MessageParameter;
import br.com.rooting.roxana.message.mapper.parameter.MessageParameters;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.UnsupportedParameterConversionException;

public class MessageMapperEnumParameterFinderTest extends UnitTest<MessageMapperEnumParameterFinder> {
	
	private static final String STRING_PARAMETER_NAME_01 = "StringParameterName01";
	private static final String STRING_PARAMETER_NAME_02 = "StringParameterName02";
	
	private static final String CURRENCY_PARAMETER_NAME_01 = "CurrencyParameterName01";
	private static final String CURRENCY_PARAMETER_NAME_02 = "CurrencyParameterName02";
	
	private static final String DATE_PARAMETER_NAME_01 = "DateParameterName01";
	private static final String DATE_PARAMETER_NAME_02 = "DateParameterName02";
	
	@Test
	public void testClassIsPublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	public void testClassExtendsMessageCreatorTest() {
		assertTrue(ParameterFinderStrategy.class.isAssignableFrom(this.getUnitTestClass()));
	}
	
	@Test
	public void testClassWasOnlyOnePackagePublicConstructorTest() {
		Constructor<?>[] constructors = this.getUnitTestClass().getDeclaredConstructors();
		assertTrue(constructors.length == 1);
		assertTrue(isPublic(constructors[0].getModifiers()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void enumMapperCanNotBeNull() {
		new MessageMapperEnumParameterFinder(null, new ArrayList<Object>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void valuesListCanNotBeNull() {
		new MessageMapperEnumParameterFinder(mock(MessageMapperEnum.class), null);
	}
	
	@Test
	public void findStringParamaterTest() {
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
	public void findMultiStringParameterTest() {
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
	public void findStringParametersTest() {
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
	public void findCurrencyParameterAsBigDecimalTest() {
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
	public void findMultiCurrencyParameterAsDoubleTest() {
		List<Object> parametersValues = new ArrayList<>();
		Double parameter_01 = new Double(0.32D);
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
	public void findCurrencyParametersAsFloatTest() {
		List<Object> parametersValues = new ArrayList<>();
		Float parameter_01 = new Float(-847.4F);
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
	
	@Test(expected = UnsupportedParameterConversionException.class)
	public void unsupportedCurrencyParameterConversionTest () {
		List<Object> parametersValues = new ArrayList<>();
		Object invalidCurrencyParameter = new Object();
		parametersValues.add(invalidCurrencyParameter);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.CURRENCY_PARAMETER, parametersValues);
		finder.findParameters();
	}
	
	@Test
	public void findDateParameterAsLocalDateTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDate parameter_01 = LocalDate.of(2018, 02, 12);
		parametersValues.add(parameter_01);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals("2/12/18", stringParameter_01.getFormattedValue(US));
		assertEquals(DATE, stringParameter_01.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateWithPatternTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDate parameter_01 = LocalDate.of(2003, 7, 30);
		LocalDate parameter_02 = LocalDate.of(1988, 10, 13);
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.MULTI_DATE_PARAMETER, parametersValues);
		List<Parameter> parameters = finder.findParameters();
		
		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals("30/07/2003", stringParameter_01.getFormattedValue(US));
		assertEquals(DATE, stringParameter_01.getType());
		
		Parameter stringParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, stringParameter_02.getName());
		assertEquals("1988-10-13", stringParameter_02.getFormattedValue(US));
		assertEquals(DATE, stringParameter_02.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateTimeTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDateTime parameter_01 = LocalDateTime.of(2000, 1, 1, 0, 0);
		parametersValues.add(parameter_01);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_PARAMETER, parametersValues);		
		List<Parameter> parameters = finder.findParameters();

		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals("1/1/00", stringParameter_01.getFormattedValue(US));
		assertEquals(DATE, stringParameter_01.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateTimeWithPatternFormatTest() {
		List<Object> parametersValues = new ArrayList<>();
		LocalDateTime parameter_01 = LocalDateTime.of(1995, 9, 14, 20, 15);
		LocalDateTime parameter_02 = LocalDateTime.of(2006, 5, 30, 1, 0);
		parametersValues.add(parameter_01);
		parametersValues.add(parameter_02);
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.DATE_PARAMETERS, parametersValues);		
		List<Parameter> parameters = finder.findParameters();
		
		Parameter stringParameter_01 = parameters.get(0);
		assertEquals(DATE_PARAMETER_NAME_01, stringParameter_01.getName());
		assertEquals("14/09/1995 20:15", stringParameter_01.getFormattedValue(US));
		assertEquals(DATE, stringParameter_01.getType());
		
		Parameter stringParameter_02 = parameters.get(1);
		assertEquals(DATE_PARAMETER_NAME_02, stringParameter_02.getName());
		assertEquals("2006-05-30 01-00", stringParameter_02.getFormattedValue(US));
		assertEquals(DATE, stringParameter_02.getType());
	}
	
//	@Test
//	public void findDateParameterAsDateTest() throws ParseException {
//		List<Object> parametersValues = new ArrayList<>();
//		Date parameter_01 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("11/12/1992 13:00");
//	}
	
	@Test(expected = MissingParametersValuesException.class)
	public void missingParametersValuesTest() {
		List<Object> parametersValues = new ArrayList<>();
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.STRING_PARAMETER, parametersValues);
		finder.findParameters();
	}
	
	@Test(expected = OverflowingParametersValuesException.class)
	public void overflowingParametersValuesTest() {
		List<Object> parametersValues = new ArrayList<>();
		parametersValues.add(new Object());
		parametersValues.add(new Object());
		
		MessageMapperEnumParameterFinder finder = new MessageMapperEnumParameterFinder(MapperEnumTest.STRING_PARAMETER, parametersValues);
		finder.findParameters();
	}
	
	private enum MapperEnumTest implements MessageMapperEnum {
		
		@MessageParameter(STRING_PARAMETER_NAME_01)
		STRING_PARAMETER(SUCCESS),
		
		@MessageParameter(STRING_PARAMETER_NAME_01)
		@MessageParameter(STRING_PARAMETER_NAME_02)
		MULTI_STRING_PARAMETER(SUCCESS),
		
		@MessageParameters(value = { @MessageParameter(STRING_PARAMETER_NAME_01),
									 @MessageParameter(STRING_PARAMETER_NAME_02) })
		STRING_PARAMETERS(SUCCESS),
		
		@CurrencyMessageParameter(CURRENCY_PARAMETER_NAME_01)
		CURRENCY_PARAMETER(SUCCESS),
		
		@CurrencyMessageParameter(CURRENCY_PARAMETER_NAME_01)
		@CurrencyMessageParameter(CURRENCY_PARAMETER_NAME_02)
		MULTI_CURRENCY_PARAMETER(SUCCESS),
		
		@CurrencyMessageParameters(value = { @CurrencyMessageParameter(CURRENCY_PARAMETER_NAME_01),
											 @CurrencyMessageParameter(CURRENCY_PARAMETER_NAME_02) })
		CURRENCY_PARAMETERS(SUCCESS),
		
		@DateMessageParameter(value = DATE_PARAMETER_NAME_01, considerTime = false)
		DATE_PARAMETER(SUCCESS),
		
		@DateMessageParameter(value = DATE_PARAMETER_NAME_01, pattern = "dd/MM/yyyy")
		@DateMessageParameter(value = DATE_PARAMETER_NAME_02, pattern = "yyyy-MM-dd")
		MULTI_DATE_PARAMETER(SUCCESS),
		
		@DateMessageParameters(value = { @DateMessageParameter(value = DATE_PARAMETER_NAME_01, pattern = "dd/MM/yyyy HH:mm"),
										 @DateMessageParameter(value = DATE_PARAMETER_NAME_02, pattern = "yyyy-MM-dd HH-mm") })
		DATE_PARAMETERS(SUCCESS);
		
		private final MessageSeverity severity;
		
		private MapperEnumTest(final MessageSeverity severity) {
			this.severity = severity;
		}

		@Override
		public MessageSeverity getSeverity() {
			return this.severity;
		}
	}

}