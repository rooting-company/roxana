package br.com.rooting.roxana.config;

import static br.com.rooting.roxana.RoxanaPackageMapper.PROJECT_ROOT_PACKAGE_PATH;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(PROJECT_ROOT_PACKAGE_PATH)
class RoxanaConfiguration {

}