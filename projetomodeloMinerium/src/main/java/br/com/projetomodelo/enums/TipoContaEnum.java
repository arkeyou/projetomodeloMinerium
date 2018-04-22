package br.com.projetomodelo.enums;

import java.util.Arrays;

public enum TipoContaEnum {
	GRATIS("LOGADO"), 
	PAGO("LOGADO","RESTRITO"), 
	REDATOR("LOGADO","RESTRITO","REDATOR"),
	ADMIN("LOGADO","RESTRITO","REDATOR","ADMIN");
	
	private String[] papeis;
	
	private TipoContaEnum(String... papeis) {
		this.papeis = papeis;
	}

	public Boolean possuiAutorizacao(String papel) {
		return Arrays.asList(papeis).contains(papel);
	}

	public String[] getPapeis() {
		return this.papeis;
	}
}
