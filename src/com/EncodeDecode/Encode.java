package com.EncodeDecode;

import com.Dictionary.Dictionary;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Encode {

    private byte[] data;
    private String path;
    private Dictionary dictionary;
    private long[][] encodeData;

    public Encode(String path, Dictionary dictionary) throws IOException {

        Path tempPath = Paths.get(path);

        try {
            data = Files.readAllBytes(tempPath);
        }catch (IOException ex){

            System.out.println("Load File Error !");
        }

        this.dictionary = dictionary;
        this.encodeData = new long[data.length][];
        int word;
        for(int i = 0; i < data.length; i++){

            word = data[i];
            encodeData[i] = dictionary.getWord(word);
        }

        JOptionPane.showConfirmDialog(null, "Continue");
    }

    public void toFile(String dest){

        try {
            FileOutputStream fos = new FileOutputStream(dest);
            byte[] temp;
            for(int i = 0; i < encodeData.length; i++){
                temp = longToBytes(encodeData[i][0]);
                fos.write(temp[6]);
                fos.write(temp[7]);
                //fos.write("\n".getBytes());
            }
        }catch(IOException ex){

            System.out.println("Writing Error !");
        }


    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

}
