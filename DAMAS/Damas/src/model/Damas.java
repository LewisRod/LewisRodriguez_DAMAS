package model;

public class Damas {

    // constantes para los colores de las piezas
    public static final String BLANCO = "BLANCO";
    public static final String NEGRO = "NEGRO";

    private Piezas[][] tablero;

    // constructor
    public Damas() {
        tablero = new Piezas[8][8];
        inicializar();
    }

    // getters
    public Piezas getPieza(int fila, int col) {
        return tablero[fila][col];
    }

    public Piezas[][] getTablero() {
        return tablero;
    }


    
    // inicializar el tablero
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



    // calcular movimientos posibles de cada pieza
    public boolean mover(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {

        Piezas pieza = tablero[filaOrigen][colOrigen];

        if (pieza == null)
            return false;

        // si la casilla destino esta ocupada
        if (tablero[filaDestino][colDestino] != null)
            return false;

        int diferenciaFila = filaDestino - filaOrigen;
        int diferenciaCol = colDestino - colOrigen;

        if ((diferenciaFila == 1 || diferenciaFila == -1) &&
                (diferenciaCol == 1 || diferenciaCol == -1)) {

            if ((pieza.getColor().equals(BLANCO) && diferenciaFila == -1) ||
                    (pieza.getColor().equals(NEGRO) && diferenciaFila == 1)) {

                tablero[filaDestino][colDestino] = pieza;
                tablero[filaOrigen][colOrigen] = null;

                return true;
            }
        }
        return false;
    }

}