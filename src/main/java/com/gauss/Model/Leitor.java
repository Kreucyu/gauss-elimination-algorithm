package com.gauss.Model;

import java.io.*;
import java.util.*;

public class Leitor {
    private Scanner sc;
    private String linha;
    private List<String> linhasEquacao;

    public Leitor(Scanner sc) {
        this.linhasEquacao = new ArrayList<>();
        this.sc = sc;
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

    private double[][] obterMatriz() {
        double[][] matriz = obterTamanhoMatriz();
        separarEquacao(matriz);
        return matriz;
    }

    private double[][] obterTamanhoMatriz() {
        int quantidadeDeLinhas = linhasEquacao.size();
        int quantidadeDeColunas = 0;
        for(String linhas : linhasEquacao) {
            String[] variaveis = linhas.split("[+=-]");
            if(variaveis.length > quantidadeDeColunas) quantidadeDeColunas = variaveis.length;
        }
        return new double[quantidadeDeLinhas][quantidadeDeColunas];
    }

    private void separarEquacao(double[][] matriz) {
        List<String> ladoEsquerdo = new ArrayList<>();
        List<Integer> ladoDireito = new ArrayList<>();

        //para cada linha do meu sistema
        for(int i = 0; i < linhasEquacao.size(); i++) {
            //vou substituir a linha original pela mesma versão dela sem espaços (2x + 3y - z = 10 -> 2x+3y-z=10)
            linhasEquacao.set(i, linhasEquacao.get(i).replaceAll("\\s+", ""));
            //vou separar a minha linha entre valores do lado esquerdo e valores do lado direito;
            String[] partes = linhasEquacao.get(i).split("=");
            ladoEsquerdo.add(partes[0]);
            ladoDireito.add(Integer.valueOf(partes[1]));
        }
        mapearVariaveis(ladoEsquerdo);
    }

    private void mapearVariaveis(List<String> ladoEsquerdo) {
        Set<Character> variaveisUnicas = new HashSet<>();

        for(int i = 0; i < linhasEquacao.size(); i++) {
            String[] todasAsVariaveis = linhasEquacao.get(i).split("[a-zA-Z]");
            for(int y = 0; y < todasAsVariaveis.length; y++) {
                variaveisUnicas.add(todasAsVariaveis[y].charAt(0));
            }
        }

        List<Character> variaveis = new ArrayList<>(variaveisUnicas);
        Collections.sort(variaveis);
        Map<Character, Integer> mapaVariaveis = new HashMap<>();

        for(int i = 0; i < variaveis.size(); i++) {
            mapaVariaveis.put(variaveis.get(i), i);
        }
    }
}
