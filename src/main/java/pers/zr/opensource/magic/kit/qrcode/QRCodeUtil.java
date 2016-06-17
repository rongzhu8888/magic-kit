package pers.zr.opensource.magic.kit.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * 二维码生成工具
 */
public class QRCodeUtil {
    public static final String IMAGE_FORMAT_PNG = "png";
    public static final String IMAGE_FORMAT_JPG = "jpg";
    public static final String IMAGE_FORMAT_GIF = "gif";

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    public static byte[] generate(String text, String format, int width, int height) throws Exception {
        BitMatrix bitMatrix = buildMatrix(text, width, height);
        return writeToBytes(bitMatrix, format);
    }

    public static void generate(String text, String format, int width, int height, File file) throws Exception {
        BitMatrix bitMatrix = buildMatrix(text, width, height);
        writeToFile(bitMatrix, format, file);
    }

    private static BitMatrix buildMatrix(String text, int width, int height) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");	// 内容所使用字符集编码
        return new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);

    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    private static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format [" + format + "] to " + file);
        }
    }

    private static byte[] writeToBytes(BitMatrix matrix, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, baos)) {
            throw new IOException("Could not write an image of format [" + format + "] to ByteArrayOutputStream");
        }
        baos.flush();
        byte [] bytes = baos.toByteArray();
        baos.close();
        return bytes;
    }



}

