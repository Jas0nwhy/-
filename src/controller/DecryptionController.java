package controller;

import com.alibaba.fastjson.JSONObject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.AESUtil;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import utils.BytesToHex;
import utils.DESUtil;
import utils.RSAUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class DecryptionController {
    private Stage stage=new Stage();

    //RSA公钥
    private PublicKey rsaPublicKey;
    //RSA私钥
    private PrivateKey rsaPrivateKey;
    //明文二进制数据
    private byte[] data;
    //hash值
    private byte[] hash;
    //明文格式
    private String dataName;

    @FXML
    private TextArea PlainText;

    @FXML
    private Button CheckHash;

    @FXML
    private ChoiceBox<String> SymEncryption;

    @FXML
    private Button DecodeData;

    @FXML
    private Button Back;

    @FXML
    private Button ChoicePriKey;

    @FXML
    private ChoiceBox<String> HashAlgorithm;

    @FXML
    private Button ChoicePubKey;

    @FXML
    private Label PrivateKeyName;

    @FXML
    private Label PublicKeyName;

    @FXML
    //选择接收者私钥文件
    void doChoicePriKey(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择接收者私钥文件");
        File file=fileChooser.showOpenDialog(stage);
        if(file != null) {
            System.out.println(file.getAbsoluteFile());
            PrivateKeyName.setText(file.getName());
            rsaPrivateKey = RSAUtil.loadPrivateKey(file.getAbsoluteFile().toString());
            System.out.println("RSA PrivateKey: " + rsaPrivateKey);
        }
    }

    @FXML
    //选择发送者公钥文件
    void doChoicePubKey(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择发送者公钥文件");
        File file=fileChooser.showOpenDialog(stage);
        if(file != null) {
            System.out.println(file.getAbsoluteFile());
            PublicKeyName.setText(file.getName());
            rsaPublicKey = RSAUtil.loadPublicKey(file.getAbsoluteFile().toString());
            System.out.println("RSA Publickey: " + rsaPublicKey);
        }
    }

    @FXML
    //进行解密操作
    void doDecodeData(ActionEvent event) throws Exception {
        if(SymEncryption.getValue() == null || HashAlgorithm == null || rsaPrivateKey == null || rsaPublicKey == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("解密设置未完成，无法解密！");
            alert.showAndWait();
            return;
        }
        //读取加密文件
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择解密文件");
        File file=fileChooser.showOpenDialog(stage);
        if(file == null) {
            return;
        }
        System.out.println(file.getAbsoluteFile());
        FileInputStream fis = new FileInputStream(file);
        byte[] byt = new byte[fis.available()];
        fis.read(byt);
        String str = new String(byt, "UTF-8");
        //将字节流解析为存储密文和密文格式的json
        JSONObject jsonE = JSONObject.parseObject(str);
        System.out.println(jsonE.toString());
        byte[] encrypt = jsonE.getBytes("ENCRYPT");
        byte[] encryptKey = jsonE.getBytes("ENCRYPTKEY");
        //得到对称加密密钥Key
        byte[] Key = RSAUtil.decrypt(rsaPrivateKey, encryptKey);
        //存储明文和加密hash值的json的字节流
        byte[] jsonHbyte = null;
        if(SymEncryption.getValue().equals("AES")){
            jsonHbyte = AESUtil.decryptAES(encrypt,Key);
        }else{
            jsonHbyte = DESUtil.decryptDES(encrypt,Key);
        }
        String str2 = new String(jsonHbyte, "UTF-8");
        //将字节流解析为存储明文。明文格式和加密hash值的json
        JSONObject jsonH = JSONObject.parseObject(str2);
        data = jsonH.getBytes("DATA");
        hash = jsonH.getBytes("HASH");
        dataName = jsonH.getString("DATANAME");
        if(dataName == "Text"){
            String s = new String(data);
            PlainText.setText(s);
        }else{
            FileChooser fileChooser2 = new FileChooser();
            fileChooser2.setTitle("设置解密文件存放路径");
            fileChooser2.setInitialFileName("decode_" + dataName);
            File file2=fileChooser2.showSaveDialog(stage);
            if(file2 != null) {
                OutputStream outputStream = new FileOutputStream(file2);
                outputStream.write(data);
                outputStream.close();
                System.out.println(" -----> 文件保存成功");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.titleProperty().set("提示");
                alert.headerTextProperty().set("解密文件保存成功！路径:" + file2.getAbsoluteFile());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void doCheckHash(ActionEvent event) throws Exception {
        if(data == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("未解密数据，请先解密数据！");
            alert.showAndWait();
        }
        byte[] checkHash = new byte[1024];
        if(HashAlgorithm.getValue().equals("MD5")){
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            checkHash = messageDigest.digest(data);
        }else if (HashAlgorithm.getValue().equals("SHA")){
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            checkHash = messageDigest.digest(data);
        }
        if(!hash.equals(checkHash)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.titleProperty().set("警告");
            alert.headerTextProperty().set("文件hash校验成功!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("文件hash校验失败!");
            alert.showAndWait();
        }
    }

    @FXML
    void doBack(ActionEvent event) throws Exception {
        application.Menu menu = new application.Menu();
        menu.showMenuWindow();
        Stage stage = (Stage)this.Back.getScene().getWindow();
        stage.close();
    }

    @FXML
        //初始化设置
    void initialize() {
        SymEncryption.setItems(FXCollections.observableArrayList("AES","DES"));
        HashAlgorithm.setItems(FXCollections.observableArrayList("SHA","MD5"));
    }
}
