package pers.zr.opensource.magic.kit.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zr.opensource.magic.kit.common.RequestIpUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//import eu.bitwalker.useragentutils to .UserAgent;

public class RuntimeUserContextFilter implements Filter{

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try{
            String userIp = RequestIpUtil.getIp(httpServletRequest);
            RuntimeContextHolder.putData(RuntimeUserContextConstant.USER_IP, userIp);
            RuntimeContextHolder.putData(RuntimeUserContextConstant.USER_REQUEST_TIME, new Date());
            //以下数据可以从HttpHeader中获取
//            RuntimeContextHolder.putData(RuntimeUserContextConstant.USER_LANGUAGE, "zh-CN");
//            RuntimeContextHolder.putData(RuntimeUserContextConstant.USER_ID, 0);
//            RuntimeContextHolder.putData(RuntimeUserContextConstant.USER_NAME, "");

            String userAgentStr = httpServletRequest.getHeader(RuntimeUserContextConstant.USER_AGENT);
            log.debug(userAgentStr);
//            UserAgent userAgent = new UserAgent(userAgentStr);
//            System.out.println(JSON.toJSONString(userAgent.getBrowser()));
//            System.out.println(JSON.toJSONString(userAgent.getBrowserVersion()));
//            System.out.println(JSON.toJSONString(userAgent.getOperatingSystem()));

            filterChain.doFilter(httpServletRequest, httpServletResponse);

        }catch (IOException |  ServletException e) {
            log.error("Failed to get user runtime context info from http servlet", e.getMessage());
            throw e;
        }
        finally {
            //清空线程本地变量
            RuntimeContextHolder.clear();
        }
    }

    @Override
    public void destroy() {
    }
}
