package br.com.rooting.roxana.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static br.com.rooting.roxana.RoxanaPackageMapper.PROJECT_ROOT_PACKAGE_PATH;
import static br.com.rooting.roxana.RoxanaPackageMapper.SWAGGER_CONFIG_PACKAGE_PATH;

@Configuration
@ComponentScan(basePackages = PROJECT_ROOT_PACKAGE_PATH,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = SWAGGER_CONFIG_PACKAGE_PATH + ".*")
        })
class RoxanaConfiguration {

}