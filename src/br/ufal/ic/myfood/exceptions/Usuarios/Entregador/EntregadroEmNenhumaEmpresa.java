package br.ufal.ic.myfood.exceptions.Usuarios.Entregador;

public class EntregadroEmNenhumaEmpresa extends Exception{
    public EntregadroEmNenhumaEmpresa() {
        super("Entregador nao estar em nenhuma empresa.");
    }
}
