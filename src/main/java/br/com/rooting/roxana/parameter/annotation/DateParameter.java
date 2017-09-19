package br.com.rooting.roxana.parameter.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// TODO ADD suporte a @AliasFor para @MessageParameter
// TODO ADD suporte a FormatSkyle 

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(DateParameters.class)
public @interface DateParameter {
	
	public static String EMPTY_PATTNER = "yyyy-MM-dd";
	
	public String value();
	
	public String pattern() default EMPTY_PATTNER;
	
}
