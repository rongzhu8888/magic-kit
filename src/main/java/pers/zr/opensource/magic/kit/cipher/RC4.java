package pers.zr.opensource.magic.kit.cipher;

import java.nio.charset.StandardCharsets;

/**
 * RC4对称加密
 * 密钥长度可变
 */
public class RC4 extends Coder {

    public static byte[] encrypt2Bytes(String data, String key) throws Exception {

        return rc4(data.getBytes(StandardCharsets.UTF_8), key);

    }

    public static String encrypt2Str(String data, String key) throws Exception {

        return parseBytes2HexStr(rc4(data.getBytes(StandardCharsets.UTF_8), key), false);

    }

    public static byte[] decrypt2Bytes(String data, String key) throws Exception {

        return rc4(parseHexStr2Bytes(data), key);

    }

    public static String decrypt2String(String data, String key) throws Exception {

        return new String(rc4(parseHexStr2Bytes(data), key), StandardCharsets.UTF_8);

    }


    private static byte[] rc4(byte[] data, String key) throws Exception {
        int[] box = new int[256];
        byte[] k = key.getBytes();
        int i = 0, x = 0, t = 0, l = k.length;

        for (i = 0; i < 256; i++) {
            box[i] = i;
        }

        for (i = 0; i < 256; i++) {
            x = (x + box[i] + k[i % l]) % 256;

            t = box[x];
            box[x] = box[i];
            box[i] = t;
        }

        //
        t = 0;
        i = 0;
        l = data.length;
        int o = 0, j = 0;
        byte[] out = new byte[l];
        int[] ibox = new int[256];
        System.arraycopy(box, 0, ibox, 0, 256);

        for (int c = 0; c < l; c++) {
            i = (i + 1) % 256;
            j = (j + ibox[i]) % 256;

            t = ibox[j];
            ibox[j] = ibox[i];
            ibox[i] = t;

            o = ibox[(ibox[i] + ibox[j]) % 256];
            out[c] = (byte) (data[c] ^ o);
        }
        return out;
    }

    public static void main(String []args) throws  Exception{

        String originalValue = "asdfjasljfals哈哈asdfa@#$@#$%#$^%234";
        String key = "01234567891";
        String encryptValue = encrypt2Str(originalValue, key);
        System.out.println("加密前：" + originalValue + ",长度：" + originalValue.getBytes().length);
        System.out.println("密钥：" + key);
        System.out.println("加密后："  + encryptValue + ",长度：" + encryptValue.getBytes().length);
        String decryptValue = decrypt2String(encryptValue, key);
        System.out.println("解密后：" + decryptValue);

    }
}
