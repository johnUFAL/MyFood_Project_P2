package br.ufal.ic.myfood.exceptions.Empresas;

public class IndiceInvalido extends Exception{
    public IndiceInvalido() {
        super("Indice invalido");
    }
    public IndiceInvalido(String message) {super(message);}
}
