package br.com.rooting.roxana.response.parameter.finder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.rooting.roxana.parameter.Parameter;
import br.com.rooting.roxana.parameter.annotation.CurrencyParameter;
import br.com.rooting.roxana.parameter.annotation.DateParameter;

public class GenericParameterFinder implements ParameterFinderStrategy {

	private static final String ERROR_EXCETION_WHEN_PASSING_PARAMETER = "Exception where passing parameter: ";

	private final Object object;
	
	public GenericParameterFinder(final Object object) {
		this.object = object;
	}

	@Override
	public List<Parameter> findParameters() {
		List<Parameter> parameters = new ArrayList<>();
		List<Field> fields = Arrays.asList(this.getObject().getClass().getDeclaredFields());
		
		fields.forEach(f -> {
			if (!f.isAccessible()) {
				f.setAccessible(true);
			}
			
			try {
				if(f.isAnnotationPresent(br.com.rooting.roxana.parameter.annotation.Parameter.class)) {
					br.com.rooting.roxana.parameter.annotation.Parameter mp = f.getDeclaredAnnotation(br.com.rooting.roxana.parameter.annotation.Parameter.class);
					String name = mp.value().equals(br.com.rooting.roxana.parameter.annotation.Parameter.DEFAULT_VALUE) ? f.getName() : mp.value();
					parameters.add(Parameter.create(name, f.get(this.getObject())));
					
				} else if(f.isAnnotationPresent(DateParameter.class)) {
					DateParameter dmp = f.getDeclaredAnnotation(DateParameter.class);
					String name = dmp.value().equals(DateParameter.DEFAULT_VALUE) ? f.getName() : dmp.value();
					parameters.add(Parameter.createDateParameter(name, f.get(this.getObject()), dmp.pattern()));
					
				} else if(f.isAnnotationPresent(CurrencyParameter.class)) {
					CurrencyParameter mmp = f.getDeclaredAnnotation(CurrencyParameter.class);
					String name = mmp.value().equals(CurrencyParameter.DEFAULT_VALUE) ? f.getName() : mmp.value();
					parameters.add(Parameter.createCurrencyParameter(name, f.get(this.getObject())));
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new FailToFindParameterException(ERROR_EXCETION_WHEN_PASSING_PARAMETER + f.getDeclaringClass() + "." + f.getName(), e);
			}
		});
		return parameters;
	}
	
	public Object getObject() {
		return this.object;
	}

}
