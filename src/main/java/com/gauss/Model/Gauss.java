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
                boolean trocou = substituirPivoNulo(matriz, i);
                pivo = matriz[i][i];
                if(!trocou) {
                    continue;
                }
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
        classificador.classificarMatriz(matriz, linhasMatriz, colunasMatriz, leitor.getMapaColunas());
        linhasMatriz = 0;
        colunasMatriz = 0;
        leitor.limpar();
    }

    private boolean substituirPivoNulo(double[][] matriz, int i) {
        double candidato = 0;
        int linha = -1;
        for(int p = i + 1; p < linhasMatriz; p++) {
            double valor = Math.abs(matriz[p][i]);
            if(valor > 1e-9 && valor > candidato) {
                    candidato = valor;
                    linha = p;
            }
        }
        if(linha == -1) {
            return false;
        }
            double temp;
            for(int j = 0; j < colunasMatriz; j++) {
                temp = matriz[i][j];
                matriz[i][j] = matriz[linha][j];
                matriz[linha][j] = temp;
            }
            return true;
        }
}
