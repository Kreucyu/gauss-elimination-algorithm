package com.gauss.Model;

import java.util.*;

public class Classificador {

    public void classificarMatriz(double[][] matriz, int linhasMatriz, int colunasMatriz, Map<Integer, Character> colunas) {
        System.out.println("\nO tipo do sistema é: ");
        boolean temVariavelLivre = false;
        for (int i = 0; i < linhasMatriz; i++) {
            boolean linhaZerada = true;
            for (int j = 0; j < colunasMatriz - 1; j++) {
                if (Math.abs(matriz[i][j]) > 1e-9) {
                    linhaZerada = false;
                    break;
                }
            }
            if (linhaZerada) {
                if (Math.abs(matriz[i][colunasMatriz - 1]) > 1e-9) {
                    System.out.println("\nSI - Sistema Impossível\nConjunto solução: []");
                    return;
                }
                temVariavelLivre = true;
            }

        }
        if (temVariavelLivre) {
            System.out.println("\nSPI - Sistema Possível Indeterminado\n\n" + obterConjuntoSolucaoSPI(matriz, colunas, linhasMatriz, colunasMatriz));
            return;
        }
        System.out.println("\nSPD - Sistema Possível Determinado\n\n" + obterConjuntoSolucaoSPD(matriz, colunas, linhasMatriz, colunasMatriz));
    }

    private String obterConjuntoSolucaoSPI(double[][] matriz, Map<Integer, Character> colunas, int linhasMatriz, int colunasMatriz) {
        Map<Character, String> variaveisResolvidas = new LinkedHashMap<>();
        Set<Integer> colunasComPivo = new HashSet<>();
        List<String> solucoes = new ArrayList<>();
        char parametro = 'a';

        for (int i = linhasMatriz - 1; i >= 0; i--) {
            int pivo = -1;
            for (int j = 0; j < colunasMatriz - 1; j++) {
                if (Math.abs(matriz[i][j]) > 1e-9) {
                    pivo = j;
                    break;
                }
            }
            if (pivo != -1) {
                colunasComPivo.add(pivo);
            }
        }

        for (int j = 0; j < colunasMatriz - 1; j++) {
            if (!colunasComPivo.contains(j)) {
                char variavel = colunas.get(j);
                variaveisResolvidas.put(variavel, String.valueOf(parametro));
                parametro++;
            }
        }

        for (int i = linhasMatriz - 1; i >= 0; i--) {
            int pivo = -1;
            for (int j = 0; j < colunasMatriz - 1; j++) {
                if (Math.abs(matriz[i][j]) > 1e-9) {
                    pivo = j;
                    break;
                }
            }

            if (pivo == -1) continue;
            String expressao = String.valueOf(matriz[i][colunasMatriz - 1]);

            for (int j = pivo + 1; j < colunasMatriz - 1; j++) {
                String valorVariavel = variaveisResolvidas.get(colunas.get(j));

                if(valorVariavel == null) continue;

                double coeficiente = matriz[i][j];
                if(coeficiente > 0) {
                    expressao += "-" + coeficiente + "*" + valorVariavel;
                    continue;
                }
                expressao += "+" + Math.abs(coeficiente) +  "*" + valorVariavel;
            }
            variaveisResolvidas.put(colunas.get(pivo), expressao);
        }

        for (Map.Entry<Character, String> entry : variaveisResolvidas.entrySet()) {
            String valor = entry.getValue();
            char variavel = entry.getKey();
            solucoes.add(variavel + "= " + valor);
        }
        Collections.sort(solucoes);
        return "Conjunto solução: " + solucoes.toString();
    }

    private String obterConjuntoSolucaoSPD(double[][] matriz, Map<Integer, Character> colunas, int linhasMatriz, int colunasMatriz) {
        Map<Character, Double> variaveisResolvidas = new LinkedHashMap<>();
        List<String> solucoes = new ArrayList<>();
        double valorVariavel = 0;

        for (int i = linhasMatriz - 1; i >= 0; i--) {
            double termoIndependente = matriz[i][colunasMatriz - 1];
            int pivo = -1;
            for (int j = 0; j < colunasMatriz - 1; j++) {
                if (matriz[i][j] != 0) {
                    pivo = j;
                    break;
                }
            }
            if (i == linhasMatriz - 1) {
                valorVariavel = termoIndependente / matriz[i][pivo];
                variaveisResolvidas.put(colunas.get(pivo), valorVariavel);
                continue;
            }
            for (int j = pivo + 1; j < colunasMatriz - 1; j++) {
                if (variaveisResolvidas.get(colunas.get(j)) != null) {
                    double resultado = matriz[i][j] * variaveisResolvidas.get(colunas.get(j));
                    termoIndependente -= resultado;
                }
            }
            valorVariavel = termoIndependente / matriz[i][pivo];
            variaveisResolvidas.put(colunas.get(pivo), valorVariavel);
        }

        for (Map.Entry<Character, Double> entry : variaveisResolvidas.entrySet()) {
            double valor = entry.getValue();
            char variavel = entry.getKey();
            solucoes.add(variavel + "= " + String.format("%.2f", valor));
        }
        variaveisResolvidas.clear();
        Collections.sort(solucoes);
        return "Conjunto solução: " + solucoes.toString();
    }
}