package com.gauss.Model;

import java.util.*;

public class Gauss {
    int linhasMatriz;
    int colunasMatriz;
    public Gauss() {
    }

    public void realizarCalculo(double[][] matriz, Leitor leitor) {
        Classificador classificador = new Classificador();
        linhasMatriz = leitor.getQuantidadeDeLinhas();
        colunasMatriz = leitor.getQuantidadeDeColunas();

        System.out.println("\nIniciando cálculo: ");

        for(int i = 0; i < linhasMatriz - 1; i++) {
            double pivo = matriz[i][i];
            if(Math.abs(pivo) < 1e-9) {
                substituirPivoNulo(matriz, i);
                pivo = matriz[i][i];
                System.out.println("\nPivô é 0, realizando troca com o valor " + pivo + "!");
            }
            System.out.printf("\nPivô atual: " + String.format("%.1f", pivo) + "\n");
            for(int j = i + 1; j < linhasMatriz; j++) {
                double multiplicador = matriz[j][i] / pivo;
                for(int k = 0; k < colunasMatriz; k++) {
                    matriz[j][k] = matriz[j][k] - (multiplicador * matriz[i][k]);
                }
            }
            System.out.print("\n");
            System.out.println("[A(" + (i + 1) + ")|b(" + (i + 1) + ")]:\n");
            leitor.exibirMatriz();
        }
        System.out.println("\nO tipo do sistema é: ");
        int tipo = classificador.classificarMatriz(matriz, linhasMatriz, colunasMatriz);
        switch(tipo) {
            case 1 -> System.out.println("\nSI - Sistema Impossível\nConjunto solução S = {}");
            case 2 -> System.out.println("\nSPI - Sistema Possível Indeterminado\n\n" + obterConjuntoSolucaoSPI(matriz));
            case 3 -> System.out.println("\nSPD - Sistema Possível Determinado\n\n" + obterConjuntoSolucaoSPD(matriz, leitor.getMapaColunas()));
        }
        linhasMatriz = 0;
        colunasMatriz = 0;
        leitor.limpar();
    }

    private void substituirPivoNulo(double[][] matriz, int i) {
        double candidato = 0;
        int linha = 0;
        for(int p = i + 1; p < linhasMatriz; p++) {
            if(Math.abs(matriz[p][i]) > 1e-9) {
                if(matriz[p][i] > candidato) {
                    candidato = matriz[p][i];
                    linha = p;
                }
            }
        }
        if(candidato != 0 && linha != 0) {
            double temp;
            for(int j = 0; j < colunasMatriz; j++) {
                temp = matriz[i][j];
                matriz[i][j] = matriz[linha][j];
                matriz[linha][j] = temp;
            }
        }
    }

    private String obterConjuntoSolucaoSPI(double[][] matriz) {

        return null;
    }

    private String obterConjuntoSolucaoSPD(double[][] matriz, Map<Integer, Character> colunas) {
        Map<Character, Double> variaveisResolvidas = new LinkedHashMap<>();
        List<String> solucoes = new ArrayList<>();
        double valorVariavel = 0;

        for(int i = linhasMatriz - 1; i >= 0; i--) {
            double termoIndependente = matriz[i][colunasMatriz - 1];
            int pivo = -1;
            for(int j = 0; j < colunasMatriz - 1; j++) {
                if(matriz[i][j] != 0) {
                    pivo = j;
                    break;
                }
            }
            if(i == linhasMatriz - 1) {
                valorVariavel = termoIndependente / matriz[i][pivo];
                variaveisResolvidas.put(colunas.get(pivo), valorVariavel);
                continue;
            }
            for(int j = pivo + 1; j < colunasMatriz - 1; j++) {
                if(variaveisResolvidas.get(colunas.get(j)) != null) {
                    double resultado = matriz[i][j] * variaveisResolvidas.get(colunas.get(j));
                    termoIndependente -= resultado;
                }
            }
            valorVariavel = termoIndependente / matriz[i][pivo];
            variaveisResolvidas.put(colunas.get(pivo), valorVariavel);
        }

        for(Map.Entry<Character, Double> entry : variaveisResolvidas.entrySet()) {
            double valor = entry.getValue();
            char variavel = entry.getKey();
            solucoes.add(variavel + "= " + String.format("%.2f", valor));
        }

        return solucoes.toString();
    }
}
