package controller;

import javafx.scene.Node;
import javafx.scene.control.Label;
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

    @FXML
    private Label lblTurno;

    @FXML
    private Label puntajeBlancas;

    @FXML
    private Label puntajeNegras;

    private List<int[]> movimientosValidos = new ArrayList<>();

    private Damas modelo;

    private int filaSeleccionada = -1;
    private int colSeleccionada = -1;

    private boolean enMulticaptura = false;

    @FXML
    private void initialize() {
        modelo = new Damas();
        inicializarPiezas();
    }

    // Inicializar las piezas en el tablero con sus posiciones iniciales
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

                celda.setOnMouseClicked(e -> clickCelda(fila, col));

                celda.getChildren().removeIf(n -> n instanceof Circle);

                Piezas pieza = modelo.getPieza(fila, col);

                if ((fila + col) % 2 == 0) {
                    rectangulo.setFill(Color.web("#b5a687"));
                } else {
                    rectangulo.setFill(Color.web("#855e32"));
                }

                // para marcar los movimientos validos
                for (int[] mov : movimientosValidos) {
                    if (fila == mov[0] && col == mov[1]) {
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

                    // para marcar las piezas que son damas
                    if (pieza.getEsDama()) {
                        circle.setStroke(Color.RED);
                        circle.setStrokeWidth(6);
                    }

                    if (fila == filaSeleccionada && col == colSeleccionada) {
                        circle.setStroke(Color.GOLD);
                        circle.setStrokeWidth(4);
                    }

                    celda.getChildren().add(circle);
                }
            }
        }

        // para mostrar en el panel izquierdo el turno actual
        if (modelo.getTurno().equals(Damas.BLANCO)) {
            lblTurno.setText("Turno: Blancas");
        } else {
            lblTurno.setText("Turno: Negras");
        }

        // para llevar el conteo de las piezas capturadas
        puntajeBlancas.setText(String.valueOf(modelo.getPuntajeBlancas()));
        puntajeNegras.setText(String.valueOf(modelo.getPuntajeNegras()));
    }

    private void clickCelda(int fila, int col) {

        Piezas pieza = modelo.getPieza(fila, col);

        if (filaSeleccionada == -1) {

            if (pieza != null && pieza.getColor().equals(modelo.getTurno())) {

                // captura obligatoria
                if (modelo.hayCapturaDisponible(modelo.getTurno()) &&
                        !modelo.puedeCapturar(fila, col)) {

                    System.out.println("Debes capturar");
                    return;
                }

                filaSeleccionada = fila;
                colSeleccionada = col;

                calcularMovimientos(fila, col);
            }

        } else {

            boolean esValido = false;

            for (int[] mov : movimientosValidos) {
                if (mov[0] == fila && mov[1] == col) {
                    esValido = true;
                    break;
                }
            }

            if (esValido) {

                boolean fueCaptura = (fila - filaSeleccionada == 2) || (fila - filaSeleccionada == -2);

                modelo.mover(filaSeleccionada, colSeleccionada, fila, col);

                // si hubo captura, verifica si hay multicaptura disponible para la pieza que
                // acaba de mover
                if (fueCaptura && modelo.puedeCapturar(fila, col)) {

                    enMulticaptura = true;

                    filaSeleccionada = fila;
                    colSeleccionada = col;

                    calcularMovimientos(fila, col);
                    inicializarPiezas();
                    return;
                }

                modelo.cambiarTurno();
                enMulticaptura = false;
            }

            filaSeleccionada = -1;
            colSeleccionada = -1;
            movimientosValidos.clear();
        }

        inicializarPiezas();
    }

    private void calcularMovimientos(int filaOrigen, int colOrigen) {

        movimientosValidos.clear();

        Piezas pieza = modelo.getPieza(filaOrigen, colOrigen);

        if (pieza == null)
            return;

        boolean hayCapturaGlobal = modelo.hayCapturaDisponible(modelo.getTurno());

        int[][] direcciones;

        /*
        para el manejo de los movimiento cuando una 
        pieza se vuelve dama, puede moverse hacia atras
        y hacia adelante */
        if (pieza.getEsDama()) {
            direcciones = new int[][] {
                    { -1, -1 }, { -1, 1 },
                    { 1, -1 }, { 1, 1 }
            };
        } else if (pieza.getColor().equals(Damas.BLANCO)) {
            direcciones = new int[][] { { -1, -1 }, { -1, 1 } };
        } else {
            direcciones = new int[][] { { 1, -1 }, { 1, 1 } };
        }

        boolean hayCaptura = false;

        // para calcular si hay capturas disponibles para esa pieza
        for (int[] direccion : direcciones) {

            int filaMedio = filaOrigen + direccion[0];
            int colMedio = colOrigen + direccion[1];

            int filaDestino = filaOrigen + direccion[0] * 2;
            int colDestino = colOrigen + direccion[1] * 2;

            if (filaDestino >= 0 && filaDestino < 8 && colDestino >= 0 && colDestino < 8) {

                if (modelo.getPieza(filaDestino, colDestino) == null) {

                    Piezas enemigo = modelo.getPieza(filaMedio, colMedio);

                    if (enemigo != null && !enemigo.getColor().equals(pieza.getColor())) {

                        movimientosValidos.add(new int[] { filaDestino, colDestino });
                        hayCaptura = true;
                    }
                }
            }
        }

        /*
         * si no hay capturas para esa pieza, y no hay capturas globales, entonces
         * calcula movimientos normales
         */
        if (!hayCaptura && !hayCapturaGlobal) {

            for (int[] direccion : direcciones) {

                int filaDestino = filaOrigen + direccion[0];
                int colDestino = colOrigen + direccion[1];

                if (filaDestino >= 0 && filaDestino < 8 && colDestino >= 0 && colDestino < 8) {

                    if (modelo.getPieza(filaDestino, colDestino) == null) {
                        movimientosValidos.add(new int[] { filaDestino, colDestino });
                    }
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