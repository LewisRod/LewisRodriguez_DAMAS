package model;

public class Damas {

    // constantes para los colores de las piezas
    public static final String BLANCO = "BLANCO";
    public static final String NEGRO = "NEGRO";

    private Piezas[][] tablero;
    private String turno;

    private int puntajeBlancas = 0;
    private int puntajeNegras = 0;

    // constructor
    public Damas() {
        tablero = new Piezas[8][8];
        turno = BLANCO;
        inicializar();
    }

    // getters
    public Piezas getPieza(int fila, int col) {
        return tablero[fila][col];
    }

    public Piezas[][] getTablero() {
        return tablero;
    }

    public String getTurno() {
        return turno;
    }

    public int getPuntajeBlancas() {
        return puntajeBlancas;
    }

    public int getPuntajeNegras() {
        return puntajeNegras;
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

    // permite mover una pieza de una posicion a otra
    public boolean mover(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {

        Piezas pieza = tablero[filaOrigen][colOrigen];

        if (pieza == null)
            return false;

        if (!pieza.getColor().equals(turno))
            return false;

        if (tablero[filaDestino][colDestino] != null)
            return false;

        int diferenciaFila = filaDestino - filaOrigen;
        int diferenciaCol = colDestino - colOrigen;

        // para movimiento normal
        if ((diferenciaFila == 1 || diferenciaFila == -1) && (diferenciaCol == 1 || diferenciaCol == -1)) {

            if ((pieza.getColor().equals(BLANCO) && diferenciaFila == -1) ||
                    (pieza.getColor().equals(NEGRO) && diferenciaFila == 1)) {

                tablero[filaDestino][colDestino] = pieza;
                tablero[filaOrigen][colOrigen] = null;

                return true;
            }
        }


        
        // para la captura (salto de 2 casillas)
        if ((diferenciaFila == 2 || diferenciaFila == -2) && (diferenciaCol == 2 || diferenciaCol == -2)) {

            int filaMedio = (filaOrigen + filaDestino) / 2;
            int colMedio = (colOrigen + colDestino) / 2;

            Piezas piezaEnemiga = tablero[filaMedio][colMedio];

            // verificar que hay pieza enemiga en medio
            if (piezaEnemiga != null && !piezaEnemiga.getColor().equals(pieza.getColor())) {

                if ((pieza.getColor().equals(BLANCO) && diferenciaFila == -2) ||
                        (pieza.getColor().equals(NEGRO) && diferenciaFila == 2)) {

                    tablero[filaDestino][colDestino] = pieza;
                    tablero[filaOrigen][colOrigen] = null;

                    // se elimina la pieza capturada
                    tablero[filaMedio][colMedio] = null;

                    // sumar al puntaje
                    if (pieza.getColor().equals(BLANCO)) {
                        puntajeBlancas++;
                    } else {
                        puntajeNegras++;
                    }

                    return true;
                }
            }
        }

        return false;
    }


    public boolean puedeCapturar(int fila, int col) {

        Piezas pieza = tablero[fila][col];

        int[][] direcciones;

        if (pieza == null)
            return false;

        if (pieza.getColor().equals(BLANCO)) {
            direcciones = new int[][] { { -1, -1 }, { -1, 1 } };
        } else {
            direcciones = new int[][] { { 1, -1 }, { 1, 1 } };
        }

        for (int[] direccion : direcciones) {

            int filaMedio = fila + direccion[0];
            int colMedio = col + direccion[1];

            int filaDestino = fila + direccion[0] * 2;
            int colDestino = col + direccion[1] * 2;

            if (filaDestino >= 0 && filaDestino < 8 && colDestino >= 0 && colDestino < 8) {

                if (tablero[filaDestino][colDestino] == null) {

                    if (filaMedio < 0 || filaMedio >= 8 || colMedio < 0 || colMedio >= 8)
                        continue;

                    Piezas enemigo = tablero[filaMedio][colMedio];

                    if (enemigo != null && !enemigo.getColor().equals(pieza.getColor())) {

                        return true;
                    }
                }
            }
        }

        return false;
    }



    public boolean hayCapturaDisponible(String color) {

        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {

                Piezas pieza = tablero[fila][col];

                if (pieza != null && pieza.getColor().equals(color)) {

                    if (puedeCapturar(fila, col)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public void cambiarTurno() {
        if (turno.equals(BLANCO)) {
            turno = NEGRO;
        } else {
            turno = BLANCO;
        }
    }

}