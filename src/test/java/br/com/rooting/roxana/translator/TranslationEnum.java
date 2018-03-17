package br.com.rooting.roxana.translator;

import static br.com.rooting.roxana.parameter.Parameter.create;
import static br.com.rooting.roxana.parameter.Parameter.createCurrencyParameter;
import static br.com.rooting.roxana.parameter.Parameter.createDateParameter;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import br.com.rooting.roxana.business.parameter.DateParameter;
import br.com.rooting.roxana.business.parameter.DateStyle;
import br.com.rooting.roxana.parameter.Parameter;

public enum TranslationEnum {
	TRANSLATION_NONE_PARAMS("hello.world", new Parameter[0], 
						"Olá Mundo.", 
						"Hello World.", 
						"Hallo Welt.", 
						"こんにちは世界。"),
	TRANSLATION_CURRENCY_PARAM("saldo.atual", new Parameter[]{createCurrencyParameter("saldo", 10.12)}, 
						"Meu Saldo é R$ 10,12.", 
						"My balance is $10.12.", 
						"Mein Guthaben beträgt 10,12 €.", 
						"私の残高は￥10。"),
	TRANSLATION_STRING_PARAM("meu.nome", new Parameter[]{create("nome", "Bruno Costa")}, 
						"Meu nome é Bruno Costa.",
						"My name is Bruno Costa.",
						"Mein Name ist Bruno Costa.",
						"私の名前はBruno Costaです。"),
	TRANSLATION_DATE_PARAM("data.nascimento", new Parameter[]{
														createDateParameter("dataNascimento", 
																			LocalDate.of(1992, DECEMBER, 11), 
																			DateStyle.SHORT,
																			false,
																			DateParameter.NONE_PATTERN)
													 }, 
						"Eu nasci em 11/12/92.",
						"I was born on 12/11/92.",
						"Ich wurde am 11.12.92 geboren.",
						"私92/12/11に生まれました。"),
	TRANSLATION_REPEATED_PARAMS("viva.roxana", new Parameter[]{create("roxana", "Roxana")}, 
						"Vida longa a Roxana! Vida longa a Roxana! Vida longa a Roxana!",
						"Long live the Roxana! Long live the Roxana! Long live the Roxana!",
						"Lang lebe der Roxana! Lang lebe der Roxana! Lang lebe der Roxana!",
						"長い人生 Roxana！ 長い人生 Roxana！ 長い人生 Roxana！"),
	TRANSLATION_STRING_DATE_CURRENCY_PARAMS("consulta.saldo", new Parameter[]{
																			create("nome", "Bruno Costa"), 
																			createDateParameter("dataAgora", 
																								LocalDate.of(2018, JANUARY, 27), 
																								DateStyle.SHORT,
																								false,
																								DateParameter.NONE_PATTERN),
																			createCurrencyParameter("saldo", 3860.80)
																		},
						"Olá senhor Bruno Costa. Seu saldo é de R$ 3.860,80. Consulta realizada em 27/01/18.",
						"Hello Mr. Bruno Costa. His balance is $3,860.80. Consultation held on 1/27/18.",
						"Hallo Herr Bruno Costa. Sein Guthaben beträgt 3.860,80 €. Die Konsultation fand am 27.01.18 statt.",
						"こんにちはミスター Bruno Costa。 彼の残高は￥3,861。相談は18/01/27で行われました。");
	
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
	
	public List<Parameter> getParameters() {
		return Arrays.asList(this.parameters);
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