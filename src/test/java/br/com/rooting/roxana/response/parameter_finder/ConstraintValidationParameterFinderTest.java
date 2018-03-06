package br.com.rooting.roxana.response.parameter_finder;

import static java.lang.reflect.Modifier.isPublic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;
import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.ParameterType;

public class ConstraintValidationParameterFinderTest extends UnitTest<ConstraintValidationParameterFinder> {

	private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
	private static final Validator VALIDATOR = VALIDATOR_FACTORY.getValidator();
	
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
	public void constraintViolationCanNotBeNull() {
		new ConstraintValidationParameterFinder(null);
	}
	
	@Test
	public void supportToConstraintValidationTest() {
		ConstraintValidatorTest testClass = new ConstraintValidatorTest();
		Set<ConstraintViolation<Object>> violations = VALIDATOR.validate(testClass);
		
		violations.stream().forEach(violation -> {
			ConstraintValidationParameterFinder finder = new ConstraintValidationParameterFinder(violation);
			List<Parameter> parameters = finder.findParameters();
			
			if (violationIsFrom(violation, NotBlank.class)) {
				validateNotBlankViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, NotNull.class)) {
				validateNotNullViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Min.class)) {
				validateMinViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Max.class))  {
				validateMaxViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, NotEmpty.class)) {
				validateNotEmptyViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Size.class)) {
				validateSizeViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Future.class)) {
				validateFutureViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Past.class)) {
				validatePastViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, FutureOrPresent.class)) {
				validateFutureOrPresentViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, PastOrPresent.class)) {
				validatePastOrPresentViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Positive.class)) {
				validatePositiveViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, PositiveOrZero.class)) {
				validatePositiveOrZeroViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Negative.class)) {
				validateNegativeViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, NegativeOrZero.class)) {
				validateNegativeOrZeroViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Email.class)) {
				validadteEmailViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, AssertTrue.class)) {
				validateAssertTrueViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, AssertFalse.class)) {
				validateAssertFalseViolation(violation, parameters);
				
			} else if (violationIsFrom(violation, Pattern.class)) {
				validatePatternViolation(violation, parameters);
			}
		});
	}
	
	private static void validateNotBlankViolation(final ConstraintViolation<?> violation, 
											      final List<Parameter> parameters) {
		
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Not Blank"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 
													ConstraintValidatorTest.EMPTY_STRING));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateNotNullViolation(final ConstraintViolation<?> violation, 
			   								     final List<Parameter> parameters) {
		
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Not Null"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 
													ConstraintValidatorTest.EMPTY_STRING));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateMinViolation(final ConstraintViolation<?> violation, 
			     							 final List<Parameter> parameters) {
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Min 100"));
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 10));
		validations.add(new ParameterTestValidation("value", 100L));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateMaxViolation(final ConstraintViolation<?> violation, 
			 								 final List<Parameter> parameters) {
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Max 1000"));
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 10000));
		validations.add(new ParameterTestValidation("value", 1000L));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateNotEmptyViolation(final ConstraintViolation<?> violation, 
			 									  final List<Parameter> parameters) {
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Not Empty"));
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, new ArrayList<>()));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateSizeViolation(final ConstraintViolation<?> violation, 
			     							  final List<Parameter> parameters) {
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, 
													"Size Min 3 Max 100"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 
													Arrays.asList("One element")));
		
		validations.add(new ParameterTestValidation("min", 3));
		validations.add(new ParameterTestValidation("max", 100));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateFutureViolation(final ConstraintViolation<?> violation, 
			  									final List<Parameter> parameters) {
		
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, 
													"Future Date"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 
													LocalDate.of(1992, 12, 11)));
		
		validateViolation(validations, parameters);
	}
	
	private static void validatePastViolation(final ConstraintViolation<?> violation, 
											  final List<Parameter> parameters) {

		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, 
													"Past Date"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 
													LocalDate.of(3000, 01, 01)));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateFutureOrPresentViolation(final ConstraintViolation<?> violation, 
											  			 final List<Parameter> parameters) {
		
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, 
													"Future Or Present Date"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 
													LocalDate.of(1992, 12, 11)));
		
		validateViolation(validations, parameters);
	}
	
	private static void validatePastOrPresentViolation(final ConstraintViolation<?> violation, 
											  		   final List<Parameter> parameters) {
		
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, 
													"Past Or Present Date"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 
													LocalDate.of(3000, 01, 01)));
		
		validateViolation(validations, parameters);
	}
	
	private static void validatePositiveViolation(final ConstraintViolation<?> violation, 
	  		   									  final List<Parameter> parameters) {

		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Positive Number"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, -3));
		
		validateViolation(validations, parameters);
	}
	
	private static void validatePositiveOrZeroViolation(final ConstraintViolation<?> violation, 
				  										final List<Parameter> parameters) {

		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Positive Or Zero Number"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, -10));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateNegativeViolation(final ConstraintViolation<?> violation, 
												  final List<Parameter> parameters) {

		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Negative Number"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 10));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateNegativeOrZeroViolation(final ConstraintViolation<?> violation, 
														final List<Parameter> parameters) {

		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Negative Or Zero Number"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, 1));
		
		validateViolation(validations, parameters);
	}
	
	private static void validadteEmailViolation(final ConstraintViolation<?> violation, 
												final List<Parameter> parameters) {
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Email"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, "brunoluisncostagmail.com"));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateAssertTrueViolation(final ConstraintViolation<?> violation, 
													final List<Parameter> parameters) {
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Assert True"));
		
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, false));
		
		validateViolation(validations, parameters);
	}
	
	private static void validateAssertFalseViolation(final ConstraintViolation<?> violation, 
													 final List<Parameter> parameters) {
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Assert False"));

		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, true));

		validateViolation(validations, parameters);
	}
	
	private static void validatePatternViolation(final ConstraintViolation<?> violation, 
			 									 final List<Parameter> parameters) {
		List<ParameterTestValidation> validations = new ArrayList<>();
		validations.add(new ParameterTestValidation(ConstraintValidatorTest.PROPERTY_NAME_PARAMETER, "Pattern Only Number"));

		validations.add(new ParameterTestValidation(ConstraintValidatorTest.INVALID_VALUE_PARAMETER, "I am a String"));
		validations.add(new ParameterTestValidation("regexp", "\\d*"));
		
		validateViolation(validations, parameters);
	}
	
	private static Boolean violationIsFrom(final ConstraintViolation<?> violation, 
			   							   final Class<?> constraintClass) {

		return violation.getConstraintDescriptor()
						.getAnnotation()
						.annotationType()
						.isAssignableFrom(constraintClass);
	}
	
	private static void validateViolation(final List<ParameterTestValidation> validations, 
									   	  final List<Parameter> parameters) {
		
		validations.forEach(v -> {
			Optional<Parameter> parameterOP = parameters.stream()
													  .filter(p -> p.getName().equals(v.getName()))
													  .findFirst();
			
			if (parameterOP.isPresent()) {
				Parameter parameter = parameterOP.get();
				assertEquals(v.getValue(), parameter.getValue());
				assertEquals(ParameterType.STRING, parameter.getType());			
			} else {
				assertTrue(false);
			}
			
		});
	}
	
	private static class ConstraintValidatorTest {
		
		private static final String PROPERTY_NAME_PARAMETER = "propertyName";
		private static final String INVALID_VALUE_PARAMETER = "invalidValue";
		
		private static final String EMPTY_STRING = "";
		
		@Null
		private final String nulll = EMPTY_STRING;
		
		@NotBlank
		private final String notBlank = EMPTY_STRING;
		
		@NotNull
		private final Object notNull = null;
		
		@Min(100)
		private final Integer min100 = 10;
		
		@Max(1000)
		private final Integer max1000 = 10000;
		
		@NotEmpty
		private final List<String> notEmpty = new ArrayList<>();
		
		@Size(min = 3, max = 100)
		private final List<String> sizeMin3Max100 = Arrays.asList("One element");
		
		@Future
		private final LocalDate futureDate = LocalDate.of(1992, 12, 11);
		
		@Past
		private final LocalDate pastDate = LocalDate.of(3000, 01, 01);
		
		@FutureOrPresent
		private final LocalDate futureOrPresentDate = LocalDate.of(1992, 12, 11);
		
		@PastOrPresent
		private final LocalDate pastOrPresentDate = LocalDate.of(3000, 01, 01);
		
		@Positive
		private final Integer positiveNumber = -3;
		
		@PositiveOrZero
		private final Integer positiveOrZeroNumber = -10;
		
		@Negative
		private final Integer negativeNumber = 10;
		
		@NegativeOrZero
		private final Integer negativeOrZeroNumber = 1;
		
		@Email
		private final String email = "brunoluisncostagmail.com";
		
		@AssertTrue
		private final Boolean assertTrue = false;
		
		@AssertFalse
		private final Boolean assertFalse = true;
		
		@Pattern(regexp = "\\d*")
		private final String patternOnlyNumber = "I am a String";
		
	}
	
	private static class ParameterTestValidation {
		
		private final String name;
		
		private final Object value;
		
		public ParameterTestValidation(String name, Object value) {
			super();
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public Object getValue() {
			return value;
		}
	}
	
}