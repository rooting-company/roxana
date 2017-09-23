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
@Repeatable(DateParametersMapper.class)
public @interface DateParameterMapper {
	
	public static final String DEFAULT_PATTNER = "yyyy-MM-dd";
	
	public String value();
	
	public String pattern() default DEFAULT_PATTNER;
	
}
