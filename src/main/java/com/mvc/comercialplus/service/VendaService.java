package com.mvc.comercialplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.comercialplus.model.Venda;
import com.mvc.comercialplus.repository.VendaRepository;

@Service
public class VendaService implements BdService<Venda> {

	private VendaRepository repo;
	
	@Autowired
	public VendaService(VendaRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public List<Venda> getAll() {
		return repo.findAll();
	}

	@Override
	public Venda getById(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Venda save(Venda objeto) {
		return repo.save(objeto);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
	}

}
