Solucionador de Sistemas Lineares — Eliminação de Gauss

Trabalho avaliativo da disciplina de Álgebra Linear — FURB (Universidade Regional de Blumenau)

---

Objetivo
Consolidar os conceitos teóricos de Matrizes, Determinantes e Sistemas Lineares através da implementação algorítmica do processo de Escalonamento (Eliminação de Gauss), sem o uso de bibliotecas prontas de álgebra linear.

---

Funcionalidades

Leitura da matriz ampliada [A|b] via arquivo .txt ou entrada manual pelo teclado
Escalonamento com método de Eliminação de Gauss até a forma escalonada reduzida
Pivoteamento Parcial: troca de linhas quando o pivô atual é nulo ou próximo de zero
Exibição das matrizes intermediárias após cada operação elementar relevante

Classificação do sistema como:

  SPD — Sistema Possível e Determinado.
  SPI — Sistema Possível e Indeterminado.
  SI — Sistema Impossível.

Apresentação da solução completa (ex: x=1, y=2) ou solução geral em função de variáveis livres no caso de SPI

---

Tecnologias

Linguagem: Java (livre escolha conforme enunciado)
Sem uso de bibliotecas de álgebra linear (numpy, scipy, math.solve ou similares)

---

Casos de Teste (Validação)

Caso Referência Classificação SPDUnidade 4.
Exercício 1(a) Sistema Possível e Determinado SPIUnidade 4.
Exercício 1(b) Sistema Possível e Indeterminado SIUnidade 3.
Exercício 11(j) Sistema Impossível.

---

Observações

O código contém comentários explicando a relação de cada função com os conceitos matemáticos envolvidos
O relatório técnico (PDF) descreve o algoritmo de classificação, o tratamento de casos de pivô nulo e a divisão de tarefas entre os integrantes
