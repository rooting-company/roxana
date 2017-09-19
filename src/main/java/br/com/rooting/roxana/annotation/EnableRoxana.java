package br.com.rooting.roxana.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import br.com.rooting.roxana.RoxanaConfiguration;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Import(RoxanaConfiguration.class)
@EnableConfigurationProperties
public @interface EnableRoxana {

}