package br.ufal.ic.myfood.models;

public class Produto {
    private int id;
    private int empresa;
    private String nome;
    private float valor;
    private String categoria;

    public Produto() {}

    public Produto(int id, int empresa, String nome, float valor, String categoria) {
        this.id = id;
        this.empresa = empresa;
        this.nome = nome;
        this.valor = valor;
        this.categoria = categoria;
    }

    public int getId() {return id;}
    public  int getEmpresa() {return empresa;}
    public String getNome() {return nome;}
    public float getValor() {return valor;}
    public String getCategoria() {return categoria;}

    public void setId(int id) {this.id = id;}
    public void setEmpresa(int empresa) {this.empresa = empresa;}
    public void setNome(String nome) {this.nome = nome;}
    public void setValor(float valor) {this.valor = valor;}
    public void setCategoria(String categoria) {this.categoria = categoria;}
}
