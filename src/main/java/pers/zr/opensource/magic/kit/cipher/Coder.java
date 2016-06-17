package pers.zr.opensource.magic.kit.cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;

public class Coder {

    /**
     * 将二进制转换成16进制
     * @param src 二进制
     * @param upperCase 是否大写
     * @return 16进制
     */
    public static String parseBytes2HexStr(byte[] src, boolean upperCase) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for(int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return upperCase? stringBuilder.toString().toUpperCase() : stringBuilder.toString();
    }

    /**
     * 将16进制转为二进制
     * @param hexString 16进制
     * @return 二进制
     */
    public static byte[] parseHexStr2Bytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    /**
     * Base64加密
     * @param src 二进制
     * @return BASE64密文
     * @throws Exception
     */
    public static String encryptBASE64(byte[] src) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(src);
    }

    /**
     * BASE64加密
     * @param src 字符串
     * @return BASE64密文
     * @throws Exception
     */
    public static String encryptBASE64(String src) throws Exception {
        return encryptBASE64(src.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64解密
     * @param src BASE64密文
     * @return 二进制
     * @throws Exception
     */
    public static byte[] decryptBASE64(String src) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(src);
    }


    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
