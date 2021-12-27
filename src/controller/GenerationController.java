package controller;

import utils.ClipBoardUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.RSAUtil;


import java.io.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

public class GenerationController {

    Stage stage=new Stage();

    @FXML
    private Button Generation;

    @FXML
    private Button SaveUK;

    @FXML
    private TextArea PublicKeyArea;

    @FXML
    private Button SaveRK;

    @FXML
    private TextArea PrivateKeyArea;

    @FXML
    private Button CopyRK;

    @FXML
    private Button CopyUK;

    @FXML
    private Button Back;

    @FXML
    //保存公钥到文件
    void DoSaveUK(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("设置公钥文件名");
        fileChooser.setInitialFileName(".pubkey");
        File file=fileChooser.showSaveDialog(stage);
        if(file != null){
            System.out.println(file.getAbsoluteFile());
            OutputStream outputStream=new FileOutputStream(file);
            outputStream.write(this.PublicKeyArea.getText().getBytes());
            outputStream.close();
            System.out.println(" -----> 公钥保存成功");
        }

    }

    @FXML
    //保存私钥到文件
    void DoSaveRK(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("设置私钥文件名");
        fileChooser.setInitialFileName(".prikey");
        File file=fileChooser.showSaveDialog(stage);
        if(file != null) {
            System.out.println(file.getAbsoluteFile());
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(this.PrivateKeyArea.getText().getBytes());
            outputStream.close();
            System.out.println(" -----> 私钥保存成功");
        }
    }

    @FXML
    //复制公钥到粘贴板
    void DoCopyUK(ActionEvent event) {
        ClipBoardUtil.setSysClipboardText(PublicKeyArea.getText());
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.titleProperty().set("提示");
        alert.headerTextProperty().set("公钥复制成功!");
        alert.showAndWait();
    }

    @FXML
    //复制私钥到粘贴板
    void DoCopyRK(ActionEvent event) {
        ClipBoardUtil.setSysClipboardText(PrivateKeyArea.getText());
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.titleProperty().set("提示");
        alert.headerTextProperty().set("私钥复制成功!");
        alert.showAndWait();
    }

    @FXML
    //返回首页
    void DoBack(ActionEvent event) throws Exception {
        application.Menu menu = new application.Menu();
        menu.showMenuWindow();
        Stage stage = (Stage)this.Back.getScene().getWindow();
        stage.close();
    }

    @FXML
    //生成密钥
    void DoGenerantion(ActionEvent event) throws Exception{
        Map<String, String> keyMap = RSAUtil.genKeyPair();
        String rsaPublicKey = keyMap.get("RSAPublicKey");
        String rsaPrivateKey = keyMap.get("RSAPrivateKey");
        System.out.println("RSA PublicKey: " + rsaPublicKey);
        System.out.println("RSA PrivateKey: " + rsaPrivateKey);
        System.out.println(rsaPrivateKey);
        PrivateKeyArea.setText(rsaPrivateKey);
        PublicKeyArea.setText(rsaPublicKey);
    }

}
