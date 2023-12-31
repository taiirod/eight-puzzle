import java.util.Arrays;

public class Nodo implements Comparable<Nodo> {

    protected int[][] estado;
    private final Integer numeroTotalVisitados;
    private final int pontuacaoG; //Função de Custo Real (g(n))
    private final int pontuacaoF; //Função de Custo Total (f(n))
    private final String direcao;

    public Nodo(int[][] estado, Integer numeroTotalVisitados, int pontuacaoG, int pontuacaoF, String direcao) {
        this.estado = estado;
        this.numeroTotalVisitados = numeroTotalVisitados;
        this.pontuacaoG = pontuacaoG;
        this.pontuacaoF = pontuacaoF;
        this.direcao = direcao;
    }

    public String getDirecao() {
        return direcao;
    }

    public Integer getNumeroTotalVisitados() {
        return numeroTotalVisitados;
    }

    @Override
    public int compareTo(Nodo other) {
        return Integer.compare(pontuacaoF, other.pontuacaoF);
    }

    @Override
    public String toString() {
        return Arrays.deepToString(estado);
    }
}