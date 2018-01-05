package br.com.rooting.roxana.business;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface BusinessConstraintValidator {
	
}