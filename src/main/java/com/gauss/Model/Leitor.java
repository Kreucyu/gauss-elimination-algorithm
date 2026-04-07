package com.gauss.Model;

import java.io.*;
import java.util.*;

public class Leitor {
    private Scanner sc;
    private String linha;
    private List<String> linhasEquacao;
    private double[][] matriz;
    private int quantidadeDeColunas;
    private int quantidadeDeLinhas;

    public Leitor(Scanner sc) {
        this.linhasEquacao = new ArrayList<>();
        this.sc = sc;
        this.matriz = null;
    }

    public Leitor() {}

    public double[][] lerEquacaoArquivo() {
        String caminhoArquivo = sc.nextLine().trim().replace("\"", "");
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
        matriz = obterTamanhoMatriz();
        separarEquacaoEPreencherMatriz();
        exibirMatriz();
        return matriz;
    }

    private double[][] obterTamanhoMatriz() {
        quantidadeDeLinhas = linhasEquacao.size();
        quantidadeDeColunas = 0;
        for(String linhas : linhasEquacao) {
            String[] lados = linhas.split("=");
            String[] variaveis = lados[0].replaceAll("^[+\\-]", "").split("[+\\-]");
            if(variaveis.length + 1 > quantidadeDeColunas) quantidadeDeColunas = variaveis.length + 1;
        }
        return new double[quantidadeDeLinhas][quantidadeDeColunas];
    }

    private void separarEquacaoEPreencherMatriz() {
        List<String> ladoEsquerdo = new ArrayList<>();
        List<Double> ladoDireito = new ArrayList<>();

        //para cada linha do meu sistema, vou substituir a linha original pela mesma versão dela sem espaços (2x + 3y - z = 10 -> 2x+3y-z=10).
        linhasEquacao.replaceAll(linha -> linha.replaceAll("\\s", ""));

        //para cada linha do meu sistema, vou separar a minha linha entre valores do lado esquerdo e valores do lado direito.
        for(int i = 0; i < linhasEquacao.size(); i++) {
            String[] partes = linhasEquacao.get(i).split("=");
            ladoEsquerdo.add(partes[0]);
            ladoDireito.add(Double.parseDouble(partes[1]));
        }
        Map<Character, Integer> mapaVariaveis = mapearVariaveis(ladoEsquerdo);
        mapearCoeficientes(mapaVariaveis, ladoEsquerdo);
        definirValoresMatrizLadoDireito(ladoDireito);
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

    private void mapearCoeficientes(Map<Character, Integer> variaveis, List<String> ladoEsquerdo) {
        Map<Character, Double> mapaCoeficientes = new HashMap<>();
        for(int i = 0; i < ladoEsquerdo.size(); i++) {
            String[] coeficientes = ladoEsquerdo.get(i).split("(?=[+-])");
            for(int j = 0; j < coeficientes.length; j++) {
                String variavel = coeficientes[j].replaceAll("[^a-zA-Z]", "");
                char variavelKey = variavel.charAt(0);
                double valor = obterCoeficiente(coeficientes[j].replaceAll("[a-zA-Z]", ""));
                mapaCoeficientes.put(variavelKey, valor);
            }
            definirValoresMatrizLadoEsquerdo(mapaCoeficientes, variaveis, i);
            mapaCoeficientes.clear();
        }
    }

    private double obterCoeficiente(String coeficiente) {
        String sinal = coeficiente.replaceAll("[0-9.]", "");
        String numero = coeficiente.replaceAll("[^0-9]", "");
        double valor = numero.isEmpty() ? 1 : Double.parseDouble(numero);
        return sinal.equals("-") ? -valor : + valor;
    }

    private void definirValoresMatrizLadoEsquerdo(Map<Character, Double> coeficientes, Map<Character, Integer> variaveis, int linha) {
        for(Map.Entry<Character, Double> entry : coeficientes.entrySet()) {
            int coluna = variaveis.get(entry.getKey());
            matriz[linha][coluna] = entry.getValue();
        }
    }

    private void definirValoresMatrizLadoDireito(List<Double> ladoDireito) {
        for(int i = 0; i < ladoDireito.size(); i++) {
            matriz[i][quantidadeDeColunas - 1] = ladoDireito.get(i);
        }
    }

    private void exibirMatriz() {
        for(int i  = 0; i <= quantidadeDeLinhas - 1; i++) {
            System.out.print("|");
            for(int j  = 0; j < quantidadeDeColunas; j++) {
                System.out.printf(j == 0 ? "%4.1f" : "%8.1f", matriz[i][j]);
            }
            System.out.println("|");
        }
    }

    public int getQuantidadeDeColunas() {
        return quantidadeDeColunas;
    }

    public int getQuantidadeDeLinhas() {
        return quantidadeDeLinhas;
    }
}
