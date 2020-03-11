package com.EncodeDecode;

import com.Dictionary.Dictionary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Decode {

    private int[] decodeData;
    private String path;
    private Dictionary dictionary;
    private byte[] encodeData;

    public Decode(String path, Dictionary dictionary){
        Path tempPath = Paths.get(path);
        try {
            encodeData = Files.readAllBytes(tempPath);
        }catch (IOException ex){

            System.out.println("Load File Error !");
        }

        byte[][] encodeTemp = new byte[encodeData.length/2][2];

        for(int i = 0; i < encodeTemp.length; i++){
           encodeTemp[i][0] = encodeData[i*2];
           encodeTemp[i][1] = encodeData[(i*2)+1];
        }

        this.dictionary = dictionary;
        this.decodeData = new int[encodeData.length/2];


        for(int i = 0; i < encodeTemp.length; i++){
               decodeData[i] = dictionary.getValue(encodeTemp[i]);
               int tutajSkonczylismy;
        }


    }

    public void toFile(String dest){

        try {
            FileOutputStream fos = new FileOutputStream(dest);
            for(int i = 0; i < decodeData.length; i+=2){
                fos.write((char)decodeData[i]);
            }

        }catch(IOException ex){

            System.out.println("Writing Error !");
        }


    }

}
