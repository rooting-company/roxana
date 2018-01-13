package br.com.rooting.roxana.response.parameterFinder;

import java.util.List;

import br.com.rooting.roxana.parameter.Parameter;

public interface ParameterFinderStrategy {
	
	public List<Parameter> findParameters();
	
}