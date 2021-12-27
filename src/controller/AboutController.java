package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    private Button Back;

    @FXML
    void doBack(ActionEvent event) throws Exception {
        application.Menu menu = new application.Menu();
        menu.showMenuWindow();
        Stage stage = (Stage)this.Back.getScene().getWindow();
        stage.close();
    }

}

