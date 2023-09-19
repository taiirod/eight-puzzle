import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class EightPuzzleTresHeuristicas {

    public static void main(String[] args) {
        final MatrizesInicioObjetivo result = MatrizesInicioObjetivo.getInicioObjetivo();

        final long start = System.currentTimeMillis();
        List<Nodo> solucao = resolvePuzzle(result.getInicial(), result.getObjetivo());
        final long finish = System.currentTimeMillis();

        Utils.printaResultadosEtempoDeExecucao(finish, start, solucao);
    }

    private static List<Nodo> resolvePuzzle(int[][] inicial, int[][] objetivo) {
        PriorityQueue<Nodo> listaAbertos = new PriorityQueue<>();
        Set<String> visitados = new HashSet<>();
        Map<String, String> nodoOrigem = new HashMap<>();
        Map<String, Integer> pontuacaoG = new HashMap<>();

        Nodo nodoInicial = new Nodo(inicial, 0, 0, heuristicaPecasForaDoLugar(inicial, objetivo), "");
        listaAbertos.add(nodoInicial);
        pontuacaoG.put(nodoInicial.toString(), 0);

        while (!listaAbertos.isEmpty()) {
            Nodo nodoAtual = listaAbertos.poll();

            if (Arrays.deepEquals(nodoAtual.estado, objetivo)) {
                return Utils.reconstruindoCaminho(nodoOrigem, nodoAtual, visitados.size());
            }

            visitados.add(nodoAtual.toString());

            List<Nodo> vizinhos = getVizinhos(nodoAtual);

            for (Nodo vizinho : vizinhos) {
                String vizinhosStr = Arrays.deepToString(vizinho.estado);
                if (visitados.contains(vizinhosStr)) {
                    continue;
                }

                // Pontuação G é referente a profundidade
                // Abaixo
                int tempPontuacaoG = pontuacaoG.get(nodoAtual.toString()) + 1;

                // A verificação abaixo será feita para garantir que não serão criados nodos repetidos na fronteira
                if (!pontuacaoG.containsKey(vizinhosStr) || tempPontuacaoG < pontuacaoG.get(vizinhosStr)) {
                    nodoOrigem.put(vizinhosStr, nodoAtual + ", " + nodoAtual.getDirecao());
                    pontuacaoG.put(vizinhosStr, tempPontuacaoG);
                    int pontuacaoF = tempPontuacaoG +
                        heuristicaPecasForaDoLugar(vizinho.estado, objetivo) +
                        heuristicaDistanciaManhattan(vizinho.estado, objetivo) +
                        heuristicaInversoes(vizinho.estado, objetivo);
                    Nodo nodoVizinho = new Nodo(vizinho.estado, 0, tempPontuacaoG, pontuacaoF, vizinho.getDirecao());
                    listaAbertos.add(nodoVizinho);
                }
            }
        }
        return null;
    }

    // Heuristica 1 - Número de peças fora do lugar.
    private static int heuristicaPecasForaDoLugar(int[][] estado, int[][] objetivo) {
        int contador = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (estado[i][j] != objetivo[i][j]) {
                    contador++;
                }
            }
        }
        return contador;
    }

    // Heuristica 2 - Soma das distancias Manhattan
    private static int heuristicaDistanciaManhattan(int[][] estado, int[][] objetivo) {
        int somaDasDistancias = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int valor = estado[i][j];
                if (valor != 0) {
                    int linhaObjetivo = (valor - 1) / 3;
                    int colunaObjetivo = (valor - 1) % 3;
                    somaDasDistancias += Math.abs(i - linhaObjetivo) + Math.abs(j - colunaObjetivo);
                }
            }
        }
        return somaDasDistancias;
    }

    //Método que retorna o valor para a heuristica 3
    private static int heuristicaInversoes(int[][] estado, int[][] objetivo) {
        int contador = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i > 0) {
                    if (checkLeft(estado, objetivo, i, j)) {
                        contador += 1;
                    }
                }
                if (i < 2) {
                    if (checkRight(estado, objetivo, i, j)) {
                        contador += 1;
                    }
                }
                if (j > 0) {
                    if (checkUp(estado, objetivo, i, j)) {
                        contador += 1;
                    }
                }
                if (j < 2) {
                    if (checkDown(estado, objetivo, i, j)) {
                        contador += 1;
                    }
                }
            }
        }
        return contador;
    }

    //checa se há um tile reversal com o quadrado a esquerda
    private static Boolean checkLeft(int[][] estado, int[][] objetivo, int i, int j) {
        return estado[i][j] == objetivo[i - 1][j] && estado[i - 1][j] == objetivo[i][j] && estado[i][j] != 0 && estado[i - 1][j] != 0;
    }

    //checa se há um tile reversal com o quadrado a direita
    private static Boolean checkRight(int[][] estado, int[][] objetivo, int i, int j) {
        return estado[i][j] == objetivo[i + 1][j] && estado[i + 1][j] == objetivo[i][j] && estado[i][j] != 0 && estado[i + 1][j] != 0;
    }

    //checa se há um tile reversal com o quadrado acima
    private static Boolean checkUp(int[][] estado, int[][] objetivo, int i, int j) {
        return estado[i][j] == objetivo[i][j - 1] && estado[i][j - 1] == objetivo[i][j] && estado[i][j] != 0 && estado[i][j - 1] != 0;
    }

    //checa se há um tile reversal com o quadrado abaixo
    private static Boolean checkDown(int[][] estado, int[][] objetivo, int i, int j) {
        return estado[i][j] == objetivo[i][j + 1] && estado[i][j + 1] == objetivo[i][j] && estado[i][j] != 0 && estado[i][j + 1] != 0;
    }

    private static List<Nodo> getVizinhos(Nodo estado) {
        List<Nodo> vizinhos = new ArrayList<>();
        int[] direcaoLinha = {-1, 1, 0, 0};
        int[] direcaoColuna = {0, 0, -1, 1};

        int linha = -1, coluna = -1;

        // for abaixo tem a função de localizar a posição do espaço vazio, definindo valores de linha e coluna onde está o 0 atualmente
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (estado.estado[i][j] == 0) {
                    linha = i;
                    coluna = j;
                    break;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            int movimentoVertical = linha + direcaoLinha[i];
            int movimentoHorizontal = coluna + direcaoColuna[i];
            String direcao = "";

            // Aqui é feita uma verificação para certificar que o índice será válido
            if (movimentoVertical >= 0 && movimentoVertical < 3 && movimentoHorizontal >= 0 && movimentoHorizontal < 3) {
                int[][] vizinho = new int[3][3];
                for (int k = 0; k < 3; k++) {
                    vizinho[k] = Arrays.copyOf(estado.estado[k], 3);
                }

                direcao = Utils.getMovimentoDaPeca(vizinho, movimentoVertical, movimentoHorizontal, linha, direcao, coluna);

                //Na sequencia abaixo, é onde acontece o movimento das peças.
                // Aqui, é onde será movimentada a peça para o espaço vazio
                vizinho[linha][coluna] = vizinho[movimentoVertical][movimentoHorizontal];

                // Aqui, é para onde o espaço vazio irá
                vizinho[movimentoVertical][movimentoHorizontal] = 0;

                vizinhos.add(new Nodo(vizinho, 0, 0, 0, direcao));
            }
        }
        return vizinhos;
    }
}