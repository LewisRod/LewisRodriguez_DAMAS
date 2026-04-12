package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import model.Damas;


public class MenuController {

    // para mover a la pantalla del tablero
    @FXML
    public void irJuego(javafx.scene.input.MouseEvent event) throws Exception {

        //usamos un node para obtener el nodo del boton que se hizo click, para luego obtener la ventana y cambiar la escena
        Node nodo = (Node) event.getSource();

        // para darle animacion al boton
        ScaleTransition click = new ScaleTransition(Duration.millis(100), nodo);
        click.setToX(0.9);
        click.setToY(0.9);

        click.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/tablero.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) nodo.getScene().getWindow();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.setMaximized(true);
                stage.show();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        click.play();
    }




    //para hacer el efecto de agrandar el boton al pasar el mouse por encima
    @FXML
    private void hoverIniciar(javafx.scene.input.MouseEvent e) {

        ScaleTransition st = new ScaleTransition(Duration.millis(200), (Node) e.getSource());
        st.setToX(1.1);
        st.setToY(1.1);
        st.play();
    }


    //para que el boton vuelva a su tamaño original al quitar el mouse de encima
    @FXML
    private void salirHoverIniciar(javafx.scene.input.MouseEvent e) {

        ScaleTransition st = new ScaleTransition(Duration.millis(200), (Node) e.getSource());
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }


    

  @FXML
public void verInstrucciones(javafx.scene.input.MouseEvent event) {

    //se usa el node para obtener el nodo del boton que se hizo click, para luego obtener la ventana y cambiar la escena
    Node nodo = (Node) event.getSource();

    ScaleTransition click = new ScaleTransition(Duration.millis(120), nodo);
    click.setToX(0.9);
    click.setToY(0.9);

    click.setOnFinished(e -> {

        ScaleTransition volver = new ScaleTransition(Duration.millis(120), nodo);
        volver.setToX(1.0);
        volver.setToY(1.0);

        volver.setOnFinished(ev -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/instrucciones.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) nodo.getScene().getWindow();

                Scene scene = new Scene(root, 390, 400);
                stage.setScene(scene);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        volver.play();
    });

    click.play();
}
}