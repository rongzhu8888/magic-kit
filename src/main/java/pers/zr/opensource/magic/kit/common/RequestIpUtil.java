package pers.zr.opensource.magic.kit.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


public class RequestIpUtil {

    protected final static Logger log = LoggerFactory.getLogger(RequestIpUtil.class);

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(ip != null && ip.contains(",") ) {
            ip = ip.split(",")[0];
        }

        if(log.isDebugEnabled()) {
            log.debug("request ip = [" + ip + "]");
        }

        return ip;
    }
}
