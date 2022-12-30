package com.mvc.comercialplus.model;

public enum FormaPagamento {

	FIADO,
	DINHEIRO,
	PIX,
	QRCODE_PIX,
	CARTAO_DEBITO,
	CARTAO_VALE,
	CARTAO_CREDITO;
	
	public double taxaPorcentagem() {
		switch(this) {
			case FIADO:
				return 1.0;
			case DINHEIRO:
				return 1.0;
			case PIX:
				return 1.0;
			case QRCODE_PIX:
				return 1.0;
			case CARTAO_DEBITO:
				return 1.0325;
			case CARTAO_VALE:
				return 1.05;
			case CARTAO_CREDITO:
				return 1.045;
			default:
				return 1.0;
		}
	}
	
     public FormaPagamento next() {
    	 FormaPagamento forma = this;
         int index = ordinal() + 1;
         if (index < values().length) {
        	 forma = values()[index];
         }
         return forma;
     }
	
}
