package com.Dictionary;

import com.EncodeDecode.Decode;
import com.EncodeDecode.Encode;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        Dictionary a = new Dictionary();
        //            Encode code = new Encode("D:\\Bitner\\Programowanko\\Java\\JavaPodstawyMoje\\Dictionary\\test.txt", a);
        //            code.toFile("D:\\Bitner\\Programowanko\\Java\\JavaPodstawyMoje\\Dictionary\\essa.txt");


        Decode decode = new Decode("D:\\Bitner\\Programowanko\\Java\\JavaPodstawyMoje\\Dictionary\\essa.txt", a);
        decode.toFile("D:\\Bitner\\Programowanko\\Java\\JavaPodstawyMoje\\Dictionary\\decode.txt");
    }
}
