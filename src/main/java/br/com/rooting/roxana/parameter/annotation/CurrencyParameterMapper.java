package br.com.rooting.roxana.parameter.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(CurrencyParametersMapper.class)
public @interface CurrencyParameterMapper {

	public String value();
	
}
