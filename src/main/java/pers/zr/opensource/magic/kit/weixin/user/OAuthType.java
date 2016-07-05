package pers.zr.opensource.magic.kit.weixin.user;

/**
 * OAuth Type
 */
public enum  OAuthType {

    STATIC("snsapi_base"), //静默授权

    MANUAL("snsapi_userinfo"); //用户授权

    public String value;

    private OAuthType(String value) {
        this.value = value;
    }

}
