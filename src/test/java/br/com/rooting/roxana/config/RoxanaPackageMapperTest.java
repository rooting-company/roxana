package br.com.rooting.roxana.config;

import static br.com.rooting.roxana.utils.ReflectionUtils.isPackagePrivate;
import static java.lang.reflect.Modifier.isFinal;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.rooting.roxana.UnitTest;

public class RoxanaPackageMapperTest extends UnitTest<RoxanaPackageMapper> {

	@Test
	public void testClassIsPrivateTest() {
		assertTrue(isPackagePrivate(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	public void testClassIsFinalTest() {
		assertTrue(isFinal(this.getUnitTestClass().getModifiers()));
	}
	
}