package model;

public class Damas {

    //constantes para los colores
    public static final String BLANCO = "BLANCO";
    public static final String NEGRO = "NEGRO";


    //tablero logico
    private Piezas[][] tablero;
    private String turno;

    private int puntajeBlancas = 0;
    private int puntajeNegras = 0;


    
    //Constructor con valores inicializados
    public Damas() {
        tablero = new Piezas[8][8];
        turno = BLANCO;
        inicializar();
    }



    //getters
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




    //para colocar las piezas en sus posiciones iniciales
    private void inicializar() {
        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {

                //posicionar en casilas oscuras
                if ((fila + col) % 2 != 0) {

                    //arriba van las negras, abajo las blancas
                    if (fila < 3) {
                        tablero[fila][col] = new Piezas(NEGRO);
                    } else if (fila > 4) {
                        tablero[fila][col] = new Piezas(BLANCO);
                    }
                }
            }
        }
    }




    
    public boolean mover(int filaOrigen, int colOrigen, int filaDestino, int colDestino) {

        Piezas pieza = tablero[filaOrigen][colOrigen];


        //validaciones para mover una pieza
        if (pieza == null)
             return false;

        if (!pieza.getColor().equals(turno))
             return false;

        if (tablero[filaDestino][colDestino] != null)
             return false;


        //determinar donde se movio una pieza
        int diferenciaFila = filaDestino - filaOrigen;
        int diferenciaCol = colDestino - colOrigen;


        //para verificar si una pieza realiza un movimiento en la direccion correcta
        if ((diferenciaFila == 1 || diferenciaFila == -1) && (diferenciaCol == 1 || diferenciaCol == -1)) {

            if (pieza.getEsDama() ||
                (pieza.getColor().equals(BLANCO) && diferenciaFila == -1) ||
                (pieza.getColor().equals(NEGRO) && diferenciaFila == 1)) {

                //mueve la pieza en el tablero logico y luego el controlador actualiza la interfaz para reflejar ese cambio.
                tablero[filaDestino][colDestino] = pieza;
                tablero[filaOrigen][colOrigen] = null;

                //para la coronacion de una pieza
                if (pieza.getColor().equals(BLANCO) && filaDestino == 0) {
                    pieza.setEsDama(true);
                }
                if (pieza.getColor().equals(NEGRO) && filaDestino == 7) {
                    pieza.setEsDama(true);
                }

                return true;
            }
        }



        if ((diferenciaFila == 2 || diferenciaFila == -2) &&
            (diferenciaCol == 2 || diferenciaCol == -2)) {

            //para calcular la posicion de la pieza a capturar
            int filaMedio = (filaOrigen + filaDestino) / 2;
            int colMedio = (colOrigen + colDestino) / 2;

            Piezas piezaEnemiga = tablero[filaMedio][colMedio];

            if (piezaEnemiga != null && !piezaEnemiga.getColor().equals(pieza.getColor())) {

                //ejecuta el movimiento
                tablero[filaDestino][colDestino] = pieza;
                tablero[filaOrigen][colOrigen] = null;

                //elimina la ficha capturada
                tablero[filaMedio][colMedio] = null;

                //se incrementa el puntaje de la pieza que captura
                if (pieza.getColor().equals(BLANCO)) {
                    puntajeBlancas++;
                } else {
                    puntajeNegras++;
                }

                // coronacion de pieza
                if (pieza.getColor().equals(BLANCO) && filaDestino == 0) {
                    pieza.setEsDama(true);
                }
                if (pieza.getColor().equals(NEGRO) && filaDestino == 7) {
                    pieza.setEsDama(true);
                }

                return true;
            }
        }

        return false;
    }






    public boolean puedeCapturar(int fila, int col) {

        Piezas pieza = tablero[fila][col];

        if (pieza == null)
             return false;


        //determinar las direcciones posibles para la captura
        int[][] direcciones;

        if (pieza.getEsDama()) {
            direcciones = new int[][] {
                    { -1, -1 }, { -1, 1 },
                    { 1, -1 }, { 1, 1 }
            };
        } else if (pieza.getColor().equals(BLANCO)) {
            direcciones = new int[][] { { -1, -1 }, { -1, 1 } };
        } else {
            direcciones = new int[][] { { 1, -1 }, { 1, 1 } };
        }


        for (int[] d : direcciones) {

            //determinar donde esta la pieza a capturar
            int filaMedio = fila + d[0];
            int colMedio = col + d[1];

            //calcular la posicion de destino despues de la captura
            int filaDestino = fila + d[0] * 2;
            int colDestino = col + d[1] * 2;


            //validar que la posicion de destino este dentro del tablero y que la casilla este vacia
            if (filaDestino >= 0 && filaDestino < 8 && colDestino >= 0 && colDestino < 8) {

                if (tablero[filaDestino][colDestino] == null) {

                    //validar que la pieza a capturar sea del color contrario y que este dentro del tablero
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



    public String obtenerGanador() {

        int blancas = 0;
        int negras = 0;

        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {
                if (tablero[fila][col] != null) {
                    if (tablero[fila][col].getColor().equals(BLANCO)) {
                        blancas++;
                    } else {
                        negras++;
                    }
                }
            }
        }

        if (blancas == 0) return "NEGRAS";
        if (negras == 0) return "BLANCAS";

        return null;
    }
}