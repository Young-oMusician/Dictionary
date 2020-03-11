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
    private byte[][] encodeData;

    public Encode(String path, Dictionary dictionary) throws IOException {

        Path tempPath = Paths.get(path);

        try {
            data = Files.readAllBytes(tempPath);
        }catch (IOException ex){

            System.out.println("Load File Error !");
        }

        this.dictionary = dictionary;
        this.encodeData = new byte[data.length][];
        int index;
        for(int i = 0; i < data.length; i++){

            index = data[i];
            encodeData[i] = dictionary.getWord(index);
        }

        JOptionPane.showConfirmDialog(null, "Continue");
    }

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

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

}
