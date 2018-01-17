package br.com.rooting.roxana.config;

import static br.com.rooting.roxana.config.RoxanaPackageMapper.PROJECT_ROOT_PACKAGE_PATH;
import static br.com.rooting.roxana.config.RoxanaPackageMapper.SWAGGER_CONFIG_PACKAGE_PATH;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = PROJECT_ROOT_PACKAGE_PATH, 
			   excludeFilters = {
					   				@ComponentScan.Filter(type = FilterType.REGEX, pattern = SWAGGER_CONFIG_PACKAGE_PATH + ".*")
					   			})
class RoxanaConfiguration {

}