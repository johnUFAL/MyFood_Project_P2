package br.ufal.ic.myfood.exceptions.Entregas;

public class ProdutoNaoEstaSendoPreparado extends Exception{
    public ProdutoNaoEstaSendoPreparado() {
        super("Nao e possivel liberar um produto que nao esta sendo preparado");
    }
}
