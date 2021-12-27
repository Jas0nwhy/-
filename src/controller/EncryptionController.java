package controller;

import com.alibaba.fastjson.JSONObject;
import javafx.scene.control.*;
import sun.misc.BASE64Encoder;
import utils.AESUtil;
import utils.BytesToHex;
import utils.DESUtil;
import utils.RSAUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;




public class EncryptionController {
    private Stage stage=new Stage();

    //RSA公钥
    private PublicKey rsaPublicKey;
    //RSA私钥
    private PrivateKey rsaPrivateKey;
    //明文二进制数据
    private byte[] data;
    //对称加密密钥
    private byte[] Key;
    //hash值
    private byte[] hash;
    //明文格式
    private String dataName;

    @FXML
    private Button Back;

    @FXML
    private TextField SymKeyText;

    @FXML
    private ChoiceBox<String> SymEncryption;

    @FXML
    private Button SymKey;

    @FXML
    private TextArea PlainText;

    @FXML
    private Button FileEncode;

    @FXML
    private Button ChoicePriKey;

    @FXML
    private Button SymKeySeed;

    @FXML
    private Button StrEncode;

    @FXML
    private ChoiceBox<String> HashAlgorithm;

    @FXML
    private Button ChoicePubKey;

    @FXML
    private Button EncodeData;

    @FXML
    private Label FileName;

    @FXML
    private Label PublicKeyName;

    @FXML
    private Label PrivatKeyName;

    @FXML
    //选择用户输入的字符串进行加密
    void doStrEncode(ActionEvent event) {
        if(PlainText.getText().length() != 0){
            data = PlainText.getText().getBytes(StandardCharsets.UTF_8);
            dataName = "Text";
            String encryptDataString = new BASE64Encoder().encode(data);
            System.out.println(encryptDataString);
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("请先在文本框中输入需要加密的明文！");
            alert.showAndWait();
            return;
        }

    }

    @FXML
    //选择文件进行加密
    void doFileEncode(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择文件进行加密");
        File file=fileChooser.showOpenDialog(stage);
        if(file != null){
            System.out.println(file.getAbsoluteFile());
            FileName.setText(file.getName());
            dataName = "decode_" + file.getName();
            long filesize = file.length();
            FileInputStream fi = new FileInputStream(file);
            byte[] buffer = new byte[(int) filesize];
            int offset = 0;
            int numread = 0;
            while (offset < buffer.length && (numread = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numread;
            }
            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new IOException("could not completely read file " + file.getName());
            }
            fi.close();
            data = buffer;
            String encryptDataString = new BASE64Encoder().encode(buffer);
            System.out.println(encryptDataString);
        }
    }

    @FXML
    //选择发送者私钥文件
    void doChoicePriKey(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择发送者私钥文件");
        File file=fileChooser.showOpenDialog(stage);
        if(file != null){
            System.out.println(file.getAbsoluteFile());
            PrivatKeyName.setText(file.getName());
            rsaPrivateKey = RSAUtil.loadPrivateKey(file.getAbsoluteFile().toString());
            System.out.println("RSA PrivateKey: " + rsaPrivateKey);
        }
    }

    @FXML
    //选择接收者公钥文件
    void doChoicePubKey(ActionEvent event) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择接受者公钥文件");
        File file=fileChooser.showOpenDialog(stage);
        if(file != null){
            System.out.println(file.getAbsoluteFile());
            PublicKeyName.setText(file.getName());
            rsaPublicKey = RSAUtil.loadPublicKey(file.getAbsoluteFile().toString());
            System.out.println("RSA Publickey: " + rsaPublicKey);
        }
    }

    @FXML
    //用户自定义生成对称加密密钥Key(AES-32位，DES-16位，采用截取hash的方法)
    void doSymKey(ActionEvent event) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        if(SymEncryption.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("请先选择需要的对称加密算法！");
            alert.showAndWait();
        }else {
            byte[] symkeytext;
            System.out.println(SymKeyText.getText());
            if(SymKeyText.getText().length() != 0){
                symkeytext= SymKeyText.getText().getBytes(StandardCharsets.UTF_8);
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.titleProperty().set("提示");
                alert.headerTextProperty().set("请先在文本框中输入自定义密钥！");
                alert.showAndWait();
                return;
            }
            if (SymEncryption.getValue().equals("AES")) {
                byte[] Key1 = messageDigest.digest(symkeytext);
                byte[] Key2 = messageDigest.digest(symkeytext);
                Key = new byte[Key1.length + Key2.length];
                System.arraycopy(Key1, 0, Key, 0, Key1.length);
                System.arraycopy(Key2, 0, Key, Key1.length, Key2.length);
                System.out.println(Key.length);
            }else {
                Key = messageDigest.digest(symkeytext);
                System.out.println(Key.length);
            }
        }
    }

    @FXML
    //根据随机种子生成对称加密密钥Key
    void doSymKeySeed(ActionEvent event) throws Exception {
        if(SymEncryption.getValue()== null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("请先选择需要的对称加密算法！");
            alert.showAndWait();
        }else{
            if(SymEncryption.getValue().equals("AES")){
                Key = AESUtil.initKey();
            }else{
                Key = DESUtil.initKey();
            }
            SymKeyText.setText(BytesToHex.fromBytesToHex(Key));
            System.out.println(BytesToHex.fromBytesToHex(Key));
        }
    }

    @FXML
    //执行发送任务生成加密数据
    void doEncodeData(ActionEvent event) throws Exception {
        if(data == null|| Key == null || rsaPrivateKey == null || rsaPublicKey == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.titleProperty().set("提示");
            alert.headerTextProperty().set("加密设置未完成，无法加密！");
            alert.showAndWait();
            return;
        }
        //计算明文哈希值
        if(HashAlgorithm.getValue().equals("MD5")){
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            hash = messageDigest.digest(data);
        }else if (HashAlgorithm.getValue().equals("SHA")){
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            hash = messageDigest.digest(data);
        }
        //获得私钥加密的hash
        byte[] encryptHash = RSAUtil.encrypt(rsaPrivateKey, hash);
        //使用jsonH存放明文和加密hash
        JSONObject jsonH = new JSONObject();
        jsonH.put("DATA", data);
        jsonH.put("DATANAME", dataName);
        jsonH.put("HASH", encryptHash);
        //将jsonH变为字节流
        byte[] jsonHbyte = jsonH.toString().getBytes(StandardCharsets.UTF_8);
        JSONObject jsonE = new JSONObject();
        byte[] encrypt;
        if(SymEncryption.getValue().equals("AES")){
            encrypt = AESUtil.encryptAES(jsonHbyte, Key);
        }else{
            encrypt = DESUtil.encryptDES(jsonHbyte, Key);
        }
        System.out.println(Key.length);
        System.out.println(BytesToHex.fromBytesToHex(Key));
        byte[] encryptKey = RSAUtil.encrypt(rsaPublicKey, Key);
        jsonE.put("ENCRYPT", encrypt);
        jsonE.put("ENCRYPTKEY", encryptKey);

        //将jsonE保存到文件
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("设置加密文件名");
        fileChooser.setInitialFileName(".txt");
        File file=fileChooser.showSaveDialog(stage);
        FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile());
        fos.write(jsonE.toString().getBytes(StandardCharsets.UTF_8));
        fos.close();
        System.out.println(" -----> 密文保存成功");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.titleProperty().set("提示");
        alert.headerTextProperty().set("加密文件保存成功！");
        alert.showAndWait();
    }

    @FXML
    //返回主菜单
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
