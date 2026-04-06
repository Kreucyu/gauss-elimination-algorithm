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
        separarEquacao();
        mapearCoeficientes();
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

    private void separarEquacao() {
        List<String> ladoEsquerdo = new ArrayList<>();
        List<Integer> ladoDireito = new ArrayList<>();

        //para cada linha do meu sistema, vou substituir a linha original pela mesma versão dela sem espaços (2x + 3y - z = 10 -> 2x+3y-z=10).
        linhasEquacao.replaceAll(linha -> linha.replaceAll("\\s", ""));

        //para cada linha do meu sistema, vou separar a minha linha entre valores do lado esquerdo e valores do lado direito.
        for(int i = 0; i < linhasEquacao.size(); i++) {
            String[] partes = linhasEquacao.get(i).split("=");
            ladoEsquerdo.add(partes[0]);
            ladoDireito.add(Integer.valueOf(partes[1]));
        }
        Map<Character, Integer> mapaVariaveis = mapearVariaveis(ladoEsquerdo);
    }

    private Map<Character, Integer> mapearVariaveis(List<String> ladoEsquerdo) {
        /*
        para cada linha do meu lado esquerdo, vou fazer um mapeamento de caractere por caractere,
        retornando a posicao dele na tabela ASCII (tabela de caracteres), filtrando apenas letras e
        convertendo de posicao da tabela diretamente para um caractere, todos devem ser unicos, sem repeticoes,
        devem ser ordenados em ordem alfabetica e devem estar em formato de uma lista (filtrada e ordenada com apenas letras).
         */
        List<Character> variaveis = ladoEsquerdo.stream()
                .flatMap(lado -> lado.chars()
                        .filter(Character::isLetter)
                        .mapToObj(c -> (char) c))
                .distinct()
                .sorted()
                .toList();

        //mapeando cada elemento da lista em uma posicao, para definir o local de cada coeficiente dentro da matriz com base na sua variável.
        Map<Character, Integer> mapaVariaveis = new HashMap<>();
        for(int i = 0; i < variaveis.size(); i++) {
            mapaVariaveis.put(variaveis.get(i), i);
        }
        return mapaVariaveis;
    }

    private void mapearCoeficientes() {

    }
}
