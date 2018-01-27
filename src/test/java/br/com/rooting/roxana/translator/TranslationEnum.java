package br.com.rooting.roxana.translator;

import static br.com.rooting.roxana.parameter.Parameter.create;
import static br.com.rooting.roxana.parameter.Parameter.createCurrencyParameter;
import static br.com.rooting.roxana.parameter.Parameter.createDateParameter;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import br.com.rooting.roxana.parameter.Parameter;

public enum TranslationEnum {
	SEM_PARAMETROS("hello.world", new Parameter[0], 
						"Olá Mundo.", 
						"Hello World.", 
						"Hallo Welt.", 
						"こんにちは世界。"),
	// TODO Verificar nova api para money Java 9.
	PARAMETRO_MONETARIO("saldo.atual", new Parameter[]{createCurrencyParameter("saldo", 10.12)}, 
						"Meu Saldo é R$ 10,12.", 
						"My balance is $10.12.", 
						"Mein Guthaben beträgt 10,12 €.", 
						"私の残高は￥10。"),
	PARAMETRO_STRING("meu.nome", new Parameter[]{create("nome", "Bruno Costa")}, 
						"Meu nome é Bruno Costa.",
						"My name is Bruno Costa.",
						"Mein Name ist Bruno Costa.",
						"私の名前はBruno Costaです。"),
	// TODO os parametros de data deveriam ser formatado de acordo com a localidade.
	PARAMETRO_DATA("data.nascimento", new Parameter[]{createDateParameter("dataNascimento", LocalDate.of(1992, DECEMBER, 11))}, 
						"Eu nasci em 1992-12-11.",
						"I was born on 1992-12-11.",
						"Ich wurde am 1992-12-11 geboren.",
						"私1992-12-11に生まれました。"),
	PARAMETROS_REPETIDOS("viva.roxana", new Parameter[]{create("roxana", "Roxana")}, 
						"Vida longa a Roxana! Vida longa a Roxana! Vida longa a Roxana!",
						"Long live the Roxana! Long live the Roxana! Long live the Roxana!",
						"Lang lebe der Roxana! Lang lebe der Roxana! Lang lebe der Roxana!",
						"長い人生 Roxana！ 長い人生 Roxana！ 長い人生 Roxana！"),
	PARAMETROS_STRING_DATA_MONETARIO("consulta.saldo", new Parameter[]{create("nome", "Bruno Costa"), 
						createDateParameter("dataAgora", LocalDate.of(2018, JANUARY, 27)),
						createCurrencyParameter("saldo", 3860.80)},
						"Olá senhor Bruno Costa. Seu saldo é de R$ 3.860,80. Consulta realizada em 2018-01-27.",
						"Hello Mr. Bruno Costa. His balance is $3,860.80. Consultation held on 2018-01-27.",
						"Hallo Herr Bruno Costa. Sein Guthaben beträgt 3.860,80 €. Die Konsultation fand am 2018-01-27 statt.",
						"こんにちはミスター Bruno Costa。 彼の残高は￥3,861。相談は2018-01-27で行われました。");
	
	private final String key;
	private final Parameter[] parameters;
	private final String ptBR, enUS, deDE, jaJP;
	
	private TranslationEnum(final String key, 
							final Parameter[] parameters, 
							final String ptBR, 
							final String enUS,
							final String deDE, 
							final String jaJP) {
		this.key = key;
		this.parameters = parameters;
		this.ptBR = ptBR;
		this.enUS = enUS;
		this.deDE = deDE;
		this.jaJP = jaJP;
	}

	public String getKey() {
		return this.key;
	}
	
	public Parameter[] getParameters() {
		return this.parameters;
	}
	
	public List<Parameter> getParametersAsList() {
		return Arrays.asList(this.getParameters());
	}
	
	public String getTranslation(final LocaleTagEnum locale) {
		switch(locale) {
		case PT_BR 	: return this.getPtBR();
		case EN_US 	: return this.getEnUS();
		case DE_DE 	: return this.getDeDE();
		case JA_JP 	: return this.getJaJP();
		default 	: throw new IllegalArgumentException();
		}
	}

	public String getPtBR() {
		return ptBR;
	}

	public String getEnUS() {
		return enUS;
	}

	public String getDeDE() {
		return deDE;
	}

	public String getJaJP() {
		return jaJP;
	}
	
}