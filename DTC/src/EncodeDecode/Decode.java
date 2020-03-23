package EncodeDecode;

import Dictionary.Dictionary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.Vector;

public class Decode {

    //zdekodowane dane
    private int[] decodeData;
    //sciezka do pliku z zakodowanymi danymi
    private String path;
    //slownik
    private Dictionary dictionary;
    //zakodowane dane
    private byte[] encodeData;

    public static Exception TooManyErrors;

    //==========KONSTRUKTOR
    /*
    odkodowuje dane na podstawie zakodowanych danych z pliku pod sciezka "path" oraz slownika
     */
    public Decode(String path, Dictionary dictionary) throws Exception {
        //wczytywanie danych z pliku
        Path tempPath = Paths.get(path);
        try {
            encodeData = Files.readAllBytes(tempPath);
        } catch (IOException ex) {

            System.out.println("Load File Error !");
        }

        //przeniesienie zakodowanych danych do dwuwymiarowej tablicy bajtow (jedno slowo jeden wiersz)
        byte[][] encodeTemp = new byte[encodeData.length / 2][2];

        for (int i = 0; i < encodeTemp.length; i++) {
            encodeTemp[i][0] = encodeData[i * 2];
            encodeTemp[i][1] = encodeData[(i * 2) + 1];
        }


        this.dictionary = dictionary;
        this.decodeData = new int[encodeData.length / 2];

        //dekodowanie
        for (int i = 0; i < encodeTemp.length; i++) {

            decodeData[i] = correction(encodeTemp[i])/pow(2,dictionary.PARITY_LENGTH);

        }


    }

    //zapisywanie zdekodowanych danych do pliku
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

    //korekta bledow
    private int correction(byte[] error) throws Exception {

        //holder przetrzymujący wartość error w postaci BitSetu
        BitSet tempError = BitSet.valueOf(error);
        //wynik operacji
        int result = 0;
        //holder przetrzymujacy wartośc error w postaci int
        int intError = byteArrToInt(error);

        //mnozenie slowa przez macierz
        for(int i = 0; i < dictionary.WORD_LENGTH; i++){

            if(tempError.get(i)){

               result = result ^ dictionary.getHColumn(i);
            }
        }

        //jesli wynikowy wektor jest zerowy zwraca bez korekty
        if(result == 0){

            return byteArrToInt(error);
        }else{
            //szuka pojedynczego bledu i dokonuje korekty
            for(int i = 0; i < dictionary.WORD_LENGTH; i++){

                if(result == dictionary.getHColumn(i)){

                    int mask = 1 << i;
                    return intError ^ mask;
                }
            }
            if(dictionary.PARITY_LENGTH == 8) {
                //szuka podwójnego bledu i dokonuje korekty
                for (int i = 0; i < dictionary.WORD_LENGTH; i++) {
                    for (int j = i + 1; j < dictionary.WORD_LENGTH; j++) {

                        if (result == (dictionary.getHColumn(dictionary.WORD_LENGTH - 1 - i)
                                ^ dictionary.getHColumn(dictionary.WORD_LENGTH - 1 - j))) {

                            int mask = 1 << dictionary.WORD_LENGTH - 1 - i;
                            mask = mask + (1 << dictionary.WORD_LENGTH - 1 - j);
                            return intError ^ mask;
                        }
                    }
                }
            }
        }

        throw TooManyErrors;
    }

    public static int byteArrToInt(byte[] arr){

        byte[] temp = new byte[4];
        for (int i = 0; i < arr.length; i++){

            temp[3-i] = arr[i];
        }

        return ByteBuffer.wrap(temp).getInt();
    }

    public int[] getDecodeData(){

        int[] result = new int[decodeData.length];

        for(int i = 0; i < decodeData.length; i++){

            result[i] = decodeData[i];
        }

        return result;
    }

    //potęgowanie intów
    private int pow(int a, int b) {
        int result = 1;

        for (int i = 0; i < b; i++) {

            result *= a;
        }

        return result;
    }

}
