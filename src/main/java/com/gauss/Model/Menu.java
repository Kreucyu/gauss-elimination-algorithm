package com.gauss.Model;

import java.util.*;

public class Menu {
    private final Scanner sc;
    private final Leitor leitor;
    private final Gauss gauss;

    public Menu(Scanner sc) {
        this.sc = sc;
        this.leitor = new Leitor(sc);
        this.gauss = new Gauss();
    }

    public void exibirMenu() {
            String opcao;
            do {
                exibirOpcoes();
                opcao = sc.nextLine().trim();

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
        System.out.println("""
                
                      -> Digite seu sistema linha por linha <-
                -> Quando finalizar, digite um '.' e aperte Enter <-
                """);
        gauss.realizarCalculo(leitor.lerEquacaoDigitada(), leitor);
    }

    private void enviarArquivo() {
        System.out.print("\nDigite o caminho do seu arquivo: ");
        gauss.realizarCalculo(leitor.lerEquacaoArquivo());
    }

    private void exibirOpcoes() {
        System.out.println("""
                        
                        ------------ ALGORITMO DE ELIMINAÇÃO DE GAUSS | ÁLGEBRA LINEAR ------------
                        
                        Como deseja enviar o sistema?
                        
                        1-Arquivo txt
                        2-Digitando
                        3-Encerrar
                        """);
    }
}
