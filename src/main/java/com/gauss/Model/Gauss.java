package com.gauss.Model;

public class Gauss {
    int linhasMatriz;
    int colunasMatriz;
    public Gauss() {
    }

    public void realizarCalculo(double[][] matriz, Leitor leitor) {
        linhasMatriz = leitor.getQuantidadeDeLinhas();
        colunasMatriz = leitor.getQuantidadeDeColunas();

        for(int i = 0; i < matriz.length - 1; i++) {
            double pivo = matriz[i][i];
            for(int j = 0; j < colunasMatriz; j++) {
                matriz[i][j] = -2 * matriz[i][j];
            }
            for(int j = 0; j < colunasMatriz; j++) {
                matriz[i + 1][j] = matriz[i][j] - matriz[i + 1][j];
            }

        }
    }
}
