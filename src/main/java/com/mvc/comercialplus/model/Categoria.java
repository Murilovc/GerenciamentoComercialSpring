package com.mvc.comercialplus.model;

public enum Categoria {
	ALIMENTACAO,
	LIMPEZA,
	FRIOS,
	VERDURAS,
	HIGIENE,
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
			case HIGIENE:
				return "higiene.png";
			case OUTROS:
				return "outros.png";
			default:
				return "Erro";
		}
	}
}
