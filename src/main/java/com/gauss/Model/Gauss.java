package com.gauss.Model;

import java.sql.SQLOutput;

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
            case 1 -> System.out.println("SI - Sistema Impossível\nConjunto solução S = {}");
            case 2 -> System.out.println("SPI - Sistema Possível Indeterminado\n" + obterConjuntoSolucaoSPI(matriz));
            case 3 -> System.out.println("SPD - Sistema Possível Determinado\n" + obterConjuntoSolucaoSPD(matriz));
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

    private String obterConjuntoSolucaoSPD(double[][] matriz) {

        return null;
    }
}
