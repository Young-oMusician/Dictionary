package com.Dictionary;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Dictionary extends Object{
    //Macierz kodująca
    private long[][] H = new long[8][];
    //Mapa do kodowania : klucz -> liczba, wartość -> słowo kodowe
    private Map<Integer, byte[]> TEncode = new HashMap<Integer, byte[]>();
    //Stale do sterowania petlami
    public final int MESSAGE_MAX_VALUE = 255;
    public final int PARITY_MAX_VALUE = 255;
    public final int WORD_LENGTH = 16;
    public final int PARITY_LENGTH = 8;
    public final int MESSAGE_LENGTH = 8;

    //KONSTRUKTOR
    /*
    konstruuje obiekt ze statycznie przypisana macierza kodowania oraz obliczonymi na jej podstawie slowami kodowymi
     */
    public Dictionary(){

        BitSet[] HTemp = new BitSet[8];
        BitSet[] TTemp = new BitSet[255];
        HTemp[0] = BitSet.valueOf(new long[]{28544});
        HTemp[1] = BitSet.valueOf(new long[]{48704});
        HTemp[2] = BitSet.valueOf(new long[]{24352});
        HTemp[3] = BitSet.valueOf(new long[]{59152});
        HTemp[4] = BitSet.valueOf(new long[]{46856});
        HTemp[5] = BitSet.valueOf(new long[]{63748});
        HTemp[6] = BitSet.valueOf(new long[]{56578});
        HTemp[7] = BitSet.valueOf(new long[]{64001});

        //=====TWORZENIE SLOW KODOWYCH DLA KOLEJNYCH WARTOSCI
        for(int i = 0; i < MESSAGE_MAX_VALUE; i++){
            TTemp[i] = new BitSet(WORD_LENGTH);
            TTemp[i] = BitSet.valueOf(new long[]{(i*(256))});
            //Obliczanie kolejnych bitow parzystosci dla slowa
            for(int j = 0 ; j< 8;j++) {
                int temp = 0;
                for (int k = PARITY_LENGTH; k >= 0; k--) {
                    if( TTemp[i].get(k + PARITY_LENGTH)  && HTemp[j].get(k+ PARITY_LENGTH)){
                        temp++ ;
                    }
                }
                if(temp%2 == 1){
                    TTemp[i].set(PARITY_LENGTH - 1 - j,true);
                }
            }
        }

        //Rzutowanie macierzy kodującej z BitSetow na tablicę longow
        for(int i = 0; i < 8; i++){

            H[i] = HTemp[i].toLongArray();
        }

        //wladanie wartosci wartosc->slowo do mapy slownika
        for(int i = 0; i < 255; i++){

            TEncode.put(i,TTemp[i].toByteArray());
        }
    }

    //zwraca slowo kodowe dla zadanej wartosci
    public byte[] getWord(int index){

        return TEncode.get(index);
    }

    //zwraca zadana kolumne macierzy H
    public int getHColumn(int index){

        BitSet tempResult = new BitSet(8);
        BitSet temp;

        for(int i = 0; i < 8; i++){

            temp = BitSet.valueOf(H[i]);
            tempResult.set(7-i,temp.get(index));
        }
        byte[] result = new byte[4];
        result[3] = tempResult.toByteArray()[0];
        return ByteBuffer.wrap(result).getInt();
    }
}


