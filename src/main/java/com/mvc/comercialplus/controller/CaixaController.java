package com.mvc.comercialplus.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.mvc.comercialplus.config.Configuracao;
import com.mvc.comercialplus.model.FormaPagamento;

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
	
	public static BigDecimal aplicarTaxaCartao(BigDecimal valorProdutos, FormaPagamento tipoCartao) {
		
		var taxaEmDecimal = BigDecimal.valueOf(tipoCartao.taxaPorcentagem()).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println(taxaEmDecimal);
		var totalComTaxa = valorProdutos.multiply(taxaEmDecimal).setScale(2, RoundingMode.HALF_EVEN);
		System.out.println(totalComTaxa);
		
		return totalComTaxa;
	}
}
