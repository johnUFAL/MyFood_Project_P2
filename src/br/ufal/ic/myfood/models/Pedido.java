package br.ufal.ic.myfood.models;
import java.util.List;

public class Pedido {
    private int numero;
    private String cliente;
    private String empresa;
    private String estado;
    private List<String> produtos;
    private float valor;

    public Pedido() {}

    public Pedido(int numero, String cliente, String empresa, String estado, List<String> produtos, float valor) {
        this.numero = numero;
        this.cliente = cliente;
        this.empresa = empresa;
        this.estado = estado;
        this.produtos = produtos;
        this.valor = valor;
    }

    public int getNumero() { return numero; }
    public String getCliente() { return cliente; }
    public String getEmpresa() { return empresa; }
    public String getEstado() { return estado; }
    public List<String> getProdutos() { return produtos; }
    public float getValor() { return valor; }

    public void setNumero(int numero) { this.numero = numero; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setProdutos(List<String> produtos) { this.produtos = produtos; }
    public void setValor(float valor) { this.valor = valor; }
}