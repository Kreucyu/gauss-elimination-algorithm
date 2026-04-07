package com.gauss.Model;

public class Gauss {
    int linhasMatriz;
    int colunasMatriz;
    public Gauss() {
    }

    public void realizarCalculo(double[][] matriz, Leitor leitor) {
        linhasMatriz = leitor.getQuantidadeDeLinhas();
        colunasMatriz = leitor.getQuantidadeDeColunas();

        System.out.println("\nIniciando cálculo: ");

        for(int i = 0; i < matriz.length - 1; i++) {
            double pivo = matriz[i][i];
            if(Math.abs(pivo) < 1e-9) {

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
        linhasMatriz = 0;
        colunasMatriz = 0;
        leitor.limpar();
    }
}
