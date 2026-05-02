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

    public static String getBitString(Integer n){
        return getBitString(n, 4, '-');
    }

    public static String getBitString(Byte n){
        return getBitString((int)(n & 0xFF), 1, '-');
    }

    public static String getBitString(Short n){
        return getBitString((int)n, 2, '-');
    }

    public static String getByteString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(byte b : bytes){
            if(i > 0)sb.append(",");
            sb.append(String.format("%02x", b));
            i++;
        }
        return sb.toString();
    }

}
