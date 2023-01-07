package com.mvc.comercialplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.mvc.comercialplus.model.Produto;
import com.mvc.comercialplus.service.ProdutoService;

@Controller
public class ProdutoController {
	
	ProdutoService produtoService;
	
	@Autowired
	public ProdutoController(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}
	
	public boolean validarEAN13(String s) {
		return produtoService.validarEAN13(s);
	}
	
	public Produto getByCodigoBarras(String s) {
		return produtoService.getByCodigoBarras(s);
	}
}
