package com.Dictionary;

import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Dictionary extends Object{

    private long[][] H = new long[7][];
    private Map<byte[], byte[]> T = new HashMap<byte[], byte[]>();

    Dictionary(){
        BitSet[] HTemp = new BitSet[7];
        BitSet[] TTemp = new BitSet[255];
        HTemp[0] = BitSet.valueOf(new long[]{32448});
        HTemp[1] = BitSet.valueOf(new long[]{32160});
        HTemp[2] = BitSet.valueOf(new long[]{31632});
        HTemp[3] = BitSet.valueOf(new long[]{30600});
        HTemp[4] = BitSet.valueOf(new long[]{28548});
        HTemp[5] = BitSet.valueOf(new long[]{24450});
        HTemp[6] = BitSet.valueOf(new long[]{16257});

        for(int i = 0; i < 255; i++){
            TTemp[i] = new BitSet(15);
            TTemp[i] = BitSet.valueOf(new long[]{(i*128)});
            for(int j = 0 ; j< 7;j++) {
                int temp = 0;
                for (int k = 7; k >= 0; k--) {
                    if( TTemp[i].get(k + 7)  && HTemp[j].get(k+7)){
                        temp++ ;
                    }
                }
                if(temp%2 == 1){
                    TTemp[i].set(6 - j,true);
                }
            }
        }

        for(int i = 0; i < 7; i++){

            H[i] = HTemp[i].toLongArray();
        }
        for(int i = 0; i < 255; i++){

            T.put(ByteBuffer.allocate(2).putInt(i).array(),TTemp[i].toByteArray());
        }
        int a = 0;
    }

    public long[] getWord(int index){

        return T.get();
    }

}


