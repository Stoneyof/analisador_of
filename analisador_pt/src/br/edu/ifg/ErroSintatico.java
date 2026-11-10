package br.edu.ifg;

// Erro do analisador sintático (tokens fora da ordem da gramática).
public class ErroSintatico extends ErroDeAnalise {

    public ErroSintatico(int linha, int coluna, String detalhe) {
        super("sintático", linha, coluna, detalhe);
    }
}
