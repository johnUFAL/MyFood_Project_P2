package br.ufal.ic.myfood.exceptions.Empresas;

public class IndiceMaiorQueEsperado extends Exception{
    public IndiceMaiorQueEsperado() {
        super("Indice maior que o esperado");
    }
    public IndiceMaiorQueEsperado(String message) {super(message);}
}
