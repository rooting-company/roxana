package br.com.rooting.roxana.response.parameter_finder;

import static br.com.rooting.roxana.business.parameter.DateStyle.MEDIUM;
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
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.business.BusinessException;
import br.com.rooting.roxana.business.parameter.CurrencyParameter;
import br.com.rooting.roxana.business.parameter.DateParameter;
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
			public static final String STRING_PARAMETER_DEFAULT_NAME = "defaultNameStringParameter";
			
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
		assertEquals(TestClass.STRING_PARAMETER_DEFAULT_NAME, defaultStringParameter.getName());
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
	public void findDateParameterAsLocalDateWithShortStyleTest() {
		class TestClass {
			public static final String LOCALDATE_SHORT_NAME = "localDateShort";
			public static final String LOCALDATE_SHORT_VALUE = "8/22/96";
			
			public static final String LOCALDATE_SHORT_CONSIDERING_TIME_NAME = "CustomNameLocalDateShortConsideringTime";
			public static final String LOCALDATE_SHORT_CONSIDERING_TIME_VALUE = "2/12/51 12:00 AM";
			
			@DateParameter
			private final LocalDate localDateShort;
			
			@DateParameter(value = LOCALDATE_SHORT_CONSIDERING_TIME_NAME, considerTime = true)
			private final LocalDate localDateShortConsideringTime;
			
			public TestClass(final LocalDate localDateShort, final LocalDate localDateShortConsideringTime) {
				this.localDateShort = localDateShort;
				this.localDateShortConsideringTime = localDateShortConsideringTime;
			}
		}
		
		TestClass testClass = new TestClass(LocalDate.of(1996, Month.AUGUST, 22), LocalDate.of(1951, Month.FEBRUARY, 12));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.LOCALDATE_SHORT_NAME, localDate.getName());
		assertEquals(TestClass.LOCALDATE_SHORT_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.LOCALDATE_SHORT_CONSIDERING_TIME_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.LOCALDATE_SHORT_CONSIDERING_TIME_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateWithMediumStyleTest() {
		class TestClass {
			public static final String LOCALDATE_MEDIUM_CONSIDERING_TIME_NAME = "CustomNameLocalDateMediumConsideringTime";
			public static final String LOCALDATE_MEDIUM_CONSIDERING_TIME_NAME_VALUE = "Sep 22, 1996 12:00:00 AM";
			
			public static final String LOCALDATE_MEDIUM_NAME = "localDateMedium";
			public static final String LOCALDATE_MEDIUM_VALUE = "Feb 12, 1951";
			
			@DateParameter(value = LOCALDATE_MEDIUM_CONSIDERING_TIME_NAME, style = MEDIUM, considerTime = true)
			private final LocalDate localDateMediumConsideringTime;
			
			@DateParameter(style = MEDIUM)
			private final LocalDate localDateMedium;
			
			public TestClass(final LocalDate localDateMediumConsideringTime, final LocalDate localDateMedium) {
				this.localDateMediumConsideringTime = localDateMediumConsideringTime;
				this.localDateMedium = localDateMedium;
			}
		}
		
		TestClass testClass = new TestClass(LocalDate.of(1996, Month.SEPTEMBER, 22), LocalDate.of(1951, Month.FEBRUARY, 12));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.LOCALDATE_MEDIUM_CONSIDERING_TIME_NAME, localDate.getName());
		assertEquals(TestClass.LOCALDATE_MEDIUM_CONSIDERING_TIME_NAME_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.LOCALDATE_MEDIUM_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.LOCALDATE_MEDIUM_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateWithPatternTest() {
		class TestClass {
			public static final String LOCALDATE_PATTERN_DEFAULT_NAME = "localDatePatternDefaultName";
			public static final String LOCALDATE_PATTERN_DEFAULT_NAME_PATTERN = "yyyy-MM-dd";
			public static final String LOCALDATE_PATTERN_DEFAULT_NAME_VALUE = "1983-02-24";
			
			public static final String LOCALDATE_PATTERN_NAME = "CustomLocalDatePatternName";
			public static final String LOCALDATE_PATTERN_PATTERN = "dd/MM/yyyy";
			public static final String LOCALDATE_PATTERN_VALUE = "05/02/1999";
			
			@DateParameter(pattern = LOCALDATE_PATTERN_DEFAULT_NAME_PATTERN)
			private final LocalDate localDatePatternDefaultName;
			
			@DateParameter(value = LOCALDATE_PATTERN_NAME, pattern = LOCALDATE_PATTERN_PATTERN)
			private final LocalDate localDatePattern;

			public TestClass(final LocalDate localDatePatternDefaultName, final LocalDate localDatePattern) {
				this.localDatePatternDefaultName = localDatePatternDefaultName;
				this.localDatePattern = localDatePattern;
			}
		}

		TestClass testClass = new TestClass(LocalDate.of(1983, Month.FEBRUARY, 24), LocalDate.of(1999, Month.FEBRUARY, 5));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.LOCALDATE_PATTERN_DEFAULT_NAME, localDate.getName());
		assertEquals(TestClass.LOCALDATE_PATTERN_DEFAULT_NAME_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.LOCALDATE_PATTERN_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.LOCALDATE_PATTERN_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateTimeWithShortStyleTest() {
		class TestClass {
			public static final String LOCALDATETIME_SHORT_NAME = "localDateTimeShort";
			public static final String LOCALDATETIME_SHORT_VALUE = "11/11/70";
			
			public static final String LOCALDATETIME_SHORT_CONSIDERING_TIME_NAME = "CustomNameLocalDateTimeShortConsideringTime";
			public static final String LOCALDATETIME_SHORT_CONSIDERING_TIME_VALUE = "2/23/96 5:10 PM";
			
			@DateParameter
			private final LocalDateTime localDateTimeShort;
			
			@DateParameter(value = LOCALDATETIME_SHORT_CONSIDERING_TIME_NAME, considerTime = true)
			private final LocalDateTime localDateTimeShortConsideringTime;
			
			public TestClass(final LocalDateTime localDateTimeShort, final LocalDateTime localDateTimeShortConsideringTime) {
				this.localDateTimeShort = localDateTimeShort;
				this.localDateTimeShortConsideringTime = localDateTimeShortConsideringTime;
			}
		}
		
		TestClass testClass = new TestClass(LocalDateTime.of(1970, Month.NOVEMBER, 11, 18, 35), 
											LocalDateTime.of(1996, Month.FEBRUARY, 23, 17, 10));
		
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.LOCALDATETIME_SHORT_NAME, localDate.getName());
		assertEquals(TestClass.LOCALDATETIME_SHORT_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.LOCALDATETIME_SHORT_CONSIDERING_TIME_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.LOCALDATETIME_SHORT_CONSIDERING_TIME_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsLocalDateTimeWithMediumStyleTest() {
		class TestClass {
			public static final String LOCALDATETIME_MEDIUM_CONSIDERING_TIME_NAME = "CustomNameLocalDateTimeMediumConsideringTime";
			public static final String LOCALDATETIME_MEDIUM_CONSIDERING_TIME_NAME_VALUE = "Jan 18, 1993 10:49:00 PM";
			
			public static final String LOCALDATETIME_MEDIUM_NAME = "localDateTimeMedium";
			public static final String LOCALDATETIME_MEDIUM_VALUE = "Dec 7, 1988";
			
			@DateParameter(value = LOCALDATETIME_MEDIUM_CONSIDERING_TIME_NAME, style = MEDIUM, considerTime = true)
			private final LocalDateTime localDateTimeMediumConsideringTime;
			
			@DateParameter(style = MEDIUM)
			private final LocalDateTime localDateTimeMedium;
			
			public TestClass(final LocalDateTime localDateTimeMediumConsideringTime, final LocalDateTime localDateTimeMedium) {
				this.localDateTimeMediumConsideringTime = localDateTimeMediumConsideringTime;
				this.localDateTimeMedium = localDateTimeMedium;
			}
		}

		TestClass testClass = new TestClass(LocalDateTime.of(1993, Month.JANUARY, 18, 22, 49), LocalDateTime.of(1988, Month.DECEMBER, 7, 16, 0));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.LOCALDATETIME_MEDIUM_CONSIDERING_TIME_NAME, localDate.getName());
		assertEquals(TestClass.LOCALDATETIME_MEDIUM_CONSIDERING_TIME_NAME_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.LOCALDATETIME_MEDIUM_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.LOCALDATETIME_MEDIUM_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	public void findDateParameterAsLocalDateTimeWithPatternTest() {
		class TestClass {
			public static final String LOCALDATETIME_PATTERN_DEFAULT_NAME = "localDateTimePatternDefaultName";
			public static final String LOCALDATETIME_PATTERN_DEFAULT_NAME_PATTERN = "yyyy-MM-dd";
			public static final String LOCALDATETIME_PATTERN_DEFAULT_NAME_VALUE = "1988-11-02";
			
			public static final String LOCALDATETIME_PATTERN_NAME = "CustomLocalDateTimePatternName";
			public static final String LOCALDATETIME_PATTERN_PATTERN = "dd/MM/yyyy";
			public static final String LOCALDATETIME_PATTERN_VALUE = "12/04/1973";
			
			@DateParameter(pattern = LOCALDATETIME_PATTERN_DEFAULT_NAME_PATTERN)
			private final LocalDateTime localDateTimePatternDefaultName;
			
			@DateParameter(value = LOCALDATETIME_PATTERN_NAME, pattern = LOCALDATETIME_PATTERN_PATTERN)
			private final LocalDateTime localDateTimePattern;

			public TestClass(final LocalDateTime localDateTimePatternDefaultName, final LocalDateTime localDateTimePattern) {
				this.localDateTimePatternDefaultName = localDateTimePatternDefaultName;
				this.localDateTimePattern = localDateTimePattern;
			}
		}

		TestClass testClass = new TestClass(LocalDateTime.of(1988, Month.NOVEMBER, 2, 0, 57), LocalDateTime.of(1973, Month.APRIL, 12, 8, 34));
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.LOCALDATETIME_PATTERN_DEFAULT_NAME, localDate.getName());
		assertEquals(TestClass.LOCALDATETIME_PATTERN_DEFAULT_NAME_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.LOCALDATETIME_PATTERN_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.LOCALDATETIME_PATTERN_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsDateWithShortStyleTest() throws ParseException {
		class TestClass {
			public static final String DATE_SHORT_NAME = "dateShort";
			public static final String DATE_SHORT_VALUE = "2/18/18";
			
			public static final String DATE_SHORT_CONSIDERING_TIME_NAME = "CustomNameDateShortConsideringTime";
			public static final String DATE_SHORT_CONSIDERING_TIME_VALUE = "5/9/13 12:01 AM";
			
			@DateParameter
			private final Date dateShort;
			
			@DateParameter(value = DATE_SHORT_CONSIDERING_TIME_NAME, considerTime = true)
			private final Date dateShortConsideringTime;
			
			public TestClass(final Date dateShort, final Date dateShortConsideringTime) {
				this.dateShort = dateShort;
				this.dateShortConsideringTime = dateShortConsideringTime;
			}
		}
		
		Date dateShort = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("18/02/2018 10:42");
		Date dateShortConsideringTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("09/05/2013 12:01");
		
		TestClass testClass = new TestClass(dateShort, dateShortConsideringTime);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_SHORT_NAME, localDate.getName());
		assertEquals(TestClass.DATE_SHORT_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_SHORT_CONSIDERING_TIME_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DATE_SHORT_CONSIDERING_TIME_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsDateWithMediumStyleTest() throws ParseException {
		class TestClass {
			public static final String DATE_MEDIUM_CONSIDERING_TIME_NAME = "CustomNameDateMediumConsideringTime";
			public static final String DATE_MEDIUM_CONSIDERING_TIME_NAME_VALUE = "Apr 1, 1994 7:22:00 PM";
			
			public static final String DATE_MEDIUM_NAME = "dateMedium";
			public static final String DATE_MEDIUM_VALUE = "Aug 24, 1990";
			
			@DateParameter(value = DATE_MEDIUM_CONSIDERING_TIME_NAME, style = MEDIUM, considerTime = true)
			private final Date dateMediumConsideringTime;
			
			@DateParameter(style = MEDIUM)
			private final Date dateMedium;
			
			public TestClass(final Date dateMediumConsideringTime, final Date dateMedium) {
				this.dateMediumConsideringTime = dateMediumConsideringTime;
				this.dateMedium = dateMedium;
			}
		}

		Date dateMediumConsideringTime = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("01/04/1994 19:22");
		Date dateMedium = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("24/08/1990 04:45");

		TestClass testClass = new TestClass(dateMediumConsideringTime, dateMedium);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_MEDIUM_CONSIDERING_TIME_NAME, localDate.getName());
		assertEquals(TestClass.DATE_MEDIUM_CONSIDERING_TIME_NAME_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_MEDIUM_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DATE_MEDIUM_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsDateWithPatternTest() throws ParseException {
		class TestClass {
			public static final String DATE_PATTERN_DEFAULT_NAME = "datePatternDefaultName";
			public static final String DATE_PATTERN_DEFAULT_NAME_PATTERN = "yyyy-MM-dd";
			public static final String DATE_PATTERN_DEFAULT_NAME_VALUE = "2005-09-27";
			
			public static final String DATE_PATTERN_NAME = "CustomDatePatternName";
			public static final String DATE_PATTERN_PATTERN = "dd/MM/yyyy";
			public static final String DATE_PATTERN_VALUE = "25/12/2014";
			
			@DateParameter(pattern = DATE_PATTERN_DEFAULT_NAME_PATTERN)
			private final Date datePatternDefaultName;
			
			@DateParameter(value = DATE_PATTERN_NAME, pattern = DATE_PATTERN_PATTERN)
			private final Date datePattern;

			public TestClass(final Date datePatternDefaultName, final Date datePattern) {
				this.datePatternDefaultName = datePatternDefaultName;
				this.datePattern = datePattern;
			}
		}

		Date datePatternDefaultName = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("27/09/2005 01:13");
		Date datePattern = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse("25/12/2014 00:00");
		
		TestClass testClass = new TestClass(datePatternDefaultName, datePattern);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.DATE_PATTERN_DEFAULT_NAME, localDate.getName());
		assertEquals(TestClass.DATE_PATTERN_DEFAULT_NAME_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.DATE_PATTERN_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.DATE_PATTERN_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsCalendarWithShortStyleTest() {
		class TestClass {
			public static final String CALENDAR_SHORT_NAME = "calendarShort";
			public static final String CALENDAR_SHORT_VALUE = "10/17/06";
			
			public static final String CALENDAR_SHORT_CONSIDERING_TIME_NAME = "CustomNameCalendarShortConsideringTime";
			public static final String CALENDAR_SHORT_CONSIDERING_TIME_VALUE = "12/17/09 7:27 AM";
			
			@DateParameter
			private final Calendar calendarShort;
			
			@DateParameter(value = CALENDAR_SHORT_CONSIDERING_TIME_NAME, considerTime = true)
			private final Calendar calendarShortConsideringTime;
			
			public TestClass(final Calendar calendarShort, final Calendar calendarShortConsideringTime) {
				this.calendarShort = calendarShort;
				this.calendarShortConsideringTime = calendarShortConsideringTime;
			}
		}

		Calendar calendarShort = Calendar.getInstance();
		calendarShort.set(2006, Calendar.OCTOBER, 17, 23, 16, 0);

		Calendar calendarShortConsideringTime = Calendar.getInstance();
		calendarShortConsideringTime.set(2009, Calendar.DECEMBER, 17, 7, 27, 0);
		
		TestClass testClass = new TestClass(calendarShort, calendarShortConsideringTime);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.CALENDAR_SHORT_NAME, localDate.getName());
		assertEquals(TestClass.CALENDAR_SHORT_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.CALENDAR_SHORT_CONSIDERING_TIME_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.CALENDAR_SHORT_CONSIDERING_TIME_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterACalendarWithMediumStyleTest() {
		class TestClass {
			public static final String CALENDAR_MEDIUM_CONSIDERING_TIME_NAME = "CustomNameLocalDateTimeParameter";
			public static final String CALENDAR_MEDIUM_CONSIDERING_TIME_NAME_VALUE = "Apr 28, 2001 5:23:00 PM";
			
			public static final String CALENDAR_MEDIUM_NAME = "calendarMedium";
			public static final String CALENDAR_MEDIUM_VALUE = "Oct 12, 1999";
			
			@DateParameter(value = CALENDAR_MEDIUM_CONSIDERING_TIME_NAME, style = MEDIUM, considerTime = true)
			private final Calendar calendarMediumConsideringTime;
			
			@DateParameter(style = MEDIUM)
			private final Calendar calendarMedium;
			
			public TestClass(final Calendar calendarMediumConsideringTime, final Calendar calendarMedium) {
				this.calendarMediumConsideringTime = calendarMediumConsideringTime;
				this.calendarMedium = calendarMedium;
			}
		}

		Calendar calendarMediumConsideringTime = Calendar.getInstance();
		calendarMediumConsideringTime.set(2001, Calendar.APRIL, 28, 17, 23, 0);

		Calendar calendarMedium = Calendar.getInstance();
		calendarMedium.set(1999, Calendar.OCTOBER, 12, 18, 0, 0);

		TestClass testClass = new TestClass(calendarMediumConsideringTime, calendarMedium);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.CALENDAR_MEDIUM_CONSIDERING_TIME_NAME, localDate.getName());
		assertEquals(TestClass.CALENDAR_MEDIUM_CONSIDERING_TIME_NAME_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.CALENDAR_MEDIUM_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.CALENDAR_MEDIUM_VALUE, defaultLocalDate.getFormattedValue(US));
		assertEquals(DATE, defaultLocalDate.getType());
	}
	
	@Test
	public void findDateParameterAsCalendarWithPatternTest() {
		class TestClass {
			public static final String CALENDAR_PATTERN_DEFAULT_NAME = "calendarPatternDefaultName";
			public static final String CALENDAR_PATTERN_DEFAULT_NAME_PATTERN = "yyyy-MM-dd";
			public static final String CALENDAR_PATTERN_DEFAULT_NAME_VALUE = "1987-06-01";
			
			public static final String CALENDAR_PATTERN_NAME = "CustomDatePatternName";
			public static final String CALENDAR_PATTERN_PATTERN = "dd/MM/yyyy";
			public static final String CALENDAR_PATTERN_VALUE = "24/07/2005";
			
			@DateParameter(pattern = CALENDAR_PATTERN_DEFAULT_NAME_PATTERN)
			private final Calendar calendarPatternDefaultName;
			
			@DateParameter(value = CALENDAR_PATTERN_NAME, pattern = CALENDAR_PATTERN_PATTERN)
			private final Calendar calendarPattern;

			public TestClass(final Calendar calendarPatternDefaultName, final Calendar calendarPattern) {
				this.calendarPatternDefaultName = calendarPatternDefaultName;
				this.calendarPattern = calendarPattern;
			}
		}

		Calendar calendarPatternDefaultName = Calendar.getInstance();
		calendarPatternDefaultName.set(1987, Calendar.JUNE, 1, 8, 8, 0);

		Calendar calendarPattern = Calendar.getInstance();
		calendarPattern.set(2005, Calendar.JULY, 24, 6, 15, 0);
		
		TestClass testClass = new TestClass(calendarPatternDefaultName, calendarPattern);
		GenericParameterFinder finder = new GenericParameterFinder(testClass);
		List<br.com.rooting.roxana.parameter.Parameter> parameters = finder.findParameters();
		assertEquals(parameters.size(), 2);
		
		br.com.rooting.roxana.parameter.Parameter localDate = parameters.get(0);
		assertEquals(TestClass.CALENDAR_PATTERN_DEFAULT_NAME, localDate.getName());
		assertEquals(TestClass.CALENDAR_PATTERN_DEFAULT_NAME_VALUE, localDate.getFormattedValue(US));
		assertEquals(DATE, localDate.getType());
		
		br.com.rooting.roxana.parameter.Parameter defaultLocalDate = parameters.get(1);
		assertEquals(TestClass.CALENDAR_PATTERN_NAME, defaultLocalDate.getName());
		assertEquals(TestClass.CALENDAR_PATTERN_VALUE, defaultLocalDate.getFormattedValue(US));
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