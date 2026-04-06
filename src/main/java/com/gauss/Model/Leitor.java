package com.gauss.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Leitor {
    private Scanner sc;

    public Leitor(Scanner sc) {
        this.sc = sc;
    }

    public double[][] obterMatriz() {
        return null;
    }

    public List<String> lerEquacaoArquivo() {
        String caminhoArquivo = sc.next();
        return null;
    }

    public List<String> lerEquacaoDigitada() {
        List<String> linhasDigitadas = new ArrayList<>();
        String linha;
        while(!(linha = sc.nextLine()).equals(".")) {
            linhasDigitadas.add(linha);
        }
        return linhasDigitadas;
    }
}
