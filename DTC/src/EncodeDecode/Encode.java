package EncodeDecode;

import Dictionary.Dictionary;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Encode {

    //dane do zakodowania
    private byte[] data;
    //sciezka do pliku z danymi
    private String path;
    //slownik na podstawie ktorego odbywa sie kodowanie
    private Dictionary dictionary;
    //wynik kodowania
    private byte[][] encodeData;

    //==========KONSTRUKTOR
    /*
    Pobiera dane z pliku znajdujacego sie pod sciezka "path" i koduje je z 8-bitowych blokow w 16-bitowe slowa
     */
    public Encode(String path, Dictionary dictionary) throws IOException {

        Path tempPath = Paths.get(path);

        //zczytuje dane z pliku
        try {
            data = Files.readAllBytes(tempPath);
        }catch (IOException ex){

            System.out.println("Load File Error !");
        }

        this.dictionary = dictionary;
        this.encodeData = new byte[data.length][];
        int index;

        //koduje dane
        for(int i = 0; i < data.length; i++){

            index = data[i];
            encodeData[i] = dictionary.getWord(index);
        }

    }

    //zapisuje zakodowane dane do pliku
    public void toFile(String dest){

        byte[] wholeData = new byte[encodeData.length*2];

        try {
            FileOutputStream fos = new FileOutputStream(dest);
            for(int i = 0; i < wholeData.length; i+=2){
                wholeData[i] = encodeData[i/2][0];
                wholeData[i+1] = encodeData[i/2][1];
            }

            fos.write(wholeData);
        }catch(IOException ex){

            System.out.println("Writing Error !");
        }


    }

    //rzutowanie z longow do bajtow
    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public byte[] getEncodeData(){

        byte[] wholeData = new byte[encodeData.length*2];

        for(int i = 0; i < wholeData.length; i+=2){
            wholeData[i] = encodeData[i/2][0];
            wholeData[i+1] = encodeData[i/2][1];
        }

        return wholeData;
    }
}


