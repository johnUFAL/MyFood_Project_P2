package br.ufal.ic.myfood.exceptions.Empresas;

public class UsuarioNaoCriaEmpresa extends Exception{
    public UsuarioNaoCriaEmpresa() {
        super("Usuario nao pode criar uma empresa");
    }
}
