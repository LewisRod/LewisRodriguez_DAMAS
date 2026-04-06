package controller;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import model.Damas;
import model.Piezas;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.List;

public class TableroController {

    @FXML
    private GridPane root;

    private List<int[]> movimientosValidos = new ArrayList<>();

    private Damas modelo;

    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;



    @FXML
    private void initialize() {
        modelo = new Damas();
        inicializarPiezas();
    }


    // Para generar las piezas del tablero negras y blancas
    @FXML
    private void inicializarPiezas() {

        for (Node nodo : root.getChildren()) {

            if (nodo instanceof StackPane) {

                StackPane celda = (StackPane) nodo;
                Rectangle rectangulo = (Rectangle) celda.getChildren().get(0);

                Integer filaTemp = GridPane.getRowIndex(celda);
                Integer colTemp = GridPane.getColumnIndex(celda);

                int fila;
                int col;

                if (filaTemp == null) {
                    fila = 0;
                } else {
                    fila = filaTemp;
                }

                if (colTemp == null) {
                    col = 0;
                } else {
                    col = colTemp;
                }

                celda.setOnMouseClicked(e -> clickCelda(fila, col)); // para los clicks en cada celda

                celda.getChildren().removeIf(n -> n instanceof Circle); /*
                                                                         * limpiar la celda antes de agregar la pieza,
                                                                         * para evitar duplicados al reiniciar el juego
                                                                         */

                Piezas pieza = modelo.getPieza(fila, col);

                if ((fila + col) % 2 == 0) {
                    rectangulo.setFill(Color.web("#b5a687"));
                } else {
                    rectangulo.setFill(Color.web("#855e32"));
                }

                // marca los movimientos validos de las piezas
                for (int[] movimiento : movimientosValidos) {
                    if (fila == movimiento[0] && col == movimiento[1]) {
                        rectangulo.setFill(Color.GREENYELLOW);
                    }
                }

                if (pieza != null) {

                    Circle circle = new Circle(25);

                    if (pieza.getColor().equals(Damas.BLANCO)) {
                        circle.setFill(Color.WHITE);
                    } else {
                        circle.setFill(Color.BLACK);
                    }

                    // para resaltar la pieza seleccionada
                    if (fila == filaSeleccionada && col == colSeleccionada) {
                        circle.setStroke(Color.GOLD);
                        circle.setStrokeWidth(4);
                    }

                    celda.getChildren().add(circle);
                }
            }
        }
    }



    // para el click en una celda del tablero
    private void clickCelda(int fila, int col) {

        Piezas pieza = modelo.getPieza(fila, col);

        if (filaSeleccionada == -1) {

            if (pieza != null) {
                filaSeleccionada = fila;
                colSeleccionada = col;

                calcularMovimientos(fila, col);
            }

        } else {

            // solo se mueve si esta en movimientos validos
            boolean esValido = false;

            for (int[] movimiento : movimientosValidos) {
                if (movimiento[0] == fila && movimiento[1] == col) {
                    esValido = true;
                    break;
                }
            }

            if (esValido) {
                modelo.mover(filaSeleccionada, colSeleccionada, fila, col);
            }

            filaSeleccionada = -1;
            colSeleccionada = -1;
            movimientosValidos.clear();
        }

        inicializarPiezas();
    }



    
    // calcular movimientos validos para la pieza seleccionada
    private void calcularMovimientos(int filaOrigen, int colOrigen) {

        movimientosValidos.clear();

        Piezas piezaSeleccionada = modelo.getPieza(filaOrigen, colOrigen);

        int[][] direccionesMovimiento;

        if (piezaSeleccionada.getColor().equals(Damas.BLANCO)) {
            direccionesMovimiento = new int[][] {{ -1, -1}, { -1, 1}};
        } else {
            direccionesMovimiento = new int[][] {{ 1, -1 }, { 1, 1 }};
        }

        for (int[] direccion : direccionesMovimiento) {

            int filaDestino = filaOrigen + direccion[0];
            int colDestino = colOrigen + direccion[1];

            // validar que este dentro del tablero
            if (filaDestino >= 0 && filaDestino < 8 && colDestino >= 0 && colDestino < 8) {

                // verificar que la casilla esta vacia
                if (modelo.getPieza(filaDestino, colDestino) == null) {
                    movimientosValidos.add(new int[] { filaDestino, colDestino });
                }
            }
        }
    }

    @FXML
    public void cerrarJuego() {

    }

    @FXML
    public void volverMenu() {
    }

    @FXML
    public void reiniciarJuego() {
    }

}
