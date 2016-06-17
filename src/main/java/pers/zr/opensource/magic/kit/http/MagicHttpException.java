package pers.zr.opensource.magic.kit.http;

public class MagicHttpException extends Exception {

    public MagicHttpException(String msg) {
        super(msg);
    }


    public MagicHttpException(Throwable cause) {
        super(cause);
    }

    public MagicHttpException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
