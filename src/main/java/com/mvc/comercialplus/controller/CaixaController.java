package com.mvc.comercialplus.controller;

import java.awt.Desktop;
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
		
		var taxaEmDecimal = BigDecimal.valueOf(tipoCartao.taxaPorcentagem()).setScale(3, RoundingMode.HALF_EVEN);
		var totalComTaxa = valorProdutos.multiply(taxaEmDecimal).setScale(3, RoundingMode.HALF_EVEN);
		
		return totalComTaxa.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public static BigDecimal aplicarTaxaParcelamentoCartao(BigDecimal valorProdutos, double taxaParcelamento) {
		var taxaEmDecimal = BigDecimal.valueOf(taxaParcelamento).setScale(4,RoundingMode.HALF_EVEN);
		var totalComTaxaParcelamento = valorProdutos.multiply(taxaEmDecimal).setScale(4,RoundingMode.HALF_EVEN);
		
		return totalComTaxaParcelamento.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public static void imprimir() {
		var modeloPraImprimir = 
		"""
				COMERCIAL LOPES
				R STA MARIA, 504
				JOÃO EDUARDO I, RB/AC
			=========================================
						CUPOM NÃO FISCAL
			-----------------------------------------
			COD		DESCRIÇÃO			QTD.	VALOR
			1		VENENO PRETO		3		12,70
			2		COTONETE			2		 7,89
			6		TALCO				4		14,00
			
			-----------------------------------------
			TOTAL PRODUTOS				R$ 34,59     
			TAXAS PAGAMENTO				R$  4,59     
			DESCONTO                    R$  0,00     
			TOTAL						R$ 39,18     
			-----------------------------------------
			
					OBRIGADO E VOLTE SEMPRE!         
				""";
		
	}
}
