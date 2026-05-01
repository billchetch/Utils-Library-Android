package net.chetch.utilities;

public class Formatting {

    public static String getBitString(Integer n, int byteCount, char byteSeperator){
        String formatted = String.format("%" + byteCount*8 + "s", Integer.toBinaryString(n)).replace(' ', '0');
        char[] charArray = formatted.toCharArray();
        char[] bitString = new char[charArray.length - 1 + charArray.length/8];
        int j  = 0;
        for(int i = 0; i < charArray.length; i++){
            bitString[j++] = charArray[i];
            if(i % 8 == 7 && i < charArray.length - 1){
                bitString[j++] = byteSeperator;
            }
        }

        return new String(bitString);
    }



}
