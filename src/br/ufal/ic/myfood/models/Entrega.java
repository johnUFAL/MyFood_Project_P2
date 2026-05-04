package br.ufal.ic.myfood.models;

import java.util.ArrayList;
import java.util.List;

public class Entrega {
    private int id;
    private String cliente;
    private String empresa;
    private int pedido;
    private int entregador;
    private String destino;
    private List<String> produtos;

    public Entrega() {}

    public Entrega(int id, String cliente, String empresa, int pedido, int entregador, String destino) {
        this.id = id;
        this.cliente = cliente;
        this.empresa = empresa;
        this.pedido = pedido;
        this.entregador = entregador;
        this.destino = destino;
        this.produtos = new ArrayList<>();
    }

    public int getId() {return id;}
    public String getCliente() {return cliente;}
    public String getEmpresa() {return empresa;}
    public int getPedido() {return pedido;}
    public int getEntregador() {return entregador;}
    public String getDestino() {return destino;}
    public List<String> getProdutos() {return produtos;}

    public void setId(int id) {this.id = id;}
    public void setCliente(String cliente) {this.cliente = cliente;}
    public void setEmpresa(String empresa) {this.empresa = empresa;}
    public void setPedido(int pedido) {this.pedido = pedido;}
    public void setEntregador(int entregador) {this.entregador = entregador;}
    public void setDestino(String destino) {this.destino = destino;}
    public void setProdutos(List<String> produtos) {this.produtos = produtos;}
}
