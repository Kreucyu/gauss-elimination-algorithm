package com.gauss.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Leitor {
    private Scanner sc;
    private String linha;
    private List<String> linhasEquacao;

    public Leitor(Scanner sc) {
        this.linhasEquacao = new ArrayList<>();
        this.sc = sc;
    }

    private double[][] obterMatriz() {
        int quantidadeDeLinhas = linhasEquacao.size();
        int quantidadeDeColunas = 0;

        for(String linhas : linhasEquacao) {
            String[] variaveis = linhas.split("[+=-]");
            if(variaveis.length > quantidadeDeColunas) quantidadeDeColunas = variaveis.length;
        }

        double[][] matriz = new double[quantidadeDeLinhas][quantidadeDeColunas];


        return matriz;
    }

    public double[][] lerEquacaoArquivo() {
        String caminhoArquivo = sc.next();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(caminhoArquivo));
            while((linha = bufferedReader.readLine()) != null) {
                this.linhasEquacao.add(linha);
            }
            return obterMatriz();
        } catch (IOException e) {
            throw new RuntimeException("Erro, não foi possível ler o arquivo: ", e);
        }
    }

    public double[][]  lerEquacaoDigitada() {
        while(!(linha = sc.nextLine()).equals(".")) {
            if(linha.isEmpty()) continue;
            linhasEquacao.add(linha);
        }
        return obterMatriz();
    }
}
