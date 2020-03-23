package Dictionary;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Dictionary extends Object {
    //Macierz kodująca
    private long[][] H;
    //Mapa do kodowania : klucz -> liczba, wartość -> słowo kodowe
    private Map<Integer, byte[]> TEncode = new HashMap<Integer, byte[]>();
    //Stale do sterowania petlami
    public int MESSAGE_MAX_VALUE;
    public int PARITY_MAX_VALUE;
    public int WORD_LENGTH;
    public int PARITY_LENGTH;
    public int MESSAGE_LENGTH;

    //KONSTRUKTOR
    /*
    konstruuje obiekt ze statycznie przypisana macierza kodowania oraz obliczonymi na jej podstawie slowami kodowymi
     */
    public Dictionary(boolean errors2) {
        //Tymczasowe bufory przechowujące odpowiadające im dane w postaci tablicy BitSetów
        BitSet[] HTemp;
        BitSet[] TTemp;

        //Deklaracja macierzy H dla wariantu z korektą dwóch błędów
        if (errors2) {
            H = new long[8][];
            HTemp = new BitSet[8];
            TTemp = new BitSet[255];
            HTemp[0] = BitSet.valueOf(new long[]{28544});
            HTemp[1] = BitSet.valueOf(new long[]{48704});
            HTemp[2] = BitSet.valueOf(new long[]{24352});
            HTemp[3] = BitSet.valueOf(new long[]{59152});
            HTemp[4] = BitSet.valueOf(new long[]{46856});
            HTemp[5] = BitSet.valueOf(new long[]{63748});
            HTemp[6] = BitSet.valueOf(new long[]{56578});
            HTemp[7] = BitSet.valueOf(new long[]{64001});
            MESSAGE_MAX_VALUE = 255;
            PARITY_MAX_VALUE = 255;
            WORD_LENGTH = 16;
            PARITY_LENGTH = 8;
            MESSAGE_LENGTH = 8;
            //Deklaracja macierzy H dla wariantu z korektą jednego błedu
        } else {
            H = new long[4][];
            HTemp = new BitSet[4];
            TTemp = new BitSet[255];
            HTemp[0] = BitSet.valueOf(new long[]{1896});
            HTemp[1] = BitSet.valueOf(new long[]{2964});
            HTemp[2] = BitSet.valueOf(new long[]{3410});
            HTemp[3] = BitSet.valueOf(new long[]{3745});
            MESSAGE_MAX_VALUE = 255;
            PARITY_MAX_VALUE = 15;
            WORD_LENGTH = 12;
            PARITY_LENGTH = 4;
            MESSAGE_LENGTH = 8;
        }

        //=====TWORZENIE SLOW KODOWYCH DLA KOLEJNYCH WARTOSCI
        for (int i = 0; i < MESSAGE_MAX_VALUE; i++) {
            TTemp[i] = new BitSet(WORD_LENGTH);
            //"oryginalną" wartość przesuwamy na przód słowa
            TTemp[i] = BitSet.valueOf(new long[]{(i * (pow(2,PARITY_LENGTH)))});
            //Obliczanie kolejnych bitow parzystosci dla slowa
            for (int j = 0; j < PARITY_LENGTH; j++) {
                int temp = 0;
                //zliczanie ilości jedynek na pozycjach znaczących j-tego wiersza macierzy H
                for (int k = MESSAGE_LENGTH; k >= 0; k--) {
                    if (TTemp[i].get(k + PARITY_LENGTH) && HTemp[j].get(k + PARITY_LENGTH)) {
                        temp++;
                    }
                }
                //ustawianie j-tego bitu parzystości
                if (temp % 2 == 1) {
                    TTemp[i].set(PARITY_LENGTH - 1 - j, true);
                }
            }
        }

        //Rzutowanie macierzy kodującej z BitSetow na tablicę longow
        for (int i = 0; i < H.length; i++) {

            H[i] = HTemp[i].toLongArray();
        }

        //wladanie wartosci wartosc->slowo do mapy slownika
        for (int i = 0; i < 255; i++) {

            TEncode.put(i, TTemp[i].toByteArray());
        }
    }

    //zwraca slowo kodowe dla zadanej wartosci
    public byte[] getWord(int index) {

        return TEncode.get(index);
    }

    //zwraca zadana kolumne macierzy H
    public int getHColumn(int index) {
        //wynik operacji
        BitSet tempResult = new BitSet(PARITY_LENGTH);
        //holder wierszy macierzy H
        BitSet temp;
        //"wyciąganie" kolejnych wierszy z macierzy H do holdera i "wrzucanie" odpowiedniej pozycji do wyniku
        for (int i = 0; i < H.length; i++) {

            temp = BitSet.valueOf(H[i]);
            tempResult.set(PARITY_LENGTH - 1 - i, temp.get(index));
        }

        //rzutowanie byte[] -> int
        byte[] result = new byte[4];
        result[3] = tempResult.toByteArray()[0];
        return ByteBuffer.wrap(result).getInt();
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