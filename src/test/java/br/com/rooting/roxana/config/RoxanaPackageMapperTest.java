package br.com.rooting.roxana.config;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.rooting.roxana.RoxanaPackageMapper;
import br.com.rooting.roxana.UnitTest;
import org.junit.jupiter.api.Test;

class RoxanaPackageMapperTest extends UnitTest<RoxanaPackageMapper> {

	@Test
	void testClassIsPackagePublicTest() {
		assertTrue(isPublic(this.getUnitTestClass().getModifiers()));
	}
	
	@Test
	void testClassIsFinalTest() {
		assertTrue(isFinal(this.getUnitTestClass().getModifiers()));
	}
	
}