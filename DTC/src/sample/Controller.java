package sample;

import Dictionary.Dictionary;
import EncodeDecode.Encode;
import EncodeDecode.Decode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Controller {


    @FXML
    private Pane mainPane;
    @FXML
    private Label titleLabel;
    @FXML
    private Label selectedFileInfoLabel;
    @FXML
    private Label selectedFileLabel;
    @FXML
    private Label statusInfoLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button saveFileButton;
    @FXML
    private Button saveAndSelectFileButton;
    @FXML
    private Button encodeButton;
    @FXML
    private Button decodeButton;
    @FXML
    private RadioButton oneErrorRadio;
    @FXML
    private  RadioButton twoErrorsRadio;

    private Dictionary dictionary;

    String filePath;
    int[] buffer;

    public void selectFile(){
        if(oneErrorRadio.isSelected()){
            dictionary = new Dictionary(false);
            statusLabel.setText("");
        }
        else if(twoErrorsRadio.isSelected()){
            dictionary = new Dictionary(true);
            statusLabel.setText("");
        }
        else{
            statusLabel.setText("Select Program Variant");
            return;
        }
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if(selectedFile != null){

            this.filePath = selectedFile.getAbsolutePath();

        }
        if(filePath == null){

            return;
        }

        this.selectedFileLabel.setText(this.filePath);
    }

    public void saveFile(){

        if(filePath == null){
            statusLabel.setText("There isn't any file");
            return;
        }

        String destination;

        FileChooser fc = new FileChooser();
        File selectedFile = fc.showSaveDialog(null);
        destination = selectedFile.getAbsolutePath();

        try {
            FileOutputStream fos = new FileOutputStream(destination);
            for(int i = 0; i < buffer.length; i++) {
                fos.write((char) buffer[i]);
            }
            fos.close();
        }catch(FileNotFoundException ex){

            this.statusLabel.setText("FILE PROBLEM !!!");
            return;
        }catch(IOException ex){

            this.statusLabel.setText("CANNOT SAVE TO FILE !!!");
            return;
        }

        this.statusLabel.setText("FILE SAVED");
    }

    public void saveAndSelectFile(){

        if(filePath == null){
            statusLabel.setText("There isn't any file");
            return;
        }
        String destination;

        FileChooser fc = new FileChooser();
        File selectedFile = fc.showSaveDialog(null);
        destination = selectedFile.getAbsolutePath();

        try {
            FileOutputStream fos = new FileOutputStream(destination);
            for(int i = 0; i < buffer.length; i++) {
                fos.write((char) buffer[i]);
            }
            fos.close();
        }catch(FileNotFoundException ex){

            this.statusLabel.setText("FILE PROBLEM !!!");
        }catch(IOException ex){

            this.statusLabel.setText("CANNOT SAVE TO FILE !!!");
            return;
        }

        this.filePath = destination;
        this.selectedFileLabel.setText(destination);
        this.statusLabel.setText("FILE SAVED");
    }

    public void encode(){

        if(filePath == null){
            statusLabel.setText("Choose file !");
            return;
        }
        byte[] temp = new byte[0];
        try {
            Encode result = new Encode(filePath, dictionary);
            temp = result.getEncodeData();
        }catch(IOException ex){

            this.statusLabel.setText("CANNOT READ FROM FILE !!!");
        }

        statusLabel.setText("Encoding...");
        buffer = new int[temp.length];
        for(int i = 0; i < temp.length; i++){

            buffer[i] = temp[i];
        }
        statusLabel.setText("Encode data successfully");

    }

    public void decode(){
        if(filePath == null){
            statusLabel.setText("Choose file !");
            return;
        }
        statusLabel.setText("Decoding...");
        try {
            Decode result = new Decode(filePath, dictionary);
            buffer = result.getDecodeData();
        }catch(Exception ex){

            statusLabel.setText("Detected more errors than " + dictionary.PARITY_LENGTH/4);
            return;
        }
        statusLabel.setText("Decode data successfully");
    }

}
