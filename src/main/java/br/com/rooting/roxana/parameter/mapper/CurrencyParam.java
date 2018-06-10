package br.com.rooting.roxana.parameter.mapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import br.com.rooting.roxana.parameter.mapper.CurrencyParam.CurrencyParams;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(CurrencyParams.class)
public @interface CurrencyParam {

	public static final String DEFAULT_VALUE = "";
	
	public String value() default DEFAULT_VALUE;
	
	@Documented
	@Target(FIELD)
	@Retention(RUNTIME)
	@interface CurrencyParams {
		
		CurrencyParam[] value();
		
	}
	
}