package br.ufal.ic.myfood.exceptions.Produtos;

public class EmpresaNaoEncontrada extends Exception{
    public EmpresaNaoEncontrada() {
        super("Empresa nao encontrada");
    }
    public EmpresaNaoEncontrada(String message) {super(message);}
}
