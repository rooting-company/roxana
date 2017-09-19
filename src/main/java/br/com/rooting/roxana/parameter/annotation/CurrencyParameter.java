package br.com.rooting.roxana.parameter.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

//TODO dar suporte a @AliasFor para @MessageParameter

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(CurrencyParameters.class)
public @interface CurrencyParameter {
	
	public String value();
	
}
