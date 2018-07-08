package br.com.rooting.roxana.exception.mapper;

import br.com.rooting.roxana.message.MessageSeverity;
import org.springframework.http.HttpStatus;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface BusinessException {

    String MESSAGE_NOT_DEFINED = "";

    HttpStatus responseCode() default HttpStatus.UNPROCESSABLE_ENTITY;

    String message() default MESSAGE_NOT_DEFINED;

    MessageSeverity severity() default MessageSeverity.ERROR;
}
