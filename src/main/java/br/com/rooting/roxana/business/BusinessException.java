package br.com.rooting.roxana.business;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

import br.com.rooting.roxana.message.MessageSeverity;

@Inherited
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface BusinessException {
	
	public static final String MESSAGE_NOT_DEFINED = "";

	HttpStatus responseCode() default HttpStatus.UNPROCESSABLE_ENTITY;

	String message() default MESSAGE_NOT_DEFINED;
	
	MessageSeverity severity() default MessageSeverity.ERROR;
}
