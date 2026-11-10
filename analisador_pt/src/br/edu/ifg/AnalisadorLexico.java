package br.edu.ifg;

import java.util.ArrayList;
import java.util.List;

// Analisador léxico: transforma o texto do código em uma lista de tokens.
public class AnalisadorLexico {

    private final String[] palavrasReservadas = {"inicio", "fim", "int", "double", "if", "else", "while"};
    private final String[] operadoresDuplos = {"==", "!=", "<=", ">="};

    private final String codigo;
    private int posicao = 0;
    private int linha = 1;
    private int coluna = 1;

    public AnalisadorLexico(String codigo) {
        this.codigo = codigo;
    }

    public List<Token> tokenizar() {
        List<Token> tokens = new ArrayList<>();
        ignorarEspacos();
        while (posicao < codigo.length()) {
            tokens.add(proximoToken());
            ignorarEspacos();
        }
        tokens.add(new Token(Token.FIM_ARQUIVO, "", linha, coluna));
        return tokens;
    }

    // Caractere na posição atual ('\0' se o texto acabou).
    private char caractereAtual() {
        if (posicao < codigo.length()) {
            return codigo.charAt(posicao);
        }
        return '\0';
    }

    // Caractere logo depois do atual, sem avançar.
    private char caractereSeguinte() {
        if (posicao + 1 < codigo.length()) {
            return codigo.charAt(posicao + 1);
        }
        return '\0';
    }

    // Consome um caractere e atualiza linha e coluna.
    private void avancar() {
        if (caractereAtual() == '\n') {
            linha++;
            coluna = 1;
        } else {
            coluna++;
        }
        posicao++;
    }

    private boolean ehEspaco(char c) {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    private boolean ehLetra(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean ehDigito(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean ehSimbolo(char c) {
        return c == '(' || c == ')' || c == '{' || c == '}' || c == ';' || c == '='
                || c == '+' || c == '-' || c == '*' || c == '/' || c == '<' || c == '>';
    }

    private void ignorarEspacos() {
        while (ehEspaco(caractereAtual())) {
            avancar();
        }
    }

    private Token proximoToken() {
        int linhaInicial = linha;
        int colunaInicial = coluna;
        char c = caractereAtual();

        // identificador ou palavra reservada
        if (ehLetra(c)) {
            String lexema = "";
            while (ehLetra(caractereAtual()) || ehDigito(caractereAtual())) {
                lexema = lexema + caractereAtual();
                avancar();
            }
            String tipo = Token.IDENTIFICADOR;
            for (String palavra : palavrasReservadas) {
                if (palavra.equals(lexema)) {
                    tipo = lexema;
                }
            }
            return new Token(tipo, lexema, linhaInicial, colunaInicial);
        }

        // número inteiro ou decimal
        if (ehDigito(c)) {
            String lexema = "";
            while (ehDigito(caractereAtual())) {
                lexema = lexema + caractereAtual();
                avancar();
            }
            if (caractereAtual() == '.' && ehDigito(caractereSeguinte())) {
                lexema = lexema + '.';
                avancar();
                while (ehDigito(caractereAtual())) {
                    lexema = lexema + caractereAtual();
                    avancar();
                }
            }
            return new Token(Token.NUMERO, lexema, linhaInicial, colunaInicial);
        }

        // operadores de dois caracteres: ==, !=, <=, >=
        String doisCaracteres = "" + c + caractereSeguinte();
        for (String operador : operadoresDuplos) {
            if (operador.equals(doisCaracteres)) {
                avancar();
                avancar();
                return new Token(operador, operador, linhaInicial, colunaInicial);
            }
        }

        // símbolos de um caractere
        if (ehSimbolo(c)) {
            avancar();
            return new Token("" + c, "" + c, linhaInicial, colunaInicial);
        }

        throw new ErroLexico(linhaInicial, colunaInicial, "caractere inválido '" + c + "'");
    }
}
