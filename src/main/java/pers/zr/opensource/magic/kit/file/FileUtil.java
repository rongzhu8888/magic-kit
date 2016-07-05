package pers.zr.opensource.magic.kit.file;

import java.io.*;

/**
 * Created by zhurong on 2016/3/9.
 *
 */
public class FileUtil {

    public static String read(String filePath, FileReaderCallback callBack) throws IOException {

        File file = new File(filePath);
        if(!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        String line;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) { //一次读一行
                sb.append(line);
                //回调：用户自定义具体执行
                if(callBack != null) {
                    callBack.executeForEachLine(line);
                }
            }
            return sb.toString();
        }finally {
            if(reader != null) {
                reader.close();
            }
        }

    }

    /**
     *
     * @param filePath 文件绝对路径
     * @param content 内容
     * @param isAttached true表示追加，false表示覆盖
     * @throws IOException
     */
    public static void write(String filePath, String content, boolean isAttached) throws IOException {

        File file = new File(filePath);
        if(!file.exists() || file.isDirectory()) {
            file.createNewFile();
        }

        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(filePath, isAttached));
            writer.write(content);
            writer.flush();
        }finally{
            if(writer != null)
                writer.close();
        }

    }



}
