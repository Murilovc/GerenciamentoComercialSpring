package com.mvc.comercialplus.controller;

import java.math.BigDecimal;

import com.mvc.comercialplus.config.Configuracao;

public class CaixaController {
	public static BigDecimal converterTextoEmMonetario(String textoCampo) {
		var semPrefixo = textoCampo.replace(Configuracao.PREFIXO_MONETARIO.get(), "");		
		var comPonto  = semPrefixo.replace(',', '.');
		return new BigDecimal(comPonto);
	}
	
	public static String converterMonetarioEmTexto(BigDecimal decimal) {
		var prefixo = Configuracao.PREFIXO_MONETARIO.get();
		var textoComVirgula = decimal.toPlainString().replace('.', ',');
		
		return prefixo+textoComVirgula;
	}
}
