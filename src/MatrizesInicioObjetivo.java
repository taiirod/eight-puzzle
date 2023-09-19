public class MatrizesInicioObjetivo {

    protected int[][] inicial;
    protected int[][] objetivo;

    public MatrizesInicioObjetivo(int[][] inicial, int[][] objetivo) {
        this.inicial = inicial;
        this.objetivo = objetivo;
    }

    public static MatrizesInicioObjetivo getInicioObjetivo() {
        int[][] inicial = {
            {8, 5, 1}, //linha 0
            {0, 6, 7}, //linha 1
            {2, 4, 3} // linha 2
        };

        int[][] objetivo = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        };
        MatrizesInicioObjetivo matrizesInicioObjetivo = new MatrizesInicioObjetivo(inicial, objetivo);
        return matrizesInicioObjetivo;
    }

    public int[][] getInicial() {
        return inicial;
    }

    public int[][] getObjetivo() {
        return objetivo;
    }


}
