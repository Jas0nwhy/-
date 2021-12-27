package application;

import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class Encryption extends Application {
    Stage stage=new Stage();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Encryption.fxml"));
        primaryStage.setTitle("密码学与安全协议大作业");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    //显示菜单窗口
    public void  showEncryptionWindow() throws Exception {
        start(stage);
    }
}
