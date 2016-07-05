package pers.zr.opensource.magic.kit.response;

import java.io.Serializable;

/**
 *  描述操作结果的response
 *
 */
public class CommonResponse implements Serializable {
    private static final long serialVersionUID = -2564445585181441109L;

    private int code;
    private String message;

    public CommonResponse() {}

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CommonResponse buildSuccessResponse() {
        return new CommonResponse(CommonResponseCode.SUCCESS, "ok");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
