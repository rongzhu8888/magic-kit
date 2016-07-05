package pers.zr.opensource.magic.kit.file;

/**
 *
 * 读取文件时的回调函数<br>
 * 如果想对文件的每一行做处理的话，实现该接口即可
 *
 * Created by peter.zhu on 2016/3/9.
 *
 */
public interface FileReaderCallback {

    public void executeForEachLine(String line);


}
