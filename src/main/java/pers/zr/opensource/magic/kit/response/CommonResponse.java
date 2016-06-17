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

    public static CommonResponse buildSuccessResponse() {
        CommonResponse response = new CommonResponse();
        response.setCode(CommonResponseCode.SUCCESS);
        response.setMessage("ok");
        return response;
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
