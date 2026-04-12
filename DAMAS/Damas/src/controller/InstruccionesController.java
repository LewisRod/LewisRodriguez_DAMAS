package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InstruccionesController {

    @FXML
    private Button btnVolverMenu;

    @FXML
    private void volverAlMenu() throws Exception {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menu.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) btnVolverMenu.getScene().getWindow();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}