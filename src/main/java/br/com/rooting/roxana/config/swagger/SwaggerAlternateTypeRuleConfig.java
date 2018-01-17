package br.com.rooting.roxana.config.swagger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;

import br.com.rooting.roxana.config.RoxanaProperties;
import br.com.rooting.roxana.config.RoxanaProperties.Business.ResponseStrategy;
import br.com.rooting.roxana.message.Message;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;

@Configuration
class SwaggerAlternateTypeRuleConfig {

	private final ResponseStrategy responseStrategy;
	
	@Autowired
	SwaggerAlternateTypeRuleConfig(final RoxanaProperties properties) {
		this.responseStrategy = properties.getBusinessResponseStrategy();
	}

	@Bean
	public AlternateTypeRuleConvention roxanaAlternateTypeRuleConvertion(final TypeResolver resolver) {
		return new AlternateTypeRuleConvention() {

			@Override
			public int getOrder() {
				return Ordered.HIGHEST_PRECEDENCE;
			}

			@Override
			public List<AlternateTypeRule> rules() {
				List<AlternateTypeRule> rules = new ArrayList<>();
				rules.add(SwaggerAlternateTypeRuleConfig.this.getMessageRule(resolver));
				rules.add(SwaggerAlternateTypeRuleConfig.this.getListMessageRule(resolver));
				rules.add(SwaggerAlternateTypeRuleConfig.this.getArrayListMessageRule(resolver));
				rules.add(SwaggerAlternateTypeRuleConfig.this.getLinkedListMessageRule(resolver));
				return rules;
			}
		};
	}
	
	private AlternateTypeRule getMessageRule(final TypeResolver resolver) {
		return new AlternateTypeRule(resolver.resolve(Message.class), this.getContreteMessageResolvedType(resolver));
	}
	
	private AlternateTypeRule getListMessageRule(final TypeResolver resolver) {
		return new AlternateTypeRule(this.getMessageListResolvedType(resolver), this.getContreteMessageResolvedType(resolver));
	}
	
	private AlternateTypeRule getArrayListMessageRule(final TypeResolver resolver) {
		ResolvedType arrayListMessageResolvedType = resolver.resolveSubtype(this.getMessageListResolvedType(resolver), ArrayList.class);
		return new AlternateTypeRule(arrayListMessageResolvedType, this.getContreteMessageResolvedType(resolver));
	}
	
	private AlternateTypeRule getLinkedListMessageRule(final TypeResolver resolver) {
		ResolvedType linkedListMessageResolvedType = resolver.resolveSubtype(this.getMessageListResolvedType(resolver), LinkedList.class);
		return new AlternateTypeRule(linkedListMessageResolvedType, this.getContreteMessageResolvedType(resolver));
	}
	
	private ResolvedType getMessageListResolvedType(final TypeResolver resolver) {
		return resolver.resolve(List.class, Message.class);
	}
	
	private ResolvedType getContreteMessageResolvedType(final TypeResolver resolver) {
		return resolver.resolve(this.getResponseStrategy().getConcreteMessageClass());
	}

	private ResponseStrategy getResponseStrategy() {
		return this.responseStrategy;
	}
	
}