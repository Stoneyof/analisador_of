package br.edu.ifg;

// Herança: superclasse dos erros do analisador léxico e do sintático.
public class ErroDeAnalise extends RuntimeException {

    public ErroDeAnalise(String categoria, int linha, int coluna, String detalhe) {
        super("Erro " + categoria + " na linha " + linha + ", coluna " + coluna + ": " + detalhe);
    }
}
