package br.com.rooting.roxana.config.swagger;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Import(SwaggerAlternateTypeRuleConfig.class)
@EnableConfigurationProperties
public @interface EnableRoxanaSwaggerSupport {

}