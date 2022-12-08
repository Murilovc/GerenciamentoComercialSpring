package com.mvc.comercialplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.comercialplus.model.Estoque;
import com.mvc.comercialplus.repository.EstoqueRepository;

@Service
public class EstoqueService implements BdService<Estoque>{

	private EstoqueRepository repo;
	
	@Autowired
	public EstoqueService(EstoqueRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public List<Estoque> getAll() {
		return repo.findAll();
	}

	@Override
	public Estoque getById(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Estoque save(Estoque objeto) {
		return repo.save(objeto);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
	}

}
