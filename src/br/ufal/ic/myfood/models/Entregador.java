package br.ufal.ic.myfood.models;

import java.util.ArrayList;
import java.util.List;

public class Entregador extends Usuario{
    private String veiculo;
    private String placa;
    private List<Integer> empresas;

    public Entregador() {}

    public Entregador(int id, String nome, String email, String senha, String endereco, String veiculo, String placa) {
        super(id, nome, email, senha, endereco);
        this.veiculo = veiculo;
        this.placa = placa;
        this.empresas = new ArrayList<>();
    }

    public String getVeiculo() {return veiculo;}
    public String getPlaca() {return placa;}
    public List<Integer> getEmpresas() {return empresas;}

    public void setVeiculo(String veiculo) {this.veiculo = veiculo;}
    public void setPlaca(String placa) {this.placa = placa;}
    public void setEmpresas(List<Integer> empresas) {this.empresas =  empresas;}

}
