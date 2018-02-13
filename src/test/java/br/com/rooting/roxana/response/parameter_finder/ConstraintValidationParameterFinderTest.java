package br.com.rooting.roxana.response.parameter_finder;

import static java.lang.reflect.Modifier.isPublic;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;

public class ConstraintValidationParameterFinderTest extends UnitTest<ConstraintValidationParameterFinder> {

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

}
