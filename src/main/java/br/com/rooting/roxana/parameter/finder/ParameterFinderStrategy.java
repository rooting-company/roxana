package br.com.rooting.roxana.parameter.finder;

import br.com.rooting.roxana.parameter.Parameter;

import java.util.List;

public interface ParameterFinderStrategy {

    List<Parameter> findParameters();

}