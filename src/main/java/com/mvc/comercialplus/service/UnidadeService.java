package com.mvc.comercialplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.comercialplus.model.Unidade;
import com.mvc.comercialplus.repository.UnidadeRepository;

@Service
public class UnidadeService implements BdService<Unidade> {

	private UnidadeRepository repo;
	
	@Autowired
	public UnidadeService(UnidadeRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public List<Unidade> getAll() {
		return repo.findAll();
	}

	@Override
	public Unidade getById(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Unidade save(Unidade objeto) {
		return repo.save(objeto);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
		
	}

}
