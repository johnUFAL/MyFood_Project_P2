package br.ufal.ic.myfood.exceptions.Empresas;

public class NomeInvalido extends Exception{
    public NomeInvalido() {
        super("Nome invalido");
    }
    public NomeInvalido(String message) {super(message);}
}
