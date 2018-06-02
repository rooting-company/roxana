package br.com.rooting.roxana.message.mapper.parameter;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import br.com.rooting.roxana.parameter.mapper.DateParameter;
import br.com.rooting.roxana.parameter.mapper.DateStyle;

@Documented
@Target(FIELD)
@Retention(RUNTIME)
@Repeatable(DateMessageParameters.class)
public @interface DateMessageParameter {
	
	public String value();
	
	public String pattern() default DateParameter.NONE_PATTERN;
	
	public DateStyle style() default DateStyle.SHORT;
	
	public boolean considerTime() default false;
	
}