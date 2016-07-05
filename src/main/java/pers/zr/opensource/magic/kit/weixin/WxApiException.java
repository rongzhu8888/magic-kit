package pers.zr.opensource.magic.kit.weixin;

/**
 *
 *  微信API异常.
 */
public class WxApiException extends Exception {

    public WxApiException(String msg) {
        super(msg);
    }

    public WxApiException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
