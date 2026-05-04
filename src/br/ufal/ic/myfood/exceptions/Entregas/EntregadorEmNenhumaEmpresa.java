package br.ufal.ic.myfood.exceptions.Entregas;

public class EntregadorEmNenhumaEmpresa extends Exception {
    public EntregadorEmNenhumaEmpresa() {
        super("Entregador nao estar em nenhuma empresa.");
    }
}
