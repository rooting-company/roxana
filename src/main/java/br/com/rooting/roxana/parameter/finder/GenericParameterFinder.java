package br.com.rooting.roxana.parameter.finder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.mapper.CurrencyParam;
import br.com.rooting.roxana.parameter.mapper.DateParam;

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
				if(field.isAnnotationPresent(br.com.rooting.roxana.parameter.mapper.Param.class)) {
					br.com.rooting.roxana.parameter.mapper.Param parameterAnnotation = 
					field.getDeclaredAnnotation(br.com.rooting.roxana.parameter.mapper.Param.class);
					
					parameters.add(this.createParameterBaseOn(field, parameterAnnotation));
					
				} else if(field.isAnnotationPresent(DateParam.class)) {
					parameters.add(this.createParameterBaseOn(field, field.getDeclaredAnnotation(DateParam.class)));
					
				} else if(field.isAnnotationPresent(CurrencyParam.class)) {
					parameters.add(this.createParameterBaseOne(field, field.getDeclaredAnnotation(CurrencyParam.class)));
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
			final br.com.rooting.roxana.parameter.mapper.Param parameterAnnotation)
			throws IllegalArgumentException, IllegalAccessException {
		
		String defaultName = br.com.rooting.roxana.parameter.mapper.Param.DEFAULT_VALUE;
		String name = parameterAnnotation.value().equals(defaultName) ? field.getName() : parameterAnnotation.value();
		return Parameter.create(name, field.get(this.getObject()));
	}
	
	private Parameter createParameterBaseOn(final Field field, final DateParam dateParameter)
			throws IllegalArgumentException, IllegalAccessException {
		
		String name = dateParameter.value().equals(DateParam.DEFAULT_VALUE) ? field.getName() : dateParameter.value();
		return Parameter.createDateParameter(name, 
											 field.get(this.getObject()), 
											 dateParameter.style(), 
											 dateParameter.considerTime(), 
											 dateParameter.pattern());
	}
	
	private Parameter createParameterBaseOne(final Field field, final CurrencyParam currencyParameter)
			throws IllegalArgumentException, IllegalAccessException {
		
		String name = currencyParameter.value().equals(CurrencyParam.DEFAULT_VALUE) ? field.getName() : currencyParameter.value();
		return Parameter.createCurrencyParameter(name, field.get(this.getObject()));
	}
	
	private String getFailToFindParameterMessage(final Field field) {
		return EXCETION_WHEN_PASSING_PARAMETER + field.getDeclaringClass() + "." + field.getName();
	}
	
	private Object getObject() {
		return this.object;
	}

}