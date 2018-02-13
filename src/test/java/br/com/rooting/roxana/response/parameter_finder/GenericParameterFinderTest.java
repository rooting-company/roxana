package br.com.rooting.roxana.response.parameter_finder;

import static br.com.rooting.roxana.parameter.ParameterType.CURRENCY;
import static br.com.rooting.roxana.parameter.ParameterType.DATE;
import static br.com.rooting.roxana.parameter.ParameterType.STRING;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Locale.US;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.business.BusinessException;
import br.com.rooting.roxana.business.parameter.CurrencyParameter;
import br.com.rooting.roxana.business.parameter.DateParameter;
import br.com.rooting.roxana.business.parameter.DateStyle;
import br.com.rooting.roxana.business.parameter.Parameter;
import br.com.rooting.roxana.parameter.UnsupportedParameterConversionException;

public class GenericParameterFinderTest extends UnitTest<GenericParameterFinder> {
	
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
	public void objectCanNotBeNullTest() {
		new GenericParameterFinder(null);
	}
	
	@Test
	public void findBusinessExceptionParametersTest() {
		@BusinessException
		class MockedBusinessException extends Exception {
			
			private static final String STRING_PARAMETER_NAME = "CustomNameStringParameter";
			private static final String STRING_PARAMETER_VALUE = "String Parameter Value";
			
			private static final String CURRENCY_PARAMETER_NAME = "CustomNameCurrencyParameter";
			private static final String CURRENCY_PARAMETER_VALUE_FORMATED = "$10.00";
			
			private static final String DATE_PARAMETER_NAME = "CustomNameDateParameter";
			private static final String DATE_PARAMETER_VALUE_FORMATED = "12/11/92";

			private static final long serialVersionUID = 1L;

			@Parameter(STRING_PARAMETER_NAME)
			private final String stringParameter;
			
			@DateParameter(DATE_PARAMETER_NAME)
			private final LocalDate dateParameter;

			@CurrencyParameter(CURRENCY_PARAMETER_NAME)
			private final BigDecimal currencyParameter;

			public MockedBusinessException(final String stringParameter, 
										   final LocalDate dateParameter, 
										   final BigDecimal currencyParameter) {
				super();
				this.stringParameter = stringParameter;
				this.dateParameter = dateParameter;
				this.currencyParameter = currencyParameter;
			}
			
		}
		
		Exception businessException = new MockedBusinessException(MockedBusinessException.STRING_PARAMETER_VALUE, 
																  LocalDate.of(1992, 12, 11), 
																  BigDecimal.TEN);
		
		GenericParameterFinder finder = new GenericParameterFinder(businessException);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 3);
		
		br.com.rooting.roxana.parameter.Parameter stringParameter = parameters.get(0);
		assertEquals(MockedBusinessException.STRING_PARAMETER_NAME, stringParameter.getName());
		assertEquals(MockedBusinessException.STRING_PARAMETER_VALUE, stringParameter.getFormattedValue(US));
		assertEquals(STRING, stringParameter.getType());
		
		br.com.rooting.roxana.parameter.Parameter dataParameter = parameters.get(1);
		assertEquals(MockedBusinessException.DATE_PARAMETER_NAME, dataParameter.getName());
		assertEquals(MockedBusinessException.DATE_PARAMETER_VALUE_FORMATED, dataParameter.getFormattedValue(US));
		assertEquals(DATE, dataParameter.getType());
		
		br.com.rooting.roxana.parameter.Parameter currencyParameter = parameters.get(2);
		assertEquals(MockedBusinessException.CURRENCY_PARAMETER_NAME, currencyParameter.getName());
		assertEquals(MockedBusinessException.CURRENCY_PARAMETER_VALUE_FORMATED, currencyParameter.getFormattedValue(US));
		assertEquals(CURRENCY, currencyParameter.getType());
	}
	
	@Test
	public void findStringParameterTest() {
		class TestClass {
			public static final String STRING_PARAMETER_NAME = "CustomNameStringParameter";
			public static final String DEFAULT_STRING_PARAMETER_NAME = "defaultNameStringParameter";
			
			@Parameter(STRING_PARAMETER_NAME)
			private final Object stringParameter;
			
			@Parameter
			private final Object defaultNameStringParameter;
			
			public TestClass(final Object stringParameter, final Object defaultNameStringParameter) {
				this.stringParameter = stringParameter;
				this.defaultNameStringParameter = defaultNameStringParameter;
			}

			public Object getStringParameter() {
				return this.stringParameter;
			}
			
			public Object getDefaultNameStringParameter() {
				return this.defaultNameStringParameter;
			}
		}
		
		TestClass testClass = new TestClass(new Object(), new Object());
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter stringParameter = parameters.get(0);
		assertEquals(TestClass.STRING_PARAMETER_NAME, stringParameter.getName());
		assertEquals(testClass.getStringParameter().toString(), stringParameter.getFormattedValue(US));
		assertEquals(STRING, stringParameter.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultStringParameter = parameters.get(1);
		assertEquals(TestClass.DEFAULT_STRING_PARAMETER_NAME, defaultStringParameter.getName());
		assertEquals(testClass.getDefaultNameStringParameter().toString(), defaultStringParameter.getFormattedValue(US));
		assertEquals(STRING, defaultStringParameter.getType());
	}
	
	@Test
	public void findCurrencyAsDoubleParameterTest() {
		class TestClass {
			private static final String WRAPPER_PARAMETER_NAME = "CustomNameDoubleWrapper";
			private static final String WRAPPER_PARAMETER_VALUE = "$12.30";
			
			private static final String PRIMITIVE_PARAMETER_NAME = "CustomNameDoublePrimitive";
			private static final String PRIMITIVE_PARAMETER_VALUE = "$0.01";
			
			private static final String WRAPPER_PARAMETER_DEFAULT_NAME = "defaultNameDoubleWrapper";
			private static final String DEFAULT_NAME_WRAPPER_PARAMETER_VALUE = "$1.30";
			
			private static final String PRIMITIVE_PARAMETER_DEFAULT_NAME = "defaultNameDoublePrimitive";
			private static final String DEFAULT_NAME_PRIMITIVE_PARAMETER_VALUE = "$18.10";
			
			@CurrencyParameter(WRAPPER_PARAMETER_NAME)
			private final Double doubleWrapper;
			
			@CurrencyParameter(PRIMITIVE_PARAMETER_NAME)
			private final double doublePrimitive;
			
			@CurrencyParameter
			private final Double defaultNameDoubleWrapper;
			
			@CurrencyParameter
			private final double defaultNameDoublePrimitive;

			public TestClass(final Double doubleWrapper, 
							 final double doublePrimitive,
							 final Double defaultNameDoubleWrapper,
							 final double defaultNameDoublePrimitive) {
				
				this.doubleWrapper = doubleWrapper;
				this.doublePrimitive = doublePrimitive;
				this.defaultNameDoubleWrapper = defaultNameDoubleWrapper;
				this.defaultNameDoublePrimitive = defaultNameDoublePrimitive;
			}
		}
		
		TestClass testClass = new TestClass(new Double(12.3D), 0.01D, new Double(1.3D), 18.10D);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 4);
		
		br.com.rooting.roxana.parameter.Parameter doubleWrapper = parameters.get(0);
		assertEquals(TestClass.WRAPPER_PARAMETER_NAME, doubleWrapper.getName());
		assertEquals(TestClass.WRAPPER_PARAMETER_VALUE, doubleWrapper.getFormattedValue(US));
		assertEquals(CURRENCY, doubleWrapper.getType());
		
		br.com.rooting.roxana.parameter.Parameter doublePrimitive = parameters.get(1);
		assertEquals(TestClass.PRIMITIVE_PARAMETER_NAME, doublePrimitive.getName());
		assertEquals(TestClass.PRIMITIVE_PARAMETER_VALUE, doublePrimitive.getFormattedValue(US));
		assertEquals(CURRENCY, doublePrimitive.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultDoubleWrapper = parameters.get(2);
		assertEquals(TestClass.WRAPPER_PARAMETER_DEFAULT_NAME, defaultDoubleWrapper.getName());
		assertEquals(TestClass.DEFAULT_NAME_WRAPPER_PARAMETER_VALUE, defaultDoubleWrapper.getFormattedValue(US));
		assertEquals(CURRENCY, defaultDoubleWrapper.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultDoublePrimitive = parameters.get(3);
		assertEquals(TestClass.PRIMITIVE_PARAMETER_DEFAULT_NAME, defaultDoublePrimitive.getName());
		assertEquals(TestClass.DEFAULT_NAME_PRIMITIVE_PARAMETER_VALUE, defaultDoublePrimitive.getFormattedValue(US));
		assertEquals(CURRENCY, defaultDoublePrimitive.getType());
	}
	
	@Test
	public void findCurrencyParameterAsFloatTest() {
		class TestClass {
			private static final String WRAPPER_PARAMETER_NAME = "CustomNameFloatWrapper";
			private static final String WRAPPER_PARAMETER_VALUE = "$26.18";
			
			private static final String PRIMITIVE_PARAMETER_NAME = "CustomNameFloatPrimitive";
			private static final String PRIMITIVE_PARAMETER_VALUE = "$2.98";
			
			private static final String WRAPPER_PARAMETER_NAME_DEFAULT_NAME = "defaultNameFloatWrapper";
			private static final String DEFAULT_NAME_WRAPPER_PARAMETER_VALUE = "$542.43";
			
			private static final String PRIMITIVE_PARAMETER_DEFAULT_NAME = "defaultNameFloatPrimitive";
			private static final String DEFAULT_NAME_PRIMITIVE_PARAMETER_VALUE = "$15.42";
			
			@CurrencyParameter(WRAPPER_PARAMETER_NAME)
			private final Float floatWrapper;
			
			@CurrencyParameter(PRIMITIVE_PARAMETER_NAME)
			private final float floatPrimitive;
			
			@CurrencyParameter
			private final Float defaultNameFloatWrapper;
			
			@CurrencyParameter
			private final float defaultNameFloatPrimitive;

			public TestClass(final Float floatWrapper, 
							 final float floatPrimitive, 
							 final Float defaultNameFloatWrapper, 
							 final float defaultNameFloatPrimitive) {
				
				this.floatWrapper = floatWrapper;
				this.floatPrimitive = floatPrimitive;
				this.defaultNameFloatWrapper = defaultNameFloatWrapper;
				this.defaultNameFloatPrimitive = defaultNameFloatPrimitive;
			}
		}
		
		TestClass testClass = new TestClass(new Float(26.18F), 2.98F, new Float(542.43F), 15.42F);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 4);
		
		br.com.rooting.roxana.parameter.Parameter floatWrapper = parameters.get(0);
		assertEquals(TestClass.WRAPPER_PARAMETER_NAME, floatWrapper.getName());
		assertEquals(TestClass.WRAPPER_PARAMETER_VALUE, floatWrapper.getFormattedValue(US));
		assertEquals(CURRENCY, floatWrapper.getType());
		
		br.com.rooting.roxana.parameter.Parameter floatPrimitive = parameters.get(1);
		assertEquals(TestClass.PRIMITIVE_PARAMETER_NAME, floatPrimitive.getName());
		assertEquals(TestClass.PRIMITIVE_PARAMETER_VALUE, floatPrimitive.getFormattedValue(US));
		assertEquals(CURRENCY, floatPrimitive.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultFloatWrapper = parameters.get(2);
		assertEquals(TestClass.WRAPPER_PARAMETER_NAME_DEFAULT_NAME, defaultFloatWrapper.getName());
		assertEquals(TestClass.DEFAULT_NAME_WRAPPER_PARAMETER_VALUE, defaultFloatWrapper.getFormattedValue(US));
		assertEquals(CURRENCY, defaultFloatWrapper.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultFloatPrimitive = parameters.get(3);
		assertEquals(TestClass.PRIMITIVE_PARAMETER_DEFAULT_NAME, defaultFloatPrimitive.getName());
		assertEquals(TestClass.DEFAULT_NAME_PRIMITIVE_PARAMETER_VALUE, defaultFloatPrimitive.getFormattedValue(US));
		assertEquals(CURRENCY, defaultFloatPrimitive.getType());
	}
	
	@Test
	public void findCurrecyParameterAsBigDecimalTest() {
		class TestClass {
			private static final String WRAPPER_PARAMETER_NAME = "CustomNameBigDecimal";
			private static final String WRAPPER_PARAMETER_VALUE = "$1,789.76";
			
			private static final String WRAPPER_PARAMETER_DEFAULT_NAME = "defaultNameBigDecimal";
			private static final String DEFAULT_NAME_WRAPPER_PARAMETER_VALUE = "$3,653.76";
			
			@CurrencyParameter(WRAPPER_PARAMETER_NAME)
			private final BigDecimal bigDecimal;
			
			@CurrencyParameter
			private final BigDecimal defaultNameBigDecimal;

			public TestClass(final BigDecimal bigDecimal, final BigDecimal defaultNameBigDecimal) {
				this.bigDecimal = bigDecimal;
				this.defaultNameBigDecimal = defaultNameBigDecimal;
			}
		}
		
		TestClass testClass = new TestClass(new BigDecimal("1789.76"), new BigDecimal("3653.76"));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter bigDecimal = parameters.get(0);
		assertEquals(TestClass.WRAPPER_PARAMETER_NAME, bigDecimal.getName());
		assertEquals(TestClass.WRAPPER_PARAMETER_VALUE, bigDecimal.getFormattedValue(US));
		assertEquals(CURRENCY, bigDecimal.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultBigDecimal = parameters.get(1);
		assertEquals(TestClass.WRAPPER_PARAMETER_DEFAULT_NAME, defaultBigDecimal.getName());
		assertEquals(TestClass.DEFAULT_NAME_WRAPPER_PARAMETER_VALUE, defaultBigDecimal.getFormattedValue(US));
		assertEquals(CURRENCY, defaultBigDecimal.getType());
	}
	
	// TODO Testa mensagem correta.
	@Test(expected = UnsupportedParameterConversionException.class)
	public void unsupportedCurrencyParameterConversionTest() {
		class TestClass {
			@CurrencyParameter
			private final Object invalidCurrencyParameter;

			public TestClass(final Object invalidCurrencyParameter) {
				this.invalidCurrencyParameter = invalidCurrencyParameter;
			}
		}
		
		TestClass testClass = new TestClass(new Object());
		new GenericParameterFinder(testClass).findParameters();
	}
	
	@Test
	public void findDateParameterAsLocalDateTest() {
		class TestClass {
			public static final String DATE_PARAMETER_NAME = "CustomNameLocalDate";
			public static final String DATE_PARAMETER_VALUE_FORMATTED = "12/11/92";
			
			public static final String DATE_PARAMETER_DEFAULT_NAME = "defaultNameLocalDate";
			public static final String DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED = "Jun 29, 1954";
			
			@DateParameter(DATE_PARAMETER_NAME)
			private final LocalDate localDate;
			
			@DateParameter(style = DateStyle.MEDIUM)
			private final LocalDate defaultNameLocalDate;

			public TestClass(final LocalDate localDate, final LocalDate defaultNameLocalDate) {
				this.localDate = localDate;
				this.defaultNameLocalDate = defaultNameLocalDate;
			}
		}
		
		TestClass testClass = new TestClass(LocalDate.of(1992, 12, 11), LocalDate.of(1954, 06, 29));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PARAMETER_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PARAMETER_VALUE_FORMATTED, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PARAMETER_DEFAULT_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateWithPatternFormatTest() {
		class TestClass {
			public static final String DATE_PARAMETER_NAME = "CustomNameLocalDate";
			public static final String DATE_PARAMETER_PATTERN = "yyyy-MM-dd";
			public static final String DATE_PARAMETER_VALUE_FORMATTED = "1992-12-11";
			
			public static final String DATE_PARAMETER_DEFAULT_NAME = "defaultNameLocalDate";
			public static final String DATE_PARAMETER_DEFAULT_NAME_PATTERN = "dd/MM/yyyy";
			public static final String DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED = "29/06/1954";
			
			@DateParameter(value = DATE_PARAMETER_NAME, 
						   pattern = DATE_PARAMETER_PATTERN, 
						   considerTime = true)
			private final LocalDate localDate;
			
			@DateParameter(pattern = DATE_PARAMETER_DEFAULT_NAME_PATTERN)
			private final LocalDate defaultNameLocalDate;

			public TestClass(final LocalDate localDate, final LocalDate defaultNameLocalDate) {
				this.localDate = localDate;
				this.defaultNameLocalDate = defaultNameLocalDate;
			}
		}
		
		TestClass testClass = new TestClass(LocalDate.of(1992, 12, 11), LocalDate.of(1954, 06, 29));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PARAMETER_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PARAMETER_VALUE_FORMATTED, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PARAMETER_DEFAULT_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateTimeTest() {
		class TestClass {
			public static final String DATE_PARAMETER_NAME = "CustomNameLocalDateTime";
			public static final String DATE_PARAMETER_VALUE_FORMATTED = "12/11/92 1:00 PM";
			
			public static final String DATE_PARAMETER_DEFAULT_NAME = "defaultNameLocalDateTime";
			public static final String DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED = "Jun 29, 1954 10:10:00 PM";
			
			@DateParameter(value = DATE_PARAMETER_NAME, considerTime = true)
			private final LocalDateTime localDateTime;
			
			@DateParameter(style = DateStyle.MEDIUM, considerTime = true)
			private final LocalDateTime defaultNameLocalDateTime;

			public TestClass(final LocalDateTime localDateTime, final LocalDateTime defaultNameLocalDateTime) {
				this.localDateTime = localDateTime;
				this.defaultNameLocalDateTime = defaultNameLocalDateTime;
			}
		}
		
		TestClass testClass = new TestClass(LocalDateTime.of(1992, 12, 11, 13, 0), LocalDateTime.of(1954, 6, 29, 22, 10));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PARAMETER_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PARAMETER_VALUE_FORMATTED, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PARAMETER_DEFAULT_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateTimeWithPatternFormatTest() {
		class TestClass {
			public static final String DATE_PARAMETER_NAME = "CustomNameLocalDateTime";
			public static final String DATE_PARAMETER_PATTERN = "yyyy-MM-dd HH-mm";
			public static final String DATE_PARAMETER_VALUE_FORMATTED = "1992-12-11 13-00";
			
			public static final String DATE_PARAMETER_DEFAULT_NAME = "defaultNameLocalDateTime";
			public static final String DATE_PARAMETER_DEFAULT_NAME_PATTERN = "MM/dd/yyyy HH:mm";
			public static final String DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED = "06/29/1954 22:10";
			
			@DateParameter(value = DATE_PARAMETER_NAME, pattern = DATE_PARAMETER_PATTERN)
			private final LocalDateTime localDateTime;
			
			@DateParameter(pattern = DATE_PARAMETER_DEFAULT_NAME_PATTERN)
			private final LocalDateTime defaultNameLocalDateTime;

			public TestClass(final LocalDateTime localDateTime, final LocalDateTime defaultNameLocalDateTime) {
				this.localDateTime = localDateTime;
				this.defaultNameLocalDateTime = defaultNameLocalDateTime;
			}
		}
		
		TestClass testClass = new TestClass(LocalDateTime.of(1992, 12, 11, 13, 0), LocalDateTime.of(1954, 6, 29, 22, 10));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PARAMETER_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PARAMETER_VALUE_FORMATTED, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PARAMETER_DEFAULT_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsDateTest() throws ParseException {
		class TestClass {
			public static final String DATE_PARAMETER_NAME = "CustomNameDate";
			public static final String DATE_PARAMETER_VALUE_FORMATTED = "12/11/92";
			
			public static final String DATE_PARAMETER_DEFAULT_NAME = "defaultNameDate";
			public static final String DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED = "Jun 29, 1954 10:10:00 PM";
			
			@DateParameter(DATE_PARAMETER_NAME)
			private final Date date;
			
			@DateParameter(style = DateStyle.MEDIUM, considerTime = true)
			private final Date defaultNameDate;

			public TestClass(final Date date, final Date defaultNameDate) {
				this.date = date;
				this.defaultNameDate = defaultNameDate;
			}
		}
		
		Date parameter_01 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("11/12/1992 13:00");
		Date parameter_02 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("29/06/1954 22:10");
		
		TestClass testClass = new TestClass(parameter_01, parameter_02);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PARAMETER_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PARAMETER_VALUE_FORMATTED, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PARAMETER_DEFAULT_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsDateWithPatternFormatTest() throws ParseException {
		class TestClass {
			public static final String DATE_PARAMETER_NAME = "CustomNameDate";
			public static final String DATE_PARAMETER_PATTERN = "yyyy-MM-dd HH-mm";
			public static final String DATE_PARAMETER_VALUE_FORMATTED = "1992-12-11 13-00";
			
			public static final String DATE_PARAMETER_DEFAULT_NAME = "defaultNameDate";
			public static final String DATE_PARAMETER_DEFAULT_NAME_PATTERN = "MM/dd/yyyy HH:mm";
			public static final String DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED = "06/29/1954 22:10";
			
			@DateParameter(value = DATE_PARAMETER_NAME, pattern = DATE_PARAMETER_PATTERN)
			private final Date date;
			
			@DateParameter(pattern = DATE_PARAMETER_DEFAULT_NAME_PATTERN)
			private final Date defaultNameDate;

			public TestClass(final Date date, final Date defaultNameDate) {
				this.date = date;
				this.defaultNameDate = defaultNameDate;
			}
		}
		
		Date parameter_01 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("11/12/1992 13:00");
		Date parameter_02 = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("29/06/1954 22:10");
		
		TestClass testClass = new TestClass(parameter_01, parameter_02);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PARAMETER_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PARAMETER_VALUE_FORMATTED, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PARAMETER_DEFAULT_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsCalendarTest() {
		class TestClass {
			public static final String DATE_PARAMETER_NAME = "CustomNameCalendar";
			public static final String DATE_PARAMETER_VALUE_FORMATTED = "12/11/92";
			
			public static final String DATE_PARAMETER_DEFAULT_NAME = "defaultNameCalendar";
			public static final String DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED = "Jun 29, 1954 10:10:00 PM";
			
			@DateParameter(DATE_PARAMETER_NAME)
			private final Calendar calendar;
			
			@DateParameter(style = DateStyle.MEDIUM, considerTime = true)
			private final Calendar defaultNameCalendar;

			public TestClass(final Calendar calendar, final Calendar defaultNameCalendar) {
				this.calendar = calendar;
				this.defaultNameCalendar = defaultNameCalendar;
			}
		}
		
		Calendar parameter_01 = Calendar.getInstance();
		parameter_01.set(1992, 11, 11, 13, 00, 00);
		
		Calendar parameter_02 = Calendar.getInstance();
		parameter_02.set(1954, 5, 29, 22, 10, 00);
		
		TestClass testClass = new TestClass(parameter_01, parameter_02);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PARAMETER_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PARAMETER_VALUE_FORMATTED, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PARAMETER_DEFAULT_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsCalendarWithPatternFormatTest() {
		class TestClass {
			public static final String DATE_PARAMETER_NAME = "CustomNameCalendar";
			public static final String DATE_PARAMETER_PATTERN = "yyyy-MM-dd HH-mm";
			public static final String DATE_PARAMETER_VALUE_FORMATTED = "1992-12-11 13-00";
			
			public static final String DATE_PARAMETER_DEFAULT_NAME = "defaultNameCalendar";
			public static final String DATE_PARAMETER_DEFAULT_NAME_PATTERN = "MM/dd/yyyy HH:mm";
			public static final String DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED = "06/29/1954 22:10";
			
			@DateParameter(value = DATE_PARAMETER_NAME, pattern = DATE_PARAMETER_PATTERN)
			private final Calendar calendar;
			
			@DateParameter(pattern = DATE_PARAMETER_DEFAULT_NAME_PATTERN)
			private final Calendar defaultNameCalendar;

			public TestClass(final Calendar calendar, final Calendar defaultNameCalendar) {
				this.calendar = calendar;
				this.defaultNameCalendar = defaultNameCalendar;
			}
		}
		
		Calendar parameter_01 = Calendar.getInstance();
		parameter_01.set(1992, 11, 11, 13, 00, 00);
		
		Calendar parameter_02 = Calendar.getInstance();
		parameter_02.set(1954, 5, 29, 22, 10, 00);
		
		TestClass testClass = new TestClass(parameter_01, parameter_02);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PARAMETER_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PARAMETER_VALUE_FORMATTED, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PARAMETER_DEFAULT_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DEFAULT_NAME_DATE_PARAMETER_VALUE_FORMATTED, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test(expected = UnsupportedParameterConversionException.class)
	public void unsupportedDateParameterConversionTest() {
		class TestClass {
			@DateParameter
			private final Object invalidDateParameter;

			public TestClass(final Object invalidDateParameter) {
				this.invalidDateParameter = invalidDateParameter;
			}
		}
		
		TestClass testClass = new TestClass(new Object());
		new GenericParameterFinder(testClass).findParameters();
	}
	
}