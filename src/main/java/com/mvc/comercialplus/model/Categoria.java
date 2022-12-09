package com.mvc.comercialplus.model;

public enum Categoria {
	ALIMENTACAO,
	LIMPEZA,
	FRIOS,
	VERDURAS,
	OUTROS;
	
	public String getArquivoCorrespondente() {
		switch(this) {
			case ALIMENTACAO:
				return "alimentacao.png";
			case LIMPEZA:
				return "limpeza.png";
			case FRIOS:
				return "frios.png";
			case VERDURAS:
				return "verduras.png";
			case OUTROS:
				return "outros.png";
			default:
				return "Erro";
		}
	}
}
