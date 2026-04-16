package br.ufal.ic.myfood.exceptions.Users;

public class AtributoNaoExiste extends Exception{
    public AtributoNaoExiste() {
        super("Atributo nao existe");
    }

    public AtributoNaoExiste (String message) {super(message);}


}
