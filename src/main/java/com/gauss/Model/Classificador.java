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
            double termoIndependente = matriz[i][colunasMatriz - 1];
            double valorPivo = matriz[i][pivo];
            String expressao = formatarNumero(termoIndependente);

            for (int j = pivo + 1; j < colunasMatriz - 1; j++) {
                String valorVariavel = variaveisResolvidas.get(colunas.get(j));

                if(valorVariavel == null) continue;

                double coeficiente = matriz[i][j];

                if(Math.abs(coeficiente) < 1e-9) continue;

                try {
                    double valorNumerico = Double.parseDouble(valorVariavel);
                    double produto = coeficiente * valorNumerico;
                    String sinal = produto >= 0 ? " - " : " + ";
                    expressao += sinal + formatarNumero(Math.abs(produto));
                    continue;
                } catch (NumberFormatException e) {

                }

                String coeficienteFormatado = (Math.abs(Math.abs(coeficiente) - 1.0) < 1e-9) ? "" :
                        formatarNumero(Math.abs(coeficiente)) + " * ";

                String sinal = coeficiente > 0 ? " - " : " + ";

                String valorExibido = valorVariavel.contains(" ")
                        ? "(" + valorVariavel + ")" : valorVariavel;
                expressao += sinal +  coeficienteFormatado + valorExibido;
            }
            if(Math.abs(valorPivo - 1.0) > 1e-9) {
                try {
                    double valorNumerico = Double.parseDouble(expressao);
                    expressao = formatarNumero(valorNumerico / valorPivo);
                } catch (NumberFormatException e) {
                    expressao = "(" + expressao + ") / " + formatarNumero(valorPivo);
                }
            }
            variaveisResolvidas.put(colunas.get(pivo), expressao);
        }
        List<Character> ordem = new ArrayList<>();
        for(int i = 0; i < colunasMatriz - 1; i++) {
            ordem.add(colunas.get(i));
        }
        for(Character valor : ordem) {
            if(variaveisResolvidas.containsKey(valor)) {
                solucoes.add(valor + "= " + variaveisResolvidas.get(valor));
            }
        }
        return "Conjunto solução: " + solucoes.toString();
    }

    private String formatarNumero(double numero) {
        if(Math.abs(numero - Math.round(numero)) < 1e-9) {
            return String.valueOf((long) Math.round(numero));
        }
        return String.format("%.1f", numero);
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

        List<Character> ordem = new ArrayList<>();
        for(int i = 0; i < colunasMatriz - 1; i++) {
            ordem.add(colunas.get(i));
        }
        for(Character valor : ordem) {
            if(variaveisResolvidas.containsKey(valor)) {
                solucoes.add(valor + "= " + String.format("%.2f", variaveisResolvidas.get(valor)));
            }
        }
        return "Conjunto solução: " + solucoes.toString();
    }


}