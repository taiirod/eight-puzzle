import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {

    public static void printaResultadosEtempoDeExecucao(long finish, long start, List<Nodo> solucao) {
        final double executionTimeMillis = finish - start;

        if (solucao != null) {
            for (Nodo estado : solucao) {
                Utils.printEstado(estado);
            }
        } else {
            System.out.println("Não foi encontrada uma solução.");
        }

        System.out.println("Total de nodos visitados: " + solucao.get(0).getNumeroTotalVisitados());
        System.out.println("Número de passos: " + (solucao.size() - 1));
        System.out.println("Tempo de execução: " + executionTimeMillis / 1000 + "s");
    }


    // No método abaixo, é recuperado e reconstruído o caminho mais curto
    public static List<Nodo> reconstruindoCaminho(Map<String, String> nodoOrigem, Nodo atual, int size) {
        List<Nodo> caminho = new ArrayList<>();
        caminho.add(atual);

        while (nodoOrigem.containsKey(atual.toString())) {
            atual = estadoStr(nodoOrigem.get(atual.toString()), size);
            caminho.add(0, atual);
        }

        return caminho;
    }

    public static Nodo estadoStr(String str, int size) {
        int[][] estado = new int[3][3];
        String[] partes = str.split("\\], \\[");
        String direcao = "";

        direcao = Utils.getTextoMovimentoPeca(str, direcao);

        for (int i = 0; i < 3; i++) {
            String[] valores = partes[i].replaceAll("\\[|\\]", "").split(", ");
            for (int j = 0; j < 3; j++) {
                estado[i][j] = Integer.parseInt(valores[j]);
            }
        }

        return new Nodo(estado, size, 0, 0, direcao);
    }

    public static String getMovimentoDaPeca(int[][] vizinho, int movimentoVertical, int movimentoHorizontal, int linha, String direcao, int coluna) {
        int peca = vizinho[movimentoVertical][movimentoHorizontal];

        if (linha > movimentoVertical) {
            direcao = "Peça " + peca + " movida para baixo";
        } else if (linha < movimentoVertical) {
            direcao = "Peça " + peca + " movida para cima";
        } else if (coluna > movimentoHorizontal) {
            direcao = "Peça " + peca + " movida para direita";
        } else if (coluna < movimentoHorizontal) {
            direcao = "Peça " + peca + " movida para esquerda";
        }
        return direcao;
    }

    public static String getTextoMovimentoPeca(String str, String direcao) {
        if (str.contains("cima")) {
            direcao = str.substring(35, 58);
        } else if (str.contains("baixo")) {
            direcao = str.substring(35, 59);
        } else if (str.contains("esquerda")) {
            direcao = str.substring(35, 62);
        } else if (str.contains("direita")) {
            direcao = str.substring(35, 61);
        }
        return direcao;
    }

    public static void printEstado(Nodo nodo) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(nodo.estado[i][j] + " ");

            }
            System.out.println();
        }
        System.out.println(nodo.getDirecao());
        System.out.println("-------------------------");
    }

}
