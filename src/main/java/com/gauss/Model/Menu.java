package com.gauss.Model;

import java.util.*;

public class Menu {
    private final Scanner sc;
    private final Leitor leitor;

    public Menu(Scanner sc) {
        this.sc = sc;
        this.leitor = new Leitor(sc);
    }

    public void exibirMenu() {
            String opcao;
            do {
                exibirOpcoes();
                opcao = sc.next().trim();

                switch(opcao) {
                    case "1" -> enviarArquivo();
                    case "2" -> digitarTexto();
                    case "3" -> System.out.println("\nSaindo...");
                    default -> System.out.println("\nOpção inválida!");
                }

            } while (!opcao.equals("3"));
            sc.close();
    }

    private void digitarTexto() {
        System.out.println("\n      -> Digite seu sistema linha por linha <-\n-> Quando finalizar, digite um '.' e aperte Enter <-\n");
        leitor.lerEquacaoDigitada();
    }

    private void enviarArquivo() {
        System.out.print("\nDigite o caminho do seu arquivo: ");
        leitor.lerEquacaoArquivo();
    }

    private void exibirOpcoes() {
        System.out.println("""
                        \n------------ ALGORITMO DE ELIMINAÇÃO DE GAUSS | ÁLGEBRA LINEAR ------------\n
                        Como deseja enviar o sistema?
                        \n1-Arquivo txt
                        2-Digitando
                        3-Encerrar
                        """);
    }
}
