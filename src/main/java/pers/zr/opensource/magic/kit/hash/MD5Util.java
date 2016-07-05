package pers.zr.opensource.magic.kit.hash;

import java.security.MessageDigest;

/**
 * MD5摘要加密
 */
public class MD5Util {
    public static String MD5(String s) throws Exception{
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

        byte[] btInput = s.getBytes();
        // 获得MD5摘要算法的 MessageDigest 对象
        MessageDigest mdInst = MessageDigest.getInstance("MD5");
        // 使用指定的字节更新摘要
        mdInst.update(btInput);
        // 获得密文
        byte[] md = mdInst.digest();
        // 把密文转换成十六进制的字符串形式
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);

    }

    public static void main(String args[]) throws Exception {
        long time1 = System.currentTimeMillis();
        String s = "18621864104" + "ijgr543dw";
        System.out.println(MurmurHashUtil.hash(s));
        long time2 = System.currentTimeMillis();
        System.out.println(time2- time1);
    }
}
