package pers.zr.opensource.magic.kit.response;

/**
 * 包含分页对象的response
 * @param <T>
 */
public class CommonPageResponse<T> extends CommonResponse {

    private Page<T> page;

    public static <T> CommonPageResponse buildSuccessResponse(Page<T> page) {
        CommonPageResponse<T> response = new CommonPageResponse<T>();
        response.setCode(CommonResponseCode.SUCCESS);
        response.setMessage("ok");
        response.setPage(page);
        return response;
    }

    public static <T> CommonPageResponse buildFailureResponse(int code, String message) {
        CommonPageResponse<T> response = new CommonPageResponse<T>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
}
