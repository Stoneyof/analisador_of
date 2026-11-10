package br.edu.ifg;

import java.io.File;
import java.util.List;
import java.util.Scanner;

// Uso: java br.edu.ifg.Principal <arquivo.mini>
public class Principal {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Uso: java br.edu.ifg.Principal <arquivo.mini>");
            return;
        }

        // lê o arquivo inteiro, linha por linha, para uma String
        Scanner leitor = new Scanner(new File(args[0]));
        String codigo = "";
        while (leitor.hasNextLine()) {
            codigo = codigo + leitor.nextLine() + "\n";
        }
        leitor.close();

        try {
            List<Token> tokens = new AnalisadorLexico(codigo).tokenizar();
            new AnalisadorSintatico(tokens).analisar();
            System.out.println("OK: código sintaticamente correto.");
        } catch (ErroDeAnalise e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
