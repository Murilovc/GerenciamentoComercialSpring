package com.mvc.comercialplus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mvc.comercialplus.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{
	
//	@Query(
//		"SELECT c FROM Cliente c WHERE c.nome LIKE %?1% " +
//		"OR c.email LIKE %?1%"
//	)
//	List<Cliente> findByTermo(String termoBusca);
}
