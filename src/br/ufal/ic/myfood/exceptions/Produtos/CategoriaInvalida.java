package br.ufal.ic.myfood.exceptions.Produtos;

public class CategoriaInvalida extends Exception{
    public CategoriaInvalida() {
        super("Categoria invalido");
    }
    public CategoriaInvalida(String message) {super(message);}
}
