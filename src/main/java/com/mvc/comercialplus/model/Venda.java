package com.mvc.comercialplus.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Venda implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Instant data;
	
	@ManyToOne
	@JoinColumn
	private Cliente cliente;
	
	@Column(nullable=true)
	private BigDecimal valorTotal;
	
	@Enumerated(EnumType.STRING)
	private StatusVenda status;
	
	@Enumerated(EnumType.STRING)
	private FormaPagamento pagamento;

	//Uma lista de itens aqui
	
	
	//getters e setters
	public Long getId() {
		return id;
	}

	public Instant getData() {
		return data;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public StatusVenda getStatus() {
		return status;
	}

	public FormaPagamento getPagamento() {
		return pagamento;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setData(Instant data) {
		this.data = data;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setStatus(StatusVenda status) {
		this.status = status;
	}

	public void setPagamento(FormaPagamento pagamento) {
		this.pagamento = pagamento;
	}
	
}
