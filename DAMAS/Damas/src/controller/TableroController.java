package controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import model.Damas;
import model.Piezas;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.*;

public class TableroController {

    @FXML
    private GridPane root; // para acceder a las celdas del tablero

    @FXML
    private Label lblTurno;

    @FXML
    private Label puntajeBlancas;

    @FXML
    private Label puntajeNegras;

    @FXML
    private VBox panelVictoria;

    @FXML
    private Label lblGanador;

    @FXML
    private Label lblHistorialBlancas;

    @FXML
    private Label lblHistorialNegras;

    // lista que guarda la posicion de los movimientos validos para la pieza
    // seleccionada
    private List<int[]> movimientosValidos = new ArrayList<>();

    private Damas modelo;

    private int filaSeleccionada = -1;

    private int colSeleccionada = -1;

    private boolean enMulticaptura = false;

    private int totalBlancas = 0;

    private int totalNegras = 0;





    // metodo para cargar el tablero y poner las piezas en sus posiciones iniciales,
    // ademas de cargar el historial de victorias
    @FXML
    private void initialize() {
        modelo = new Damas();
        actualizarTablero();
        cargarHistorial();
    }




    // Este metodo va actualizando el tablero segun el estado actual del juego.
    @FXML
    private void actualizarTablero() {

        // recorre todos los elementos del tablero (StackPane) para colocar las piezas y
        // marcar los movimientos validos
        for (Node nodo : root.getChildren()) {

            // filtra solo los StackPane (celdas del tablero)
            if (nodo instanceof StackPane) {

                StackPane celda = (StackPane) nodo;
                Rectangle rectangulo = (Rectangle) celda.getChildren().get(0);

                Integer filaTemp = GridPane.getRowIndex(celda);
                Integer colTemp = GridPane.getColumnIndex(celda);

                int fila;
                int col;

                // para el manejo de filas y columnas nulas (caso borde) se asigna 0 por defecto
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

                // cuando se hafa clic en una celda, se llama al metodo clickCelda y maneje la
                // jugada
                celda.setOnMouseClicked(e -> clickCelda(fila, col));

                // limpia la celda de piezas (circulos) para actualizar el tablero
                celda.getChildren().removeIf(n -> n instanceof Circle);

                Piezas pieza = modelo.getPieza(fila, col);

                // pinta el tablero con los colores
                if ((fila + col) % 2 == 0) {
                    rectangulo.setFill(Color.web("#f0ead2"));
                } else {
                    rectangulo.setFill(Color.web("#344e41"));
                }

                // para marcar los movimientos validos
                for (int[] mov : movimientosValidos) {
                    if (fila == mov[0] && col == mov[1]) {
                        rectangulo.setFill(Color.GREENYELLOW);
                    }
                }

                // si hay una pieza en esa celda, ejecuta el bloque para dibujarla
                if (pieza != null) {

                    Circle circle = new Circle(25);

                    if (pieza.getColor().equals(Damas.BLANCO)) {
                        circle.setFill(Color.WHITE);
                    } else {
                        circle.setFill(Color.BLACK);
                    }

                    // para marcar las piezas que son damas
                    if (pieza.getEsDama()) {

                        // borde dorado de las damas
                        circle.setStroke(Color.web("#ffd700"));
                        circle.setStrokeWidth(3);

                        celda.getChildren().add(circle);

                        // coronita para las piezas que son damas
                        Circle corona = new Circle(8);
                        corona.setFill(Color.web("#ffd900e8"));
                        corona.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.5)));
                        corona.setTranslateY(-15);

                        celda.getChildren().add(corona);

                    } else {
                        celda.getChildren().add(circle);
                    }

                    // para resaltar la pieza seleccionada
                    if (fila == filaSeleccionada && col == colSeleccionada) {
                        circle.setStroke(Color.GOLD);
                        circle.setStrokeWidth(4);
                    }
                }

            }
        }

        // para mostrar de quien es el turno
        if (modelo.getTurno().equals(Damas.BLANCO)) {
            lblTurno.setText("Turno: Blancas");
        } else {
            lblTurno.setText("Turno: Negras");
        }

        // para ir actualizando el puntaje
        puntajeBlancas.setText(String.valueOf(modelo.getPuntajeBlancas()));
        puntajeNegras.setText(String.valueOf(modelo.getPuntajeNegras()));
    }






    // para manejar la logica de cada jugada al hacer click en una celda
    private void clickCelda(int fila, int col) {

        Piezas pieza = modelo.getPieza(fila, col);

        // si ya hay un ganador, no se permiten mas movimientos
        if (modelo.obtenerGanador() != null)
            return;

        // si no hay una pieza seleccionada
        if (filaSeleccionada == -1) {

            if (pieza != null && pieza.getColor().equals(modelo.getTurno())) {

                // si hay captura disponible en una jugada, es el unico movimiento permitido.
                if (modelo.hayCapturaDisponible(modelo.getTurno()) &&
                        !modelo.puedeCapturar(fila, col)) {

                    Alert alerta = new Alert(AlertType.WARNING);
                    alerta.setTitle("Movimiento invalido");
                    alerta.setHeaderText(null);
                    alerta.setContentText("Hay captura disponible.");

                    alerta.showAndWait();
                    return;
                }

                // se calculan los movimientos validos para la pieza seleccionada
                filaSeleccionada = fila;
                colSeleccionada = col;

                calcularMovimientos(fila, col);
            }

        } else {

            // verifica si el moviemiento seleccionado es valido
            boolean esValido = false;

            for (int[] mov : movimientosValidos) {
                if (mov[0] == fila && mov[1] == col) {
                    esValido = true;
                    break;
                }
            }

            // si el movimiento es valido, se ejecuta el movimiento y se verifica si hubo
            // captura para permitir multicaptura
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
                    actualizarTablero();
                    return;
                }

                modelo.cambiarTurno();
                enMulticaptura = false;

            }

            // se resetea la seleccion y los movimientos validos para la siguiente jugada
            filaSeleccionada = -1;
            colSeleccionada = -1;
            movimientosValidos.clear();
        }

        // se actualiza el tablero con los cambios realizados
        actualizarTablero();

        // para verificar si hay un ganador despues de cada movimiento
        String ganador = modelo.obtenerGanador();

        if (ganador != null) {
            mostrarVictoria(ganador);
        }
    }





    // aqui se calculan los movimientos validos cuando se selecciona una pieza
    private void calcularMovimientos(int filaOrigen, int colOrigen) {

        // para borrar los movimientos validos de la jugada anterior
        movimientosValidos.clear();

        Piezas pieza = modelo.getPieza(filaOrigen, colOrigen);

        if (pieza == null)
            return;

        // para almacenar las direcciones donde puede moverse una pieza
        int[][] direcciones;

        // direcciones segun tipo de pieza
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

        for (int[] d : direcciones) {

            int filaMedio = filaOrigen + d[0];
            int colMedio = colOrigen + d[1];

            // se multiplica por 2 para dar el salto de la captura
            int filaDestino = filaOrigen + d[0] * 2;
            int colDestino = colOrigen + d[1] * 2;

            if (filaDestino >= 0 && filaDestino < 8 && colDestino >= 0 && colDestino < 8) {

                if (modelo.getPieza(filaDestino, colDestino) == null) {

                    if (filaMedio < 0 || filaMedio >= 8 || colMedio < 0 || colMedio >= 8)
                        continue;

                    Piezas enemigo = modelo.getPieza(filaMedio, colMedio);

                    if (enemigo != null && !enemigo.getColor().equals(pieza.getColor())) {

                        movimientosValidos.add(new int[] { filaDestino, colDestino });
                        hayCaptura = true;
                    }
                }
            }
        }

        // si no hay captura disponible, se calculan los movimientos normales
        if (!hayCaptura) {

            for (int[] d : direcciones) {

                int filaDestino = filaOrigen + d[0];
                int colDestino = colOrigen + d[1];

                if (filaDestino >= 0 && filaDestino < 8 && colDestino >= 0 && colDestino < 8) {

                    if (modelo.getPieza(filaDestino, colDestino) == null) {
                        movimientosValidos.add(new int[] { filaDestino, colDestino });
                    }
                }
            }
        }
    }






    // guarda el historial de victorias de cada jugador en archivo
    private void guardarResultado(String ganador) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("historial.txt", true))) {

            bw.write(ganador);
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (ganador.equals("Blancas")) {
            totalBlancas++;
        } else {
            totalNegras++;
        }

        actualizarHistorial();
    }





    // carga el historial de victorias desde el archivo al iniciar el juego
    private void cargarHistorial() {

        totalBlancas = 0;
        totalNegras = 0;

        File archivo = new File("historial.txt");

        if (!archivo.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.equals("Blancas")) {
                    totalBlancas++;
                } else if (linea.equals("Negras")) {
                    totalNegras++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        actualizarHistorial();
    }






    private void actualizarHistorial() {
        lblHistorialBlancas.setText(" Blancas: " + totalBlancas);
        lblHistorialNegras.setText("Negras: " + totalNegras);
    }





    @FXML
    public void cerrarJuego() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar salida");
        alert.setHeaderText("Estas seguro de que quieres salir del juego?");
        alert.setContentText("Tu progreso se perdera.");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.close();
        }
    }





    private void mostrarVictoria(String ganador) {

        lblGanador.setText("🏆 " + ganador + " GANA 🏆");

        panelVictoria.setOpacity(0);
        panelVictoria.setVisible(true);

        FadeTransition ft = new FadeTransition(Duration.millis(400), panelVictoria);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        guardarResultado(ganador);
    }





    @FXML
    private void volverMenu(ActionEvent event) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar salida");
        alert.setHeaderText("Estas seguro de que quieres volver al menu?");
        alert.setContentText("Tu progreso se perdera.");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/menu.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) this.root.getScene().getWindow();
            stage.setScene(scene);
        }
    }



    

    @FXML
    private void reiniciarJuego() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar reinicio");
        alert.setHeaderText("Estas seguro de que quieres reiniciar el juego?");
        alert.setContentText("Tu progreso se perdera.");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            modelo = new Damas();
            filaSeleccionada = -1;
            colSeleccionada = -1;
            movimientosValidos.clear();
            panelVictoria.setVisible(false);
            actualizarTablero();
        }
    }
}