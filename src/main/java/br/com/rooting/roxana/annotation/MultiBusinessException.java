package br.com.rooting.roxana.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;


@Inherited
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface MultiBusinessException {
	
	HttpStatus responseCode() default HttpStatus.UNPROCESSABLE_ENTITY;

}
