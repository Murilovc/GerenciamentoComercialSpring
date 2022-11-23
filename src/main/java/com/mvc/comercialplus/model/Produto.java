package com.mvc.comercialplus.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Produto implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false) 
	private String nome;
	
	@Column(nullable=false)
	private String categoria;
	
	@Column(nullable=false)
	private BigDecimal preco;
	
	@Column(name="codigo_barras", nullable=false)
	private String codigoBarras;

	
	//getters e setters
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getCategoria() {
		return categoria;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
}
