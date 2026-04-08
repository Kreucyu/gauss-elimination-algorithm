package com.gauss.Model;

public class Classificador {

    public Classificador() {

    }

    public String classificarMatriz(double[][] matriz, int linhasMatriz, int colunasMatriz) {
        for(int i = 0; i < linhasMatriz - 1; i++) {
            boolean linhaZerada = true;
            for(int j = 0; j < colunasMatriz - 1; j++) {
                if(matriz[i][j] != 0) {
                    linhaZerada = false;
                    break;
                }
            }
            if(linhaZerada) {
                return matriz[i][colunasMatriz] != 0 ? "SI - Sistema Impossível" : "SPI - Sistema Possível Indeterminado";
            }
        }
        return "SPD - Sistema Possível Determinado";
    }
}
