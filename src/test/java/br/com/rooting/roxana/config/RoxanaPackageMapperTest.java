package br.com.rooting.roxana.config;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.rooting.roxana.RoxanaPackageMapper;
import br.com.rooting.roxana.UnitTest;

public class RoxanaPackageMapperTest extends UnitTest<RoxanaPackageMapper> {

	@Test
	public void testClassIsPackagePublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	public void testClassIsFinalTest() {
		assertTrue(isFinal(this.getUnitTestClass().getModifiers()));
	}
	
}