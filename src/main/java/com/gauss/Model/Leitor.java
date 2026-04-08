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

    /*
    metodo responsável por ler o arquivo do sistema, recebendo o
    caminho do arquivo do usuário e transformando em uma lista de linhas,
    removendo aspas e espaços em branco desnecessários para não ocorrer
    inconsistensias na leitura.
    */
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

    /*
    metodo que lê a entrada do usuário digitada e transforma as linhas em uma
    lista de linhas, ignorando linhas vazias e encerrando quando o usuario digitar um (.).
    */
    public double[][]  lerEquacaoDigitada() {
        while(!(linha = sc.nextLine()).equals(".")) {
            if(linha.isEmpty()) continue;
            linhasEquacao.add(linha);
        }
        return obterMatriz();
    }

    /*
    metodo responsável por passar uma matriz como retorno, após transformar o texto do usuário
    em valores da matriz, coleta os dados de tamanho da matriz por meio do metodo obterTamanhoMatriz(),
    em seguida chama o metodo responsável por separar e preencher a matriz, depois de preencher, chama
    exibirMatriz() para mostrar a estrutura inicial da mesma.
     */
    private double[][] obterMatriz() {
        matriz = obterTamanhoMatriz();
        separarEquacaoEPreencherMatriz();
        System.out.println("\nA matriz é: \n");
        exibirMatriz();
        return matriz;
    }

    /*
    esse metodo realiza o cáluclo do tamanho de linhas e colunas que a matriz vai ter, sendo a quantidade
    de linhas o tamanho da lista de linhas coletada da entrada do usuário (passando arquivo ou digitando) e,
    a quantidade de colunas é feito um cálculo com base em um loop de repetição, é feita a leitura da lista de linhas,
    para cada linha dentro da lista, irá ser feito um recorte da equação, separando o lado antes e depois do sinal de =,
    no lado antes do sinal, é removido todos os sinais do começo da linha, para evitar espaços em branco no cálculo e,
    em seguida é feito o recorte dos restantes dos valores com base no sinal de + ou de -, com o contador do número de
    colunas atualizando conforme encontra uma linha com mais variáveis do que a anterior, se não encontrar nenhuma linha
    com mais variáveis, mantém o maior valor encontrado, depois retorna a criação de uma nova matriz com os valores calculados.
     */
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

    /*
    metodo que divide os valores da matriz em lado esquerdo e direito, para depois montar a matriz escalonada, removendo espaços
    entre os valores e fazendo a separação, criando um mapa de variáveis que vai servir para mapear os coeficientes dentro da matriz,
    utilizando a varíavel como chave para descobrir tanto o coeficiente quanto a posição que ele vai ocupar na matriz, chamando
    mapearCoeficientes() passando o mapa de variaveis e o lado esquerdo da equação, depois chamando o metodo que define o lado direito
    da equação dentro da matriz.
     */
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

    //metodo que mapeia todas as variaveis de acordo com a posicao dentro da matriz.
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

    /*
    metodo que utiliza o mapa de variaveis para mapear da forma correta os coeficientes em suas devidas linhas e colunas dentro da matriz.
     */
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

    public void exibirMatriz() {
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

    public void limpar() {
        linha = null;
        linhasEquacao.clear();
        quantidadeDeColunas = 0;
        quantidadeDeLinhas = 0;
    }
}
