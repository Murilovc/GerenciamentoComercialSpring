package com.mvc.comercialplus.config;

public enum Configuracao {
	PREFIXO_MONETARIO;
	
	public String get() {
		switch(this) {
			case PREFIXO_MONETARIO:
				return "R$ ";//R, seguido de cifrao, terminando com espaco.
			default:
				return "A configuracao exigida nao existe";
		}

	}
}
