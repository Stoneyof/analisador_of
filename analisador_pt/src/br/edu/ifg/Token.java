package br.edu.ifg;

// Um token: tipo, texto (lexema) e posição no código.
// Encapsulamento: atributos private final, acesso apenas pelos métodos get.
// Para palavras reservadas e símbolos, o tipo é o próprio texto (ex.: "if", "(").
public class Token {

    public static final String IDENTIFICADOR = "identificador";
    public static final String NUMERO = "numero";
    public static final String FIM_ARQUIVO = "fim de arquivo";

    private final String tipo;
    private final String lexema;
    private final int linha;
    private final int coluna;

    public Token(String tipo, String lexema, int linha, int coluna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
