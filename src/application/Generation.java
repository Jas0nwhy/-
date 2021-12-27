package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Generation extends Application {

    Stage stage=new Stage();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Generation.fxml"));
        primaryStage.setTitle("密码学与安全协议大作业");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
    //显示非对称密钥生成窗口
    public void  showGenerationWindow() throws Exception {
        start(stage);
    }
}
