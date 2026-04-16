package br.ufal.ic.myfood.exceptions.Empresas;

public class AtributoInvalido extends Exception{
    public AtributoInvalido() {
        super("Atributo invalido");
    }
    public AtributoInvalido(String message) {super(message);}
}
