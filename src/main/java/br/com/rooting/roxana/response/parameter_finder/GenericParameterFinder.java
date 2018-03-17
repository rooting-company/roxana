package br.com.rooting.roxana.response.parameter_finder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.rooting.roxana.business.parameter.CurrencyParameter;
import br.com.rooting.roxana.business.parameter.DateParameter;
import br.com.rooting.roxana.parameter.Parameter;

public class GenericParameterFinder implements ParameterFinderStrategy {

	private static final String EXCETION_WHEN_PASSING_PARAMETER = "Exception when passing parameter: ";

	private final Object object;
	
	public GenericParameterFinder(final Object object) throws IllegalArgumentException {
		if (object == null) {
			throw new IllegalArgumentException();
		}
		
		this.object = object;
	}

	@Override
	public List<Parameter> findParameters() {
		List<Parameter> parameters = new ArrayList<>();
		List<Field> fields = Arrays.asList(this.getObject().getClass().getDeclaredFields());
		
		fields.forEach(field -> {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			
			try {
				if(field.isAnnotationPresent(br.com.rooting.roxana.business.parameter.Parameter.class)) {
					br.com.rooting.roxana.business.parameter.Parameter parameterAnnotation = 
					field.getDeclaredAnnotation(br.com.rooting.roxana.business.parameter.Parameter.class);
					
					parameters.add(this.createParameterBaseOn(field, parameterAnnotation));
					
				} else if(field.isAnnotationPresent(DateParameter.class)) {
					parameters.add(this.createParameterBaseOn(field, field.getDeclaredAnnotation(DateParameter.class)));
					
				} else if(field.isAnnotationPresent(CurrencyParameter.class)) {
					parameters.add(this.createParameterBaseOne(field, field.getDeclaredAnnotation(CurrencyParameter.class)));
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// Não devera entrar aqui, pois o field pertence ao objeto passado.
				// e este metodo deixa o field acessivel atraves do setAccessible.
				throw new FailToFindParameterException(this.getFailToFindParameterMessage(field), e);
			}
		});
		return parameters;
	}
	
	private Parameter createParameterBaseOn(final Field field,
			final br.com.rooting.roxana.business.parameter.Parameter parameterAnnotation)
			throws IllegalArgumentException, IllegalAccessException {
		
		String defaultName = br.com.rooting.roxana.business.parameter.Parameter.DEFAULT_VALUE;
		String name = parameterAnnotation.value().equals(defaultName) ? field.getName() : parameterAnnotation.value();
		return Parameter.create(name, field.get(this.getObject()));
	}
	
	private Parameter createParameterBaseOn(final Field field, final DateParameter dateParameter)
			throws IllegalArgumentException, IllegalAccessException {
		
		String name = dateParameter.value().equals(DateParameter.DEFAULT_VALUE) ? field.getName() : dateParameter.value();
		return Parameter.createDateParameter(name, 
											 field.get(this.getObject()), 
											 dateParameter.style(), 
											 dateParameter.considerTime(), 
											 dateParameter.pattern());
	}
	
	private Parameter createParameterBaseOne(final Field field, final CurrencyParameter currencyParameter)
			throws IllegalArgumentException, IllegalAccessException {
		
		String name = currencyParameter.value().equals(CurrencyParameter.DEFAULT_VALUE) ? field.getName() : currencyParameter.value();
		return Parameter.createCurrencyParameter(name, field.get(this.getObject()));
	}
	
	private String getFailToFindParameterMessage(final Field field) {
		return EXCETION_WHEN_PASSING_PARAMETER + field.getDeclaringClass() + "." + field.getName();
	}
	
	private Object getObject() {
		return this.object;
	}

}