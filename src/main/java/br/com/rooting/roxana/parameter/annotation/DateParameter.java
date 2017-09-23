package br.com.rooting.roxana.parameter.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// TODO ADD suporte a @AliasFor para @MessageParameter
// TODO ADD suporte a FormatSkyle 

@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface DateParameter {
	
	public static final String DEFAULT_PATTNER = "yyyy-MM-dd";
	
	public static final String DEFAULT_VALUE = "";
	
	public String value() default DEFAULT_VALUE;
	
	public String pattern() default DEFAULT_PATTNER;
	
}