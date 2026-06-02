# 🚗 Highest Paid Toll (O Pedágio Mais Caro)


## 📌 Problema
* Nome: UVa 12047 - Highest Paid Toll
* Link: [Online Judge - Problem 12047](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3198)

## 👥 Integrantes do Grupo
* Lucas Barroso Sá 2426651
* Átila Silvio Carvalho Rocha Melo Oliveira 2320454
* Victor Menezes do Vale 2325183

🗺️ Modelagem e Representação do Grafo
O mapa da cidade foi modelado como um Grafo Direcionado e Ponderado, onde:
Vértices (V): Representam os cruzamentos ou locais da cidade. 
Arestas (E): Representam as ruas de mão única que conectam os locais.
Pesos (C): Representam o custo financeiro do pedágio de cada rua.
Representação Adotada: A estrutura escolhida foi a Lista de Adjacência (implementada via arrays de Bag). Essa escolha foi feita por se tratar de um grafo esparso (com até 10.000 vértices e 100.000 arestas, segundo os limites do problema). Uma matriz de adjacência consumiria memória excessiva e tempo desnecessário de iteração para ruas inexistentes.

🧠Estratégia e Algoritmo Utilizado

Para evitar o estouro de tempo de execução na busca exaustiva por todas as combinações de rotas possíveis, adotamos a estratégia de pré-processamento conhecida como Dijkstra Duplo.
A lógica baseia-se em validar individualmente cada aresta do grafo. Para uma aresta direcionada de u para v com custo c ser considerada parte de uma rota válida, ela deve satisfazer a restrição matemática do orçamento total financeiro

Se a aresta for válida, comparamos o seu custo (c) com o maior pedágio já encontrado e armazenamos o valor máximo. Ao final do laço, o algoritmo retorna esse valor.

⚙️ Variação de Dijkstra Usada
Utilizamos o algoritmo de caminhos mínimos de Dijkstra com duas adaptações estratégicas na sua execução:Fila de Prioridade Indexada (Min-Heap): A extração do vértice de menor distância utiliza uma estrutura customizada IndexMinPQ, o que otimiza as operações de relaxamento das arestas.

Busca em Grafo Reverso: O algoritmo é executado duas vezes. A primeira partindo do vértice de origem S no grafo original, e a segunda partindo do vértice de destino T em um grafo reverso (com as direções das arestas invertidas, o que nos entrega otimizadamente o custo mínimo de qualquer nó v até o destino T).

## Como Executar
1. Vá a pasta do repositório
2. Compile o projeto:
   ```bash
   javac -d out src/*.java
   ```
3. Execute passando o arquivo de entrada:
   ```bash
   java .\src\Main.java
   ```
4. Use o ``dados/input.txt`` para usar como seu caso-teste e digite no terminal

📊 Análise de ComplexidadeComplexidade de Tempo:O(E log V)
Cada execução do algoritmo de Dijkstra com Min-Heap custa O(Elog V).
Como ele é executado duas vezes, temos uma constante multiplicativa que é ignorada na notação assintótica Big-O.
O laço final que itera sobre todas as arestas para validação da equação custa O(E). O termo logarítmico domina a equação final, resultando em O(E log V), mantendo a performance do programa totalmente dentro do tempo limite do juiz online, mesmo para o limite máximo de 100.000 arestas.
Complexidade de Espaço: O(V + E)
A representação do grafo original e do reverso em Listas de Adjacência ocupa um espaço na memória que é estritamente proporcional ao número de vértices e arestas reais. 
Somando isso aos arrays auxiliares de distâncias, a complexidade espacial é linear O(V + E), garantindo que o algoritmo não estoure o limite de memória (Memory Limit Exceeded).

