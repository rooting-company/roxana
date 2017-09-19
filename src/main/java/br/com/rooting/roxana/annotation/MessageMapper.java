package br.com.rooting.roxana.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Inherited
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface MessageMapper {
	
	public static final String MESSAGE_NOT_DEFINED = "";
	
	String value() default MESSAGE_NOT_DEFINED;
}
