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
    private Map<Character, Integer> mapaVariaveis;
    private Map<Character, Double> mapaCoeficientes;
    private Map<Integer, Character> mapaColunas;

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
            System.out.print("\nSistema:\n\n");
            exibirSistema();
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
        System.out.print("\nSistema:\n\n");
        exibirSistema();
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
        mapaVariaveis = mapearVariaveis(ladoEsquerdo);
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
        mapaVariaveis = new HashMap<>();
        mapaColunas = new HashMap<>();
        for(int i = 0; i < variaveis.size(); i++) {
            mapaVariaveis.put(variaveis.get(i), i);
            mapaColunas.put(i, variaveis.get(i));
        }
        return mapaVariaveis;
    }

    /*
    metodo que utiliza o mapa de variaveis para mapear da forma correta os coeficientes em suas devidas linhas e colunas dentro da matriz.
     */
    private void mapearCoeficientes(Map<Character, Integer> variaveis, List<String> ladoEsquerdo) {
        mapaCoeficientes = new HashMap<>();
        for(int i = 0; i < ladoEsquerdo.size(); i++) {
            //crio uma lista de coeficientes separando os valores pelo sinal, fazendo um recorte de cada valor do lado esquerdo.
            String[] coeficientes = ladoEsquerdo.get(i).split("(?=[+-])");
            for(int j = 0; j < coeficientes.length; j++) {
                //substituo tudo o que não for letra por espaços vazios para obter apenas a variável.
                String variavel = coeficientes[j].replaceAll("[^a-zA-Z]", "");
                char variavelKey = variavel.charAt(0);
                //substituo todas as letras por espaços vazios para obter apenas o sinal + valor do coeficiente.
                double valor = obterCoeficiente(coeficientes[j].replaceAll("[a-zA-Z]", ""));
                //defino o coeficiente com a chave sendo a variável dentro do mapa de coeficientes.
                mapaCoeficientes.put(variavelKey, valor);
            }
            //chamo os métodos para definir aquela linha na matriz e depois reinicio os valores do mapa.
            definirValoresMatrizLadoEsquerdo(mapaCoeficientes, variaveis, i);
            mapaCoeficientes.clear();
        }
    }

    /*
    metodo responsável por coletar o sinal e o número do coeficiente, decidindo se tem algum valor ou se é 1 (apenas a variável aparece),
    depois, retorno o valor do coeficiente ja com o sinal, se o sinal é -, retorna o valor encontrado negativo, se for + ou não ter nenhum
    sinal vai retornar o valor positivo.
     */
    private double obterCoeficiente(String coeficiente) {
        String sinal = coeficiente.replaceAll("[0-9.]", "");
        String numero = coeficiente.replaceAll("[^0-9]", "");
        double valor = numero.isEmpty() ? 1 : Double.parseDouble(numero);
        return sinal.equals("-") ? -valor : + valor;
    }

    /*
    esse metodo foi utilizado para definir o lado A da matriz escalonada, percorrendo todos os elementos do meu mapa de forma que seja "apenas visualização",
   e para cada valor eu pegava a chave do meu coeficiente (variável) e fazia uma busca dentro do meu mapa de variáveis, utilizando essa chave para obter o
   valor da coluna responsável por aquela variável dentro da minha matriz, fazia coeficiente -> variável -> posição -> matriz(posição) = coeficiente.
     */
    private void definirValoresMatrizLadoEsquerdo(Map<Character, Double> coeficientes, Map<Character, Integer> variaveis, int linha) {
        for(Map.Entry<Character, Double> entry : coeficientes.entrySet()) {
            int coluna = variaveis.get(entry.getKey());
            matriz[linha][coluna] = entry.getValue();
        }
    }

    //para esse lado, apenas peguei os valores do lado direito das linhas e setei eles na posicao mais a direita das minhas linhas da matriz.
    private void definirValoresMatrizLadoDireito(List<Double> ladoDireito) {
        for(int i = 0; i < ladoDireito.size(); i++) {
            matriz[i][quantidadeDeColunas - 1] = ladoDireito.get(i);
        }
    }

    //esse metodo serve para exibir a matriz, definindo o espaçamento entre os valores para que fiquem simétricos, percorrendo elementos um por um e exibindo-os.
    public void exibirMatriz() {
            for(int j  = 0; j < quantidadeDeColunas - 1; j++) {
                System.out.printf(j == 0 ? "%4s" : "%8s", mapaColunas.get(j));
            }
        System.out.printf("%8s\n", "b");
        for(int i  = 0; i <= quantidadeDeLinhas - 1; i++) {
            System.out.print("|");
            for(int j  = 0; j < quantidadeDeColunas; j++) {
                System.out.printf(j == 0 ? "%4.1f" : "%8.1f", matriz[i][j]);
            }
            System.out.println("|");
        }
    }

    //retorna valores desta classe, para consulta por outras classes fora desta.
    public int getQuantidadeDeColunas() {
        return quantidadeDeColunas;
    }
    public int getQuantidadeDeLinhas() {
        return quantidadeDeLinhas;
    }
    public Map<Integer, Character> getMapaColunas() {return Collections.unmodifiableMap(mapaColunas);}

    //limpa todos os dados encontrados no cálculo e na matriz, para que quando o usuário enviar outro sistema, o cálculo recomeçar do zero.
    public void limpar() {
        linha = null;
        linhasEquacao.clear();
        quantidadeDeColunas = 0;
        quantidadeDeLinhas = 0;
    }

    //metodo para exibir o sistema enviado pelo usuário.
    private void exibirSistema() {
        for(String linha : linhasEquacao) {
            System.out.println(linha);
        }
    }
}
