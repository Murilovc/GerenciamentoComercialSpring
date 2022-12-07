package com.mvc.comercialplus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvc.comercialplus.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	Produto findByCodigoBarras(String codBarras);
}
