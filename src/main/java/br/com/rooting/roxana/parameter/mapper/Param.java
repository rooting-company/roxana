package br.com.rooting.roxana.parameter.mapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import br.com.rooting.roxana.parameter.mapper.Param.Params;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(Params.class)
public @interface Param {
	
	public static final String DEFAULT_VALUE = "";
	
	public String value() default DEFAULT_VALUE;
	
	@Documented
	@Target(FIELD)
	@Retention(RUNTIME)
	public @interface Params {
		
		Param[] value();
		
	}
	
}