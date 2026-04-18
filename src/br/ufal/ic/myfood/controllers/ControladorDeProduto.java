package br.ufal.ic.myfood.controllers;
import br.ufal.ic.myfood.exceptions.Produtos.*;
import br.ufal.ic.myfood.models.Produto;

import java.util.Map;
import java.util.LinkedHashMap;

public class ControladorDeProduto {
    private  Map<Integer, Produto> produto;
    private int proximoId;

    public ControladorDeProduto() {
        this.produto = new LinkedHashMap<>();
        this.proximoId = 1;
    }

    public Map<Integer, Produto> getProduto() {return produto;}
    public void setProduto(Map<Integer, Produto> produto) {this.produto = produto;}

    public int getProximoId() {return proximoId;}
    public void setProximoId(int proximoId) {this.proximoId = proximoId;}

    public void zerar() {
        this.produto.clear();
        this.proximoId = 1;
    }

    public int gerarId() {return this.proximoId++;}

    public int criarProduto(int empresa, String nome, float valor, String categoria) throws Exception {
        validarDadosBase(nome, valor, categoria);

        for (Produto p : this.produto.values()) {
            if (p.getEmpresa() == empresa && p.getNome().equals(nome)) {
                throw new ProibidoMesmoNomeLocal();
            }
        }

        int id = gerarId();
        Produto novoProduto = new Produto(id, empresa, nome, valor, categoria);
        this.produto.put(id, novoProduto);

        return id;
    }

    public void editarProduto(int produto, String nome, float valor, String categoria) throws Exception {
        if (!this.produto.containsKey(produto)) {
            throw new ProdutoNaoCadastrado();
        }

        validarDadosBase(nome, valor, categoria);

        Produto p = this.produto.get(produto);
        p.setNome(nome);
        p.setValor(valor);
        p.setCategoria(categoria);
    }

    public Produto buscarProdutoPorNomeEEmpresa(String nome, int empresa) throws Exception {
        for (Produto p : this.produto.values()) {
            if (p.getEmpresa() == empresa && p.getNome().equals(nome)) {
                return p;
            }
        }
        throw new ProdutoNaoEncontrado();
    }

    public String listarProdutos(int empresa) {
        StringBuilder resultado = new StringBuilder("{[");
        boolean primeiro = true;

        for (Produto p : this.produto.values()) {
            if (p.getEmpresa() == empresa) {
                if (!primeiro) {
                    resultado.append(", ");
                }
                resultado.append(p.getNome());
                primeiro = false;
            }
        }

        resultado.append("]}");
        return resultado.toString();
    }

    private void validarDadosBase(String nome, float valor, String categoria) throws Exception {
        if (nome == null || nome.trim().isEmpty()) throw new NomeInvalido();
        if (valor < 0) throw new ValorInvalido();
        if (categoria == null || categoria.trim().isEmpty()) throw new CategoriaInvalida();
    }
}
