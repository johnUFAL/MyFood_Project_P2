package br.ufal.ic.myfood.models;

public abstract class Empresa {
    private int id;
    private int dono;
    private String nome;
    private String endereco;

    public Empresa() {}

    public Empresa(int id, int dono,  String nome, String endereco) {
        this.id = id;
        this.dono = dono;
        this.nome = nome;
        this.endereco = endereco;
    }

    public int getId() {return id;}
    public int getDono() {return dono;}
    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}

    public void setId(int id) {this.id = id;}
    public void setDono(int dono) {this.dono = dono;}
    public void setNome(String nome) {this.nome = nome;}
    public void setEndereco(String endereco) {this.endereco = endereco;}
}
