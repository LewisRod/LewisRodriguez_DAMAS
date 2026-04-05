package model;

public class Damas {


    //constantes para los colores de las piezas
    public static final String BLANCO = "BLANCO";
    public static final String NEGRO = "NEGRO";

    private Piezas[][] tablero;


    //constructor
    public Damas() {
        tablero = new Piezas[8][8];
        inicializar();
    }

    // Inicializar tablero
    private void inicializar() {

        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {

                // casillas oscuras
                if ((fila + col) % 2 != 0) {

                    // negras arriba
                    if (fila < 3) {
                        tablero[fila][col] = new Piezas(NEGRO);
                    }

                    // blancas abajo
                    else if (fila > 4) {
                        tablero[fila][col] = new Piezas(BLANCO);
                    }
                }
            }
        }
    }

    // getters
    public Piezas getPieza(int fila, int col) {
        return tablero[fila][col];
    }

    public Piezas[][] getTablero() {
        return tablero;
    }
}