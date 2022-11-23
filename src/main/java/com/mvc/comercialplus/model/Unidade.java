package com.mvc.comercialplus.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Unidade implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional=false)
	private Venda venda;
	
	@ManyToOne(optional=false)
	private Produto produto;

	//getters e setters
	public Long getId() {
		return id;
	}

	public Venda getVenda() {
		return venda;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
