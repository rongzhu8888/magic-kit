package pers.zr.opensource.magic.kit.log;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * api log aop
 *
 * <code>
 *      <bean id="apiLogAop" class="pers.zr.opensource.magic.conf.server.log.ApiLogAop"></bean>
 *      <aop:config>
 *          <aop:aspect ref="apiLogAop">
 *              <aop:around method="doMonitor"
 *              pointcut="execution(* pers.zr.opensource.magic.conf.server..*Controller*.*(..))" />
 *          </aop:aspect>
 *      </aop:config>
 * </code>
 *
 */

public class ApiLogAop {

    private final Log log = LogFactory.getLog(ApiLogAop.class);

    public Object doMonitor(ProceedingJoinPoint pjp) {
        StringBuffer className = new StringBuffer(" ");
        StringBuffer methodName = new StringBuffer(" ");
        StringBuffer paramStr = new StringBuffer(" ");
        getJointPointInfo(pjp, className, methodName, paramStr);

        String resultStr = null;
        Long start = System.currentTimeMillis();
        long end = 0L;
        try {
            Object result = pjp.proceed();
            end = System.currentTimeMillis();
            resultStr = JSON.toJSONString(result);
            return result;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (end == 0) {
                end = System.currentTimeMillis();
            }

            log.info("invoke service=" + className + "." + methodName + ",params=["
                    + paramStr + "],result=[" + resultStr + "]"
                    + ",use time=" + (end - start) + "ms");
        }
    }

    private void getJointPointInfo(ProceedingJoinPoint pjp, StringBuffer className, StringBuffer methodName, StringBuffer paramStr){
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        className.append(method.getDeclaringClass().getName());

        methodName.append(method.getName());
        Object[] params = pjp.getArgs();
        int length = 0;
        if (params != null) {
            length = params.length;
        }

        for (int i = 0; i < length; i++) { // 组装打印参数
            paramStr.append(JSON.toJSONString(params[i])).append("|");
        }
    }
}
