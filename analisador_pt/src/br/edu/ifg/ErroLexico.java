package br.edu.ifg;

// Erro do analisador léxico (caractere que a linguagem não conhece).
public class ErroLexico extends ErroDeAnalise {

    public ErroLexico(int linha, int coluna, String detalhe) {
        super("léxico", linha, coluna, detalhe);
    }
}
