package pers.zr.opensource.magic.kit.hash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 *
 * 碰撞率极低的非加密型哈希算法，性能比传统的加密型（MD5、SHA1）快许多。
 *
 * 应用场景：获取Key值的hash，重复交易验证
 *
 * This is a very fast, non-cryptographic hash suitable for general hash-based lookup. See
 * http://murmurhash.googlepages.com/ for more details. <br>
 * <p>
 * The C version of MurmurHashUtil 2.0 found at that site was ported to Java by Andrzej Bialecki (ab at
 * getopt org).
 * </p>
 */
public class MurmurHashUtil {
    /**
     * Hashes bytes in an array.
     * @param data The bytes to hash.
     * @param seed The seed for the hash.
     * @return The 32 bit hash of the bytes in question.
     */
    public static int hash(byte[] data, int seed) {
        return hash(ByteBuffer.wrap(data), seed);
    }

    /**
     * Hashes bytes in part of an array.
     * @param data The data to hash.
     * @param offset Where to start munging.
     * @param length How many bytes to process.
     * @param seed The seed to start with.
     * @return The 32-bit hash of the data in question.
     */
    public static int hash(byte[] data, int offset, int length, int seed) {
        return hash(ByteBuffer.wrap(data, offset, length), seed);
    }

    /**
     * Hashes the bytes in a buffer from the current position to the limit.
     * @param buf The bytes to hash.
     * @param seed The seed for the hash.
     * @return The 32 bit murmur hash of the bytes in the buffer.
     */
    public static int hash(ByteBuffer buf, int seed) {
        // save byte order for later restoration
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        int m = 0x5bd1e995;
        int r = 24;

        int h = seed ^ buf.remaining();

        int k;
        while (buf.remaining() >= 4) {
            k = buf.getInt();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, use this first:
            // finish.position(4-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getInt();
            h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        buf.order(byteOrder);
        return h;
    }

    public static long hash64A(byte[] data, int seed) {
        return hash64A(ByteBuffer.wrap(data), seed);
    }

    public static long hash64A(byte[] data, int offset, int length, int seed) {
        return hash64A(ByteBuffer.wrap(data, offset, length), seed);
    }

    public static long hash64A(ByteBuffer buf, int seed) {
        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }

    public static long hash(byte[] key) {
        return hash64A(key, 0x1234ABCD);
    }

    public static long hash(String key) {
        return hash(key.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        long hash;
        long start = System.currentTimeMillis();
//        for(int i=0; i<1000000; i++) {
            hash = hash("qwerqwersdfasfd29348729374jkasdfjha;sf;as;fdajsldfj298sajdlfkjasd892o0");

//        }
        long end = System.currentTimeMillis();
        System.out.println(hash);

    }
}
