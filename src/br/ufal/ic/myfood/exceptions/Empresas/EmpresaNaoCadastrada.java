package br.ufal.ic.myfood.exceptions.Empresas;

public class EmpresaNaoCadastrada extends Exception{
    public EmpresaNaoCadastrada() {
        super("Empresa nao cadastrada");
    }
}
