package br.com.rooting.roxana.parameter.mapper;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface DateParameter {
	
	public static final String NONE_PATTERN = "none";
	
	public static final String DEFAULT_VALUE = "";
	
	public String value() default DEFAULT_VALUE;
	
	public String pattern() default NONE_PATTERN;
	
	public DateStyle style() default DateStyle.SHORT;
	
	public boolean considerTime() default false;
	
}