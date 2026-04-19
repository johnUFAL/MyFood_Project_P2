package br.ufal.ic.myfood.controllers;

import br.ufal.ic.myfood.exceptions.Pedidos.*;
import br.ufal.ic.myfood.models.Pedido;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class ControladorDePedidos {
    private Map<Integer, Pedido> pedido;
    private int proximoNumero;

    public ControladorDePedidos() {
        this.pedido = new LinkedHashMap<>();
        this.proximoNumero = 1;
    }

    public Map<Integer, Pedido> getPedido() { return pedido; }
    public void setPedido(Map<Integer, Pedido> pedido) { this.pedido = pedido; }

    public int getProximoNumero() { return proximoNumero; }
    public void setProximoNumero(int proximoNumero) { this.proximoNumero = proximoNumero; }

    public void zerar() {
        this.pedido.clear();
        this.proximoNumero = 1;
    }

    public int gerarNumero() { return this.proximoNumero++; }

    public int criarPedido(String nomeCliente, String nomeEmpresa) throws Exception {
        for (Pedido p : this.pedido.values()) {
            if (p.getCliente().equals(nomeCliente) && p.getEmpresa().equals(nomeEmpresa) && p.getEstado().equals("aberto")) {
                throw new DoisPedidosAbertos();
            }
        }

        int numero = gerarNumero();
        Pedido novoPedido = new Pedido(numero, nomeCliente, nomeEmpresa, "aberto", new ArrayList<>(), 0.0f);
        this.pedido.put(numero, novoPedido);
        return numero;
    }

    public int getNumeroPedido(String nomeCliente, String nomeEmpresa, int indice) throws Exception {
        int cont = 0;
        for (Pedido p : this.pedido.values()) {
            if (p.getCliente().equals(nomeCliente) && p.getEmpresa().equals(nomeEmpresa)) {
                if (cont == indice) {
                    return p.getNumero();
                }
                cont++;
            }
        }
        throw new PedidoNaoEncontrado();
    }

    public void adicionarProduto(int numero, String nomeEmpresaDoProduto, String nomeProduto, float valorProduto) throws Exception {
        if (!this.pedido.containsKey(numero)) {
            throw new NaoPedidoAberto();
        }

        Pedido p = this.pedido.get(numero);

        if (!p.getEstado().equals("aberto")) {
            throw new PedidoFechado();
        }

        if (!p.getEmpresa().equals(nomeEmpresaDoProduto)) {
            throw new ProdutoNaoPertenceEmpresa();
        }

        p.getProdutos().add(nomeProduto);
        p.setValor(p.getValor() + valorProduto);
    }

    public void fecharPedido(int numero) throws Exception {
        if (!this.pedido.containsKey(numero)) throw new PedidoNaoEncontrado();
        this.pedido.get(numero).setEstado("preparando");
    }

    public void removerProduto(int numero, String nomeProduto, float valorProduto) throws Exception {
        if (!this.pedido.containsKey(numero)) throw new PedidoNaoEncontrado();

        Pedido p = this.pedido.get(numero);

        if (!p.getEstado().equals("aberto")) {
            throw new RemoverPedidoFechado();
        }

        if (!p.getProdutos().contains(nomeProduto)) {
            throw new ProdutoNaoEncontrado();
        }

        p.getProdutos().remove(nomeProduto);
        p.setValor(p.getValor() - valorProduto);
    }

    public Pedido buscarPedidoPorId(int numero) throws Exception {
        if (!this.pedido.containsKey(numero)) throw new PedidoNaoEncontrado();
        return this.pedido.get(numero);
    }
}