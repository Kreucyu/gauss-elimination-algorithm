package com.gauss.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Menu {
    private Scanner sc;
    public Menu(Scanner sc) {
        this.sc = sc;
    }

    public void exibirMenu() {
        System.out.println("------------ ALGORITMO DE ELIMINAÇÃO DE GAUSS | ÁLGEBRA LINEAR ------------\n\nComo deseja enviar o sistema?\n1-Arquivo txt\n2-Digitando\n");
        int opcao = sc.nextInt();

        if(opcao == 1) {
            enviarArquivo();
            return;
        }
        digitarTexto();
    }

    public List<String> digitarTexto() {
        System.out.println("\n      -> Digite seu sistema linha por linha <-\n-> Quando finalizar, digite um '.' e aperte Enter <-\n");
        List<String> linhasDigitadas = new ArrayList<>();
        String linha;
        while(!(linha = sc.nextLine()).equals(".")) {
            linhasDigitadas.add(linha);
        }
        return linhasDigitadas;
    }

    public void enviarArquivo() {
        System.out.println("\nDigite o caminho do seu arquivo: ");
        String caminhoArquivo = sc.next();
        Leitor leitor = new Leitor();
        leitor.getCaminho(caminhoArquivo);
    }

}
