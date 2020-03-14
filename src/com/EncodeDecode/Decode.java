package com.EncodeDecode;

import com.Dictionary.Dictionary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.Vector;

public class Decode {

    private int[] decodeData;
    private String path;
    private Dictionary dictionary;
    private byte[] encodeData;

    public Decode(String path, Dictionary dictionary) {
        Path tempPath = Paths.get(path);
        try {
            encodeData = Files.readAllBytes(tempPath);
        } catch (IOException ex) {

            System.out.println("Load File Error !");
        }

        byte[][] encodeTemp = new byte[encodeData.length / 2][2];

        for (int i = 0; i < encodeTemp.length; i++) {
            encodeTemp[i][0] = encodeData[i * 2];
            encodeTemp[i][1] = encodeData[(i * 2) + 1];
        }


        this.dictionary = dictionary;
        this.decodeData = new int[encodeData.length / 2];


        for (int i = 0; i < encodeTemp.length; i++) {

            decodeData[i] = correction(encodeTemp[i]);
        }


    }

    public void toFile(String dest) {

        try {
            FileOutputStream fos = new FileOutputStream(dest);
            for (int i = 0; i < decodeData.length; i++) {
                fos.write((char) decodeData[i]);
            }

        } catch (IOException ex) {

            System.out.println("Writing Error !");
        }


    }

    private int correction(byte[] error) {

        BitSet dictionaryTemp;
        BitSet errorTemp = BitSet.valueOf(error);
        int mistakesNumber = 16;
        int probablyCorrect = -1;
        int counter = 0;
        int sync = 0;
        Vector<Integer> errorPos = new Vector<Integer>();
        for (int i = 0; i < 255; i++) {

            dictionaryTemp = BitSet.valueOf(dictionary.getWord(i));
            counter = 0;

            for (int j = 0; j < 15; j++) {

                if (dictionaryTemp.get(j) ^ errorTemp.get(j)) {

                    counter++;
                }
            }

            if (counter == 0) {

                return dictionary.getValue(dictionary.getWord(i));
            }
            if (counter == 1) {

                probablyCorrect = dictionary.getValue(dictionary.getWord(i));
                sync = 1;
            }
            if (counter == 2 && sync == 0) {
                probablyCorrect = dictionary.getValue(dictionary.getWord(i));
                sync = 2;
            }


        }
        if (probablyCorrect == -1) {

            return 0;
        } else {

            return probablyCorrect;
        }

    }

}
