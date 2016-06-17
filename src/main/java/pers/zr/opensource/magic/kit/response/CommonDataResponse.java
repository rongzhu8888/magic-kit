package pers.zr.opensource.magic.kit.response;

/**
 * 包含普通对象（列表）的response
 * @param <T>
 */
public class CommonDataResponse<T> extends CommonResponse {

    private T data;

    public static <T> CommonDataResponse buildSuccessResponse(T data) {
        CommonDataResponse<T> response = new CommonDataResponse<T>();
        response.setCode(CommonResponseCode.SUCCESS);
        response.setMessage("ok");
        response.setData(data);
        return response;
    }

    public static <T> CommonDataResponse buildFailureResponse(int code, String message) {
        CommonDataResponse<T> response = new CommonDataResponse<T>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
