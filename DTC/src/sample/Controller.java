package sample;

import com.Dictionary.Dictionary;
import com.EncodeDecode.Encode;
import com.EncodeDecode.Decode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Controller {

    private Dictionary dictionary = new Dictionary();
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

    String filePath;
    int[] buffer;

    public void selectFile(){

        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if(selectedFile != null){

            this.filePath = selectedFile.getAbsolutePath();

        }

        this.selectedFileLabel.setText(this.filePath);
    }

    public void saveFile(){

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
        }

        this.statusLabel.setText("FILE SAVED");
    }

    public void saveAndSelectFile(){
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
        }

        this.filePath = destination;
        this.statusLabel.setText("FILE SAVED");
    }

    public void encode(){

        byte[] temp = new byte[0];
        try {
            Encode result = new Encode(filePath, dictionary);
            temp = result.getEncodeData();
        }catch(IOException ex){

            this.statusLabel.setText("CANNOT READ FROM FILE !!!");
        }

        buffer = new int[temp.length];
        for(int i = 0; i < temp.length; i++){

            buffer[i] = temp[i];
        }

        statusLabel.setText("Encode data successfully");

    }

    public void decode(){

            Decode result = new Decode(filePath, dictionary);
            buffer = result.getDecodeData();

            statusLabel.setText("Decode data successfully");
    }

}
