package com.gauss.Model;

import java.util.*;

public class Classificador {

    //metodo que realiza a classificação da matriz
    public void classificarMatriz(double[][] matriz, int linhasMatriz, int colunasMatriz, Map<Integer, Character> colunas) {
        System.out.println("\nO tipo do sistema é: ");
        boolean temVariavelLivre = false;
        //percorre todas as linhas da matriz
        for (int i = 0; i < linhasMatriz; i++) {
            boolean linhaZerada = true;
            //percorre todas as colunas da matriz
            for (int j = 0; j < colunasMatriz - 1; j++) {
                //confere se o valor encontrado é maior que um valor muito próximo de 0.
                if (Math.abs(matriz[i][j]) > 1e-9) {
                    //se encontrar valores diferentes de zero ele irá definir que não tem linha zerada e encerra o loop.
                    linhaZerada = false;
                    break;
                }
            }
            //se a linha zerada for verdadeiro, ele vai testar se o valor final da coluna zerada é diferente de 0, o sistema é impossível.
            if (linhaZerada) {
                if (Math.abs(matriz[i][colunasMatriz - 1]) > 1e-9) {
                    System.out.println("\nSI - Sistema Impossível\nConjunto solução: []");
                    return;
                }
                //caso a linha INTEIRA for 0, então temos uma SPI, com variável livre.
                temVariavelLivre = true;
            }

        }
        //depois de informado acima, o sistema define que o sistema é uma SPI e chama o metodo que calcula o conjunto solução.
        if (temVariavelLivre) {
            System.out.println("\nSPI - Sistema Possível Indeterminado\n\n" + obterConjuntoSolucaoSPI(matriz, colunas, linhasMatriz, colunasMatriz));
            return;
        }
        //se não temos linhas zeradas, então teremos um SPD, chamamos o metodo que calcula o conjunto solução.
        System.out.println("\nSPD - Sistema Possível Determinado\n\n" + obterConjuntoSolucaoSPD(matriz, colunas, linhasMatriz, colunasMatriz));
    }

    //metodo responsável pelo cálculo do conjunto solução de uma SPI.
    private String obterConjuntoSolucaoSPI(double[][] matriz, Map<Integer, Character> colunas, int linhasMatriz, int colunasMatriz) {
        Map<Character, String> variaveisResolvidas = new LinkedHashMap<>();
        Set<Integer> colunasComPivo = new HashSet<>();
        List<String> solucoes = new ArrayList<>();
        char parametro = 'a';

        //realiza a busca de um pivo que seja maior que 0 ou próximo disso.
        for (int i = linhasMatriz - 1; i >= 0; i--) {
            int pivo = -1; //inicia com uma coluna inexistente no pivô.
            for (int j = 0; j < colunasMatriz - 1; j++) {
                if (Math.abs(matriz[i][j]) > 1e-9) {
                    pivo = j;
                    break;
                }
            }
            //se o pivo não for uma variável livre, então vai adicionar o pivô em uma lista de pivôs.
            if (pivo != -1) {
                colunasComPivo.add(pivo);
            }
        }

        //vai definir as variáveis livres, passando um parâmetro sendo uma letra do alfabeto que vai ser diferente a cada variável livre.
        for (int j = 0; j < colunasMatriz - 1; j++) {
            if (!colunasComPivo.contains(j)) {
                char variavel = colunas.get(j);
                variaveisResolvidas.put(variavel, String.valueOf(parametro));
                parametro++;
            }
        }

        //inicia da linha final da matriz e define um pivô nãu nulo.
        for (int i = linhasMatriz - 1; i >= 0; i--) {
            int pivo = -1;
            for (int j = 0; j < colunasMatriz - 1; j++) {
                if (Math.abs(matriz[i][j]) > 1e-9) {
                    pivo = j;
                    break;
                }
            }
            //se o pivo for -1 significa que estamos na linha nula da variável nula, então vamos ignorar.
            if (pivo == -1) continue;
            //define o termo independente.
            double termoIndependente = matriz[i][colunasMatriz - 1];
            double valorPivo = matriz[i][pivo];
            //formata o termo independente para que se torne um texto.
            String expressao = formatarNumero(termoIndependente);

            //busca entre os valores para encontrar um valor de variável que não seja nulo.
            for (int j = pivo + 1; j < colunasMatriz - 1; j++) {
                String valorVariavel = variaveisResolvidas.get(colunas.get(j));

                if(valorVariavel == null) continue;

                //define o valor do coeficiente.
                double coeficiente = matriz[i][j];

                //se for 0 ou próximo de 0 ele vai ignorar.
                if(Math.abs(coeficiente) < 1e-9) continue;

                try {
                    //vai tentar formatar o valor para uma melhor apresentação.
                    double valorNumerico = Double.parseDouble(valorVariavel);
                    double produto = coeficiente * valorNumerico;
                    String sinal = produto >= 0 ? " - " : " + ";
                    expressao += sinal + formatarNumero(Math.abs(produto));
                    continue;
                } catch (NumberFormatException e) {
                    //se não conseguir formatar ele segue com a execução padrão.
                }

                //formata os valores, tentando reduzir os cálculos e tentando separar a troca de sinais.
                String coeficienteFormatado = (Math.abs(Math.abs(coeficiente) - 1.0) < 1e-9) ? "" :
                        formatarNumero(Math.abs(coeficiente)) + " * ";

                String sinal = coeficiente > 0 ? " - " : " + ";

                String valorExibido = valorVariavel.contains(" ")
                        ? "(" + valorVariavel + ")" : valorVariavel;
                expressao += sinal +  coeficienteFormatado + valorExibido;
            }
            //tenta dividir os valores do pivo com o valor do termo independente.
            if(Math.abs(valorPivo - 1.0) > 1e-9) {
                try {
                    double valorNumerico = Double.parseDouble(expressao);
                    expressao = formatarNumero(valorNumerico / valorPivo);
                } catch (NumberFormatException e) {
                    //se não conseguir formatar ele vai exibir entre parêntesses.
                    expressao = "(" + expressao + ") / " + formatarNumero(valorPivo);
                }
            }
            //define a posição das variáveis resolvidas.
            variaveisResolvidas.put(colunas.get(pivo), expressao);
        }
        //ordena a exibição dos termos em ordem alfabética e retorna o conjunto solução.
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

    //metood que formata um numero passado, arredondando ele ou formatando em menos casas decimais.
    private String formatarNumero(double numero) {
        if(Math.abs(numero - Math.round(numero)) < 1e-9) {
            return String.valueOf((long) Math.round(numero));
        }
        return String.format("%.1f", numero);
    }

    //metodo que calcula o conjunto solução do SPD.
    private String obterConjuntoSolucaoSPD(double[][] matriz, Map<Integer, Character> colunas, int linhasMatriz, int colunasMatriz) {
        //mapa de variáveis que já foram resolvidas.
        Map<Character, Double> variaveisResolvidas = new LinkedHashMap<>();
        //lista de soluções encontradas.
        List<String> solucoes = new ArrayList<>();
        double valorVariavel = 0;

        //laço de repetição iniciando do final da matriz, pela variável mais "fácil", indo do final para o começo
        for (int i = linhasMatriz - 1; i >= 0; i--) {
            //define o termo independente sendo o último valor da coluna.
            double termoIndependente = matriz[i][colunasMatriz - 1];
            int pivo = -1; //pivo inicia com uma coluna que nao existe.
            for (int j = 0; j < colunasMatriz - 1; j++) {
                //encontra um valor diferente de 0 e define ele como pivo, salvando a posição da coluna na matriz.
                if (matriz[i][j] != 0) {
                    pivo = j;
                    break;
                }
            }
            //se a linha atual for a última linha da matriz, vai diretamente dividir o termo independete pelo valor da variável que queremos descobrir.
            if (i == linhasMatriz - 1) {
                valorVariavel = termoIndependente / matriz[i][pivo];
                variaveisResolvidas.put(colunas.get(pivo), valorVariavel);
                continue;
            }
            /*
            sendo qualqquer valor menos a última linha, então vai realizar o cálculo das variáveis ja
             resolvidas em relação ao termo independente e, depois irá calcular o termo pelo valor da variável que queremos descobrir.
             */
            for (int j = pivo + 1; j < colunasMatriz - 1; j++) {
                if (variaveisResolvidas.get(colunas.get(j)) != null) {
                    double resultado = matriz[i][j] * variaveisResolvidas.get(colunas.get(j));
                    termoIndependente -= resultado;
                }
            }
            valorVariavel = termoIndependente / matriz[i][pivo];
            variaveisResolvidas.put(colunas.get(pivo), valorVariavel);
        }

        //ordena todas as variáveis em ordem alfabética para fazer a exibição do conjunto solução.
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