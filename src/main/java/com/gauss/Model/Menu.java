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
                //chama função para exibir o menu e coleta a opção que o usuário deseja.
                exibirOpcoes();
                opcao = sc.nextLine().trim();

                switch(opcao) {
                    case "1" -> enviarArquivo();
                    case "2" -> digitarTexto();
                    case "3" -> System.out.println("\nSaindo...");
                    default -> System.out.println("\nOpção inválida!");
                }
            /*loop de repetição utilizado para continuar
            recebendo sistemas do usuário até ele decidir encerrar.*/
            } while (!opcao.equals("3"));

            sc.close();
    }

    private void digitarTexto() {
        //texto do menu de digitação.
        System.out.println("""
                
                      -> Digite seu sistema linha por linha <-
                -> Quando finalizar, digite um '.' e aperte Enter <-
                """);
        /*chamada do metodo de realizar cálculo passando como parâmetro uma chamada
        do leitor que irá coletar a equação e transformar o texto digitado em matriz.*/
        gauss.realizarCalculo(leitor.lerEquacaoDigitada(), leitor);
    }

    private void enviarArquivo() {
        System.out.print("\nDigite o caminho do seu arquivo: ");
        /*chamada do metodo de realizar cálculo passando como parâmetro uma chamada
        do leitor que irá coletar o caminho do arquivo e transformar o texto digitado em matriz.*/
        gauss.realizarCalculo(leitor.lerEquacaoArquivo(), leitor);
    }

    private void exibirOpcoes() {
        //exibição do menu de opções
        System.out.println("""
                        
                        ------------ ALGORITMO DE ELIMINAÇÃO DE GAUSS | ÁLGEBRA LINEAR ------------
                        
                        Como deseja enviar o sistema?
                        
                        1-Arquivo txt
                        2-Digitando
                        3-Encerrar
                        """);
    }
}
