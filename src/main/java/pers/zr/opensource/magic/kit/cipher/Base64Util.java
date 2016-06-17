package pers.zr.opensource.magic.kit.cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;

/**
 * BASE64加解密
 *
 */
public class Base64Util {

    public static String encrypt(String key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String key) throws Exception {
        return new String((new BASE64Decoder()).decodeBuffer(key), StandardCharsets.UTF_8);
    }

}
