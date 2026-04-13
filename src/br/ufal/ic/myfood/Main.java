package br.ufal.ic.myfood;

import easyaccept.EasyAccept;

public class Main {
    public static void main(String[] args) {
        String[]  argsEasyAccept = new String[] {
                "br.ufal.ic.myfood.Facade",
                "tests/"
        };

        EasyAccept.main(argsEasyAccept);
    }
}