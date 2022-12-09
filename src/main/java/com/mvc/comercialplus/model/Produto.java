package com.mvc.comercialplus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=true)
	private Categoria categoria;
	
	@Column(nullable=false)
	private BigDecimal preco;
	
	@Column(nullable=true)
	private BigDecimal desconto;
	
	@Column(name="codigo_barras", nullable=false)
	private String codigoBarras;

	
	//getters e setters
	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Categoria getCategoria() {
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

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoria, codigoBarras, desconto, id, nome, preco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		return categoria == other.categoria && Objects.equals(codigoBarras, other.codigoBarras)
				&& Objects.equals(desconto, other.desconto) && Objects.equals(id, other.id)
				&& Objects.equals(nome, other.nome) && Objects.equals(preco, other.preco);
	}
	
	
	
}
