package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class Menu extends Application {

    Stage stage=new Stage();
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
        primaryStage.setTitle("密码学与安全协议大作业");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    //显示菜单窗口
    //显示菜单窗口
    public void  showMenuWindow() throws Exception {
        start(stage);
    }

}

