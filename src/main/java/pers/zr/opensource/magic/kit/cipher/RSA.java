package pers.zr.opensource.magic.kit.cipher;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA非对称加密
 * @author zhurong
 */
public class RSA extends Coder {

    public static final String KEY_ALGORITHM = "RSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";


    /**
     * 初始化公钥和私钥对
     * @return 密钥对
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);

        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String, Object> keyMap = new HashMap<String, Object>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 获取私钥
     * @param keyMap 密钥对
     * @return 私钥
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);

        return parseBytes2HexStr(key.getEncoded(), true);
    }

    /**
     * 获取公钥
     * @param keyMap 密钥对
     * @return 公钥
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);

        return parseBytes2HexStr(key.getEncoded(), true);
    }

    /**
     * 公钥加密
     * @param data 二进制明文
     * @param key 公钥
     * @return 二进制密文
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key) throws Exception {
        // 对公钥解密
        byte[] keyBytes = parseHexStr2Bytes(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 私钥加密
     * @param data 二进制明文
     * @param key 私钥
     * @return 二进制密文
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(String data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = parseHexStr2Bytes(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 公钥解密
     * @param data 二进制密文
     * @param key 公钥
     * @return 二进制明文
     * @throws Exception
     */
    public static String decryptByPublicKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = parseHexStr2Bytes(key);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     * @param data 二进制密文
     * @param key 私钥
     * @return 二进制明文
     * @throws Exception
     */
    public static String decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = parseHexStr2Bytes(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
    }


    public static void main(String []args) throws Exception {

        Map<String, Object> keyMap = initKey();
        String publicKey = getPublicKey(keyMap);
        String privateKey = getPrivateKey(keyMap);
        System.out.println("公钥：" + publicKey);
        System.out.println("公钥长度: " + publicKey.length());
        System.out.println("私钥：" + privateKey);
        System.out.println("私钥长度：" + privateKey.length());

        String origin = "RSA测试@!#$@#$#%$^SDFGSDFG()(*&*&*^^&123??::LK";
        System.out.println("加密前：" + origin);

        byte[] encryptValue = encryptByPrivateKey(origin, privateKey);
        System.out.println("私钥加密后：" + parseBytes2HexStr(encryptValue, true));

        String decryptValue = decryptByPublicKey(encryptValue, publicKey);
        System.out.println("公钥解密后: " + decryptValue);

    }


}
