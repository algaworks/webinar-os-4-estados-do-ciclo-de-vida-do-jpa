package com.algaworks.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.algaworks.model.Cliente;
import com.algaworks.model.Pagamento;
import com.algaworks.model.Pedido;
import com.algaworks.model.Status;

@Named
@ViewScoped
public class PedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Pedido> listaPedidos;
	
	private BigDecimal valorPagamento;
	
	private Pedido pedidoSelecionado;
	
	@Inject
	private EntityManager manager;
	
	public void prepararPedidos() {
		/*Cliente cliente = new Cliente();
		cliente.setNome("João Silva");
		
		Pedido pedido1 = new Pedido();
		pedido1.setCodigo(1L);
		pedido1.setCliente(cliente);
		pedido1.setDataEmissao(new Date());
		pedido1.setStatus(Status.PENDENTE);
		pedido1.setValorTotal(new BigDecimal("400"));
		
		Pedido pedido2 = new Pedido();
		pedido2.setCodigo(2L);
		pedido2.setCliente(cliente);
		pedido2.setDataEmissao(new Date());
		pedido2.setStatus(Status.PENDENTE);
		pedido2.setValorTotal(new BigDecimal("200"));
		
		listaPedidos = Arrays.asList(pedido1, pedido2); */
		
		listaPedidos = manager.createQuery("from Pedido", Pedido.class).getResultList();
	}
	
	public List<Pedido> getListaPedidos() {
		return listaPedidos;
	}
	
	public void adicionarPagamento() {
		manager.getTransaction().begin();
		
		Pedido pedidoAPagar = manager.find(Pedido.class, this.pedidoSelecionado.getCodigo());
		
		Pagamento novoPagamento = new Pagamento();
		novoPagamento.setPedido(pedidoAPagar);
		novoPagamento.setValor(this.valorPagamento);
		
		manager.persist(novoPagamento);
		
		BigDecimal valorPago = pedidoAPagar.getValorPago();
		
		if (valorPago.compareTo(pedidoAPagar.getValorTotal()) >= 0) {
			pedidoAPagar.setStatus(Status.PAGO);
			System.out.println("Cumpriu o objetivo. Atualizando no banco de dados...");
			//manager.merge(pedidoAPagar);
		}
		
		manager.getTransaction().commit();
		this.prepararPedidos();
	}

	public BigDecimal getValorPagamento() {
		return valorPagamento;
	}

	public void setValorPagamento(BigDecimal valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public Pedido getPedidoSelecionado() {
		return pedidoSelecionado;
	}

	public void setPedidoSelecionado(Pedido pedidoSelecionado) {
		this.pedidoSelecionado = pedidoSelecionado;
	}
	
}
