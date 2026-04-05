package controller;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import model.Damas;
import model.Piezas; 
import javafx.fxml.FXML;

public class TableroController {

    @FXML
    private GridPane root;

    private Damas modelo;

    @FXML
    private void initialize() {
        modelo = new Damas();
        inicializarPiezas();
    }


    // Para generar las piezas del tablero negras y blancas
    private void inicializarPiezas() {

        for (Node nodo : root.getChildren()) {

            if (nodo instanceof StackPane) {

                StackPane celda = (StackPane) nodo;

                Integer fila = GridPane.getRowIndex(celda);
                Integer col = GridPane.getColumnIndex(celda);

                if (fila == null)
                    fila = 0;
                if (col == null)
                    col = 0;

                // ir limpiando las celdas para evitar que se dupliquen las piezas al reiniciar
                // el juego
                celda.getChildren().removeIf(n -> n instanceof Circle);

                // Obtener la pieza del modelo en la posición correspondiente
                Piezas pieza = modelo.getPieza(fila, col);

                if (pieza != null) {

                    Circle circle = new Circle(25);

                    if (pieza.getColor().equals(Damas.BLANCO)) {
                        circle.setFill(Color.WHITE);
                    } else {
                        circle.setFill(Color.BLACK);
                    }
                    celda.getChildren().add(circle);
                }
            }
        }
    }

    

    // Para pasarle el color a cada pieza del tablero
    private Circle crearPieza(Color color) {
        Circle pieza = new Circle(25);
        pieza.setFill(color);
        return pieza;
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
