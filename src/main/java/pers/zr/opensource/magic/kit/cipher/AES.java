package pers.zr.opensource.magic.kit.cipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * AES对称加密
 * secret key length:	128bit, default:	128 bit<br/>
 * mode:	ECB/CBC/PCBC/CTR/CTS/CFB/CFB8 to CFB128/OFB/OBF8 to OFB128<br/>
 * padding:	Nopadding/PKCS5Padding/ISO10126Padding/
 * @author zhurong
 *
 */
public class AES extends Coder {

    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 相同密钥对同一个明文多次加密结果一样
     * 加密维度：密钥（长度16bytes）
     */
    private static final String AES_ECB_PKCS5Padding = "AES/ECB/PKCS5Padding";

    /**
     * 相同密钥和iv对同一个明文多次加密结果一样
     * 加密维度：密钥（长度16bytes）、iv（长度16bytes）
     */
    private static final String AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";



    /**
     * 随机生成AES密钥
     *
     * @return AES密钥
     * @throws Exception
     */
    public static Key initSecretKey() throws Exception{
        //返回生成指定算法的秘密密钥的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);

        //初始化此密钥生成器，使其具有确定的密钥大小
        //AES 要求密钥长度为 128
        kg.init(128);
        //生成一个密钥
        SecretKey  secretKey = kg.generateKey();
        return toKey(secretKey.getEncoded());
    }

    /**
     * 转换密钥
     *
     * @param key	二进制密钥
     * @return AES密钥
     */
    private static Key toKey(byte[] key){
        if(key == null || key.length ==0) {
            return null;
        }
        //生成密钥
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * 转换密钥
     * @param key 字符串密钥
     * @return AES密钥
     */
    private static Key toKey(String key) {
        //生成密钥
        return toKey(key.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * AES/ECB/PKCS5Padding 加密(密钥相同，相同明文加密结果一样)
     * @param plainText 明文
     * @param key 字符串密钥，长度16
     * @return 二进制密文（可以转成BASE64或者16进制字符串）
     * @throws Exception
     */
    public static byte[] encryptECB(String plainText, String key) throws Exception {

        return encryptECB(plainText, toKey(key));
    }

    /**
     * AES/ECB/PKCS5Padding 加密(密钥相同，相同明文加密结果一样)
     * @param plainText 明文
     * @param key 二进制密钥
     * @return 二进制密文（可以转成BASE64或者16进制字符串）
     * @throws Exception
     */
    public static byte[] encryptECB(String plainText, byte[] key) throws Exception {

        return encryptECB(plainText, toKey(key));
    }

    /**
     * AES/ECB/PKCS5Padding 加密(密钥相同，相同明文加密结果一样)
     * @param plainText 明文
     * @param key 密钥
     * @return 二进制密文（可以转成BASE64或者16进制字符串）
     * @throws Exception
     */
    public static byte[] encryptECB(String plainText, Key key) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5Padding);
        //使用密钥初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //执行操作
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * AES/ECB/PKCS5Padding 解密
     * @param cipherText 二进制密文
     * @param key 字符串密钥（长度16）
     * @return 字符串明文
     * @throws Exception
     */
    public static String decryptECB(byte[] cipherText, String key) throws Exception {

        return decryptECB(cipherText, toKey(key));
    }

    /**
     * AES/ECB/PKCS5Padding 解密
     * @param cipherText 二进制密文
     * @param key 二进制密钥
     * @return 字符串明文
     * @throws Exception
     */
    public static String decryptECB(byte[] cipherText, byte[] key) throws Exception {

        return decryptECB(cipherText, toKey(key));
    }

    /**
     * AES/ECB/PKCS5Padding 解密
     * @param cipherText 二进制密文
     * @param key 密钥
     * @return 字符串明文
     * @throws Exception
     */
    public static String decryptECB(byte[] cipherText, Key key) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(AES_ECB_PKCS5Padding);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key);
        //执行操作
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }

    /**
     * AES/CBC/PKCS5Padding 加密
     * @param plainText 字符串明文
     * @param key 字符串密钥（长度16）
     * @param iv 字符串向量
     * @return 二进制密文（可以转成BASE64或者16进制字符串）
     * @throws Exception
     */
    public static byte[] encryptCBC(String plainText, String key, String iv) throws Exception {

        return encryptCBC(plainText, toKey(key), iv);
    }

    /**
     * AES/CBC/PKCS5Padding 加密
     * @param plainText 字符串明文
     * @param key 二进制密钥
     * @param iv 字符串向量
     * @return 二进制密文（可以转成BASE64或者16进制字符串）
     * @throws Exception
     */
    public static byte[] encryptCBC(String plainText, byte[] key, String iv) throws Exception {

        return encryptCBC(plainText, toKey(key), iv);
    }

    /**
     * AES/CBC/PKCS5Padding 加密
     * @param plainText 字符串明文
     * @param key 密钥
     * @param iv 字符串向量
     * @return 二进制密文（可以转成BASE64或者16进制字符串）
     * @throws Exception
     */
    public static byte[] encryptCBC(String plainText, Key key, String iv) throws Exception {

        return encryptCBC(plainText, key, iv.getBytes(StandardCharsets.UTF_8));

    }

    /**
     * AES/CBC/PKCS5Padding 加密
     * @param plainText 字符串明文
     * @param key 密钥
     * @param iv 二进制向量 (16bytes)
     * @return 二进制密文（可以转成BASE64或者16进制字符串）
     * @throws Exception
     */
    public static byte[] encryptCBC(String plainText, Key key, byte[] iv)throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding);
        //使用密钥和iv初始化，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * AES/CBC/PKCS5Padding 解密
     * @param cipherText 二进制密文
     * @param key 字符串密钥
     * @param iv 字符串向量值
     * @return 字符串明文
     * @throws Exception
     */
    public static String decryptCBC(byte[] cipherText, String key, String iv) throws Exception {

        return decryptCBC(cipherText, toKey(key), iv);
    }

    /**
     * AES/CBC/PKCS5Padding 解密
     * @param cipherText 二进制密文
     * @param key 二进制密钥
     * @param iv 字符串向量值
     * @return 字符串明文
     * @throws Exception
     */
    public static String decryptCBC(byte[] cipherText, byte[] key, String iv) throws Exception {

        return decryptCBC(cipherText, toKey(key), iv);
    }

    /**
     * AES/CBC/PKCS5Padding 解密
     * @param cipherText 二进制密文
     * @param key 密钥
     * @param iv 字符串向量值
     * @return 字符串明文
     * @throws Exception
     */
    public static String decryptCBC(byte[] cipherText, Key key, String iv) throws Exception {

        return decryptCBC(cipherText, key, iv.getBytes(StandardCharsets.UTF_8));

    }

    /**
     * AES/CBC/PKCS5Padding 解密
     * @param cipherText 二进制密文
     * @param key 密钥
     * @param iv 二进制向量值 (16bytes)
     * @return 字符串明文
     * @throws Exception
     */
    public static String decryptCBC(byte[] cipherText, Key key, byte[] iv) throws Exception {
        //实例化
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5Padding);
        //使用密钥和iv初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);

    }


    public static void main(String[] args) throws Exception {
        String data ="AES数据";

        //****************************************************************
        //** AES/ECB/PKCS5Padding 加解密测试
        //****************************************************************
        System.out.println("AES/ECB/PKCS5Padding 加解密测试...");
        String aesKey = "1234567890123456";

        System.out.println("加密前数据: " + data);
        byte[] encryptData = encryptECB(data, aesKey);
        System.out.println("加密后数据[hexStr]: "+ parseBytes2HexStr(encryptData, true));
        String decryptData = decryptECB(encryptData, aesKey);
        System.out.println("解密后数据: "+ decryptData);


        //****************************************************************
        //** AES/CBC/PKCS5Padding 加解密测试
        //****************************************************************
        System.out.println("AES/CBC/PKCS5Padding 加解密测试...");
        String cbcKey = "1234567890123456";
        String iv = "9876543210123465";
        System.out.println("加密前数据：" + data + ", 长度: " + data.getBytes().length);

        encryptData = encryptCBC(data, cbcKey, iv);
        System.out.println("加密后数据[hexStr]: " + parseBytes2HexStr(encryptData, true));
        decryptData = decryptCBC(encryptData, cbcKey, iv);
        System.out.println("解密后数据：" + decryptData);

        encryptData = encryptCBC(data, toKey(cbcKey), iv.getBytes());
        System.out.println("加密后数据[hexStr]: " + parseBytes2HexStr(encryptData, true) + "，长度：" + encryptData.length);
        decryptData = decryptCBC(encryptData, toKey(cbcKey), iv.getBytes());
        System.out.println("解密后数据：" + decryptData);

    }

}
