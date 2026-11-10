package br.edu.ifg;

import java.util.Iterator;
import java.util.List;

// Analisador sintático (parser): um método para cada regra da gramática.
//
// Gramática:
//   programa    -> "inicio" declaracao* (blocoIfElse | blocoWhile)+ "fim"
//                  (deve haver ao menos um if-else e ao menos um while)
//   declaracao  -> ("int" | "double") identificador ";"
//   blocoIfElse -> "if" "(" condicao ")" bloco "else" bloco
//   blocoWhile  -> "while" "(" condicao ")" bloco
//   bloco       -> "{" comando* "}"
//   comando     -> atribuicao | blocoIfElse | blocoWhile      (podem ser encadeados)
//   atribuicao  -> identificador "=" expressao ";"
//   condicao    -> expressao ("==" | "!=" | "<" | ">" | "<=" | ">=") expressao
//   expressao   -> termo (("+" | "-") termo)*
//   termo       -> fator (("*" | "/") fator)*
//   fator       -> numero | identificador | "(" expressao ")"
public class AnalisadorSintatico {

    private final Iterator<Token> tokens;
    private Token atual;

    // Sobrecarga de construtores: recebe uma lista de tokens...
    public AnalisadorSintatico(List<Token> tokens) {
        this.tokens = tokens.iterator();
        avancar();
    }

    // ...ou um iterador de tokens.
    public AnalisadorSintatico(Iterator<Token> tokens) {
        this.tokens = tokens;
        avancar();
    }

    // Passa para o próximo token. No fim, continua no último (fim de arquivo).
    private void avancar() {
        if (tokens.hasNext()) {
            atual = tokens.next();
        } else if (atual == null) {
            atual = new Token(Token.FIM_ARQUIVO, "", 1, 1);
        }
    }

    private boolean verifica(String tipo) {
        return atual.getTipo().equals(tipo);
    }

    // Exige o token esperado e avança; senão, erro sintático.
    private void consome(String esperado) {
        if (!verifica(esperado)) {
            throw erro(esperado);
        }
        avancar();
    }

    private ErroSintatico erro(String esperado) {
        String encontrado = "'" + atual.getLexema() + "'";
        if (verifica(Token.FIM_ARQUIVO)) {
            encontrado = Token.FIM_ARQUIVO;
        }
        return new ErroSintatico(atual.getLinha(), atual.getColuna(),
                "esperado " + esperado + ", mas encontrado " + encontrado);
    }

    public void analisar() {
        consome("inicio");
        while (verifica("int") || verifica("double")) {
            declaracao();
        }
        boolean temIfElse = false;
        boolean temWhile = false;
        while (verifica("if") || verifica("while")) {
            if (verifica("if")) {
                blocoIfElse();
                temIfElse = true;
            } else {
                blocoWhile();
                temWhile = true;
            }
        }
        if (!temIfElse) {
            throw erro("bloco 'if-else'");
        }
        if (!temWhile) {
            throw erro("bloco 'while'");
        }
        consome("fim");
        consome(Token.FIM_ARQUIVO);
    }

    private void declaracao() {
        avancar(); // int ou double, já verificado
        consome(Token.IDENTIFICADOR);
        consome(";");
    }

    private void blocoIfElse() {
        consome("if");
        consome("(");
        condicao();
        consome(")");
        bloco();
        consome("else");
        bloco();
    }

    private void blocoWhile() {
        consome("while");
        consome("(");
        condicao();
        consome(")");
        bloco();
    }

    private void bloco() {
        consome("{");
        while (!verifica("}")) {
            if (verifica(Token.FIM_ARQUIVO)) {
                throw erro("'}'");
            }
            comando();
        }
        consome("}");
    }

    private void comando() {
        if (verifica(Token.IDENTIFICADOR)) {
            atribuicao();
        } else if (verifica("if")) {
            blocoIfElse();
        } else if (verifica("while")) {
            blocoWhile();
        } else {
            throw erro("comando (atribuição, 'if' ou 'while')");
        }
    }

    private void atribuicao() {
        consome(Token.IDENTIFICADOR);
        consome("=");
        expressao();
        consome(";");
    }

    private void condicao() {
        expressao();
        if (verifica("==") || verifica("!=") || verifica("<")
                || verifica(">") || verifica("<=") || verifica(">=")) {
            avancar();
        } else {
            throw erro("operador relacional");
        }
        expressao();
    }

    private void expressao() {
        termo();
        while (verifica("+") || verifica("-")) {
            avancar();
            termo();
        }
    }

    private void termo() {
        fator();
        while (verifica("*") || verifica("/")) {
            avancar();
            fator();
        }
    }

    private void fator() {
        if (verifica(Token.NUMERO) || verifica(Token.IDENTIFICADOR)) {
            avancar();
        } else if (verifica("(")) {
            avancar();
            expressao();
            consome(")");
        } else {
            throw erro("número, identificador ou '('");
        }
    }
}
