package com.mvc.comercialplus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mvc.comercialplus.model.Cliente;
import com.mvc.comercialplus.repository.ClienteRepository;

public class ClienteService implements BdService<Cliente>{

	ClienteRepository repo;
	
	@Autowired
	ClienteService(ClienteRepository repo){
		this.repo = repo;
	}
	
	@Override
	public List<Cliente> getAll() {
		return repo.findAll();
	}

	@Override
	public Cliente getById(Long id) {
		return repo.findById(id).orElse(null);
	}

//	public List<Cliente> getByTermo(String termoBusca) {
//		return repo.findByTermo(termoBusca);
//	}

	@Override
	public Cliente save(Cliente objeto) {
		return repo.save(objeto);
	}

	@Override
	public void delete(Long id) {
		repo.deleteById(id);
		
	}

}
