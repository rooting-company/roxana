package br.com.rooting.roxana.business.parameter;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Parameter {
	
	public static final String DEFAULT_VALUE = "";
	
	public String value() default DEFAULT_VALUE;
	
}