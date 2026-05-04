package br.ufal.ic.myfood.exceptions.Entregas;

public class EntregadroEmEntrega extends Exception{
    public EntregadroEmEntrega() {
        super("Entregador ainda em entrega");
    }
}
