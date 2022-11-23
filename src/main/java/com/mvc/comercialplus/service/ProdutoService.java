package com.mvc.comercialplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mvc.comercialplus.model.Produto;
import com.mvc.comercialplus.repository.ProdutoRepository;

public class ProdutoService implements BdService<Produto>{

	private ProdutoRepository repo;
	
	@Autowired
	public ProdutoService(ProdutoRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public List<Produto> getAll() {
		return repo.findAll();
	}

	@Override
	public Produto getById(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Produto save(Produto objeto) {
		return repo.save(objeto);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
	}

}
