package br.com.rooting.roxana.message.mapper.parameter;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(CurrencyMessageParameters.class)
public @interface CurrencyMessageParameter {

	public String value();
	
}