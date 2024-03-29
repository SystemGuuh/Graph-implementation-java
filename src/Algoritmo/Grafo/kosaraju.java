package Algoritmo.Grafo;

import Algoritmo.Grafo.*;
import java.util.Stack;
import java.util.Iterator;

public class kosaraju<T> {

    /*VARIÁVEIS USADAS:*/
    int p; //quantidade de vertices
    int cont = 0; //quantidade de vertices fortemente conectados
    boolean[] visitados; //array com vertices visitados

    Grafo<T> grafo; //grafo atual
    Grafo<T> gf = new Grafo<T>(); //grafo auxiliar
    Grafo<String> gr = new Grafo<String>(); //Grafo para fortemente conectados

    String resposta = ""; //recebe a resposta dos vértices fortemente conectados
    Stack pilha = new Stack(); // Pilha
    Iterator<T> it; //Iterador para loops


    //Construtor da classe
    public kosaraju(Grafo<T> grafo, int rep) {
        this.grafo = grafo;
        this.p = rep;
        visitados = new boolean[p];
    }

    /*MÉTODOS*/
    //Monta o grafo fortemente conectado

    //cria as arestas dos grafo fortemente conectado **DIFICULDADE**
    public void criaGrafoForte() {

        //Criando conexões do grafo fortemente conectado
        for (int i = 0; i < p; i++) { //para cada vértice do grafo principal
            for (int j = 0; j < gr.vertices.size(); j++) { //pra cada vértice do grafo de componentes fortemente conectados
                if (gr.vertices.get(j).dado.contains((CharSequence) grafo.vertices.get(i).dado)) {
                    for (int z = 0; z < grafo.vertices.get(i).arestasSaida.size(); z++) {
                        if (!(gr.vertices.get(j).dado.contains((CharSequence) grafo.vertices.get(i).arestasSaida.get(z).fim.dado))) {
                            for (int s = 0; s < gr.vertices.size(); s++) {
                                if (gr.vertices.get(s).dado.contains((CharSequence) grafo.vertices.get(i).arestasSaida.get(z).fim.dado)) {
                                        gr.adicionarAresta(gr.vertices.get(j).dado, gr.vertices.get(s).dado);
                                    //gr.vertices.get(s).dado -> dado da saída
                                    //gr.vertices.get(j).dado -> dado do entrada

                                }
                            }
                        }
                    }
                }

            }
        }

        int g=0;
        //loop para tratamento de erro
        for (int j = 0; j < gr.vertices.size(); j++) {
            for (int i=0; i< gr.vertices.get(j).arestasSaida.size(); i++){
                for (int k=0; k< gr.vertices.get(j).arestasSaida.size(); k++){
                    if(gr.vertices.get(j).arestasSaida.get(i).fim.dado.equals(gr.vertices.get(j).arestasSaida.get(k).fim.dado) && i!=k){
                     g++;
                     if(g!=0){
                        gr.vertices.get(j).arestasSaida.remove(k);
                        g=0;
                     }
                    }
                }
            }

        }

    }

    //Imprime a repsosta identada
    public void resposta(){
        System.out.println("");

        //Checa se o grafo é fortemente conexo, atravez da contagem de vértices fortemente conectados
        if(cont == 1) System.out.println("Sim");
        else System.out.println("Não");

        System.out.println(cont);
        System.out.println(resposta);
        System.out.println("");

        criaGrafoForte();
        gr.imprimeBonito();;
    }

    //Cria uma cópia do grafo original
    public void copiaGrafo(){
        for(int i=0; i<p; i++){
            gf.adicionarVertice(grafo.vertices.get(i).dado);
            gf.vertices.get(i).posicao = i;
        }

    }

    //Gera o transposto no grafo que copiamos
    Grafo<T> getTransposto(Grafo<T> gf) {
        for (int v = 0; v < p; v++) {
            int n = grafo.vertices.get(v).getArestasSaida().size();
            while(n>0){
                gf.adicionarAresta(grafo.vertices.get(v).arestasSaida.get(n-1).fim.dado, grafo.vertices.get(v).getDado());
                n--;
            }

        }
        return gf;
    }

    //Preenche a pilha com os valores do grafo original
    void preencher(int i, Stack pilha){
        visitados[i] = true;

        //int i = grafo.vertices.get(k).getArestasSaida().size();
        it = (Iterator<T>) grafo.vertices.get(i).arestasSaida.iterator();
        int n = grafo.vertices.get(i).getArestasSaida().size();
        while(it.hasNext()){
            it.next();
            int posicao = grafo.vertices.get(i).arestasSaida.get(n-1).getFim().posicao;
            if(!visitados[posicao]) preencher(posicao, pilha);
            n--;
        }
        pilha.push(grafo.vertices.get(i).dado);
    }

    // Função recursiva para imprimir vértices fortemente conectados
    void DFS(int v, Stack stack){
        // Marco o nó como visitado
        visitados[v] = true;
        resposta = resposta+gf.vertices.get(v).getDado();
        int m; //variável pra receber as posições das arestas de sáida

        // Recursão para os adjacentes
        int n = gf.vertices.get(v).getArestasSaida().size();
        while(n>0){
            m = gf.vertices.get(v).arestasSaida.get(n-1).getFim().posicao;
            if (!visitados[m]) {
                DFS(m, stack);
            }
            n--;
        }
    }

    //printa a ordenação topológica
    public void printOT(){
        Stack stack = new Stack(); //cria uma pilha vazia
        int V =  p; //V = a quantidade de vértices

        // Marca todos os vértices como não visitados para o preencher
        for (int i = 0; i < V; i++){
            visitados[i] = false;
        }

        //dando posições para os vertice, assim nos localizamos em relação a pilha
        for (int i=0; i<V; i++){
            grafo.vertices.get(i).posicao = i;
        }

        // Coloca os vértices na pilha de acordo com o final
        for (int i = 0; i < V; i++) {
            if (!visitados[i]) {
                preencher(i, stack);
            }
        }

        // Grafo reverso
        copiaGrafo();
        gf = getTransposto(gf);
        // Volta a lista de visitados para falso, usaremos no segundo DFS
        for (int i = 0; i < V; i++) visitados[i] = false;

        // Agora imprimimos na ordem da pilha
        while (!stack.empty())
        {

            // Retira um valor da pilha
           int v = (p-stack.size());
           stack.pop();

            // Printa o componente fortemente conectado
            if (!visitados[v])
            {
                DFS(v, stack);

                resposta = resposta + " ";
                String[] vertice = resposta.split(" ");
                gr.adicionarVertice(vertice[cont]);
                cont++;

            }
        }

        resposta();
    }
}
