package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    private Button Generation;

    @FXML
    private Button Encryption;

    @FXML
    private Button Decryption;

    @FXML
    private Button About;

    @FXML
    //转到加密操作
    void DoEncryption(ActionEvent event) throws Exception {
        application.Encryption EP = new application.Encryption();
        EP.showEncryptionWindow();
        Stage stage = (Stage)this.Encryption.getScene().getWindow();
        stage.close();
    }

    @FXML
    //转到解密操作
    void DoDecryption(ActionEvent event) throws Exception {
        application.Decryption DP = new application.Decryption();
        DP.showDecryptionWindow();
        Stage stage = (Stage)this.Decryption.getScene().getWindow();
        stage.close();
    }

    @FXML
    //转到关于程序
    void DoAbout(ActionEvent event) throws Exception {
        application.About AT = new application.About();
        AT.showAboutWindow();
        Stage stage = (Stage)this.About.getScene().getWindow();
        stage.close();
    }

    @FXML
    //转到非对称密钥生成
    void DoGeneration(ActionEvent event) throws Exception {
        application.Generation GR = new application.Generation();
        GR.showGenerationWindow();
        Stage stage = (Stage)this.Generation.getScene().getWindow();
        stage.close();
    }
}
