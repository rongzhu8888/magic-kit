package pers.zr.opensource.magic.kit.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RuntimeContextHolder {

    private static ThreadLocal<Map<String, Object>> contextData = new ThreadLocal<>();

    private static final Logger log = LoggerFactory.getLogger(RuntimeContextHolder.class);

    public static void clear() {
        log.debug("clear thread local data");
        contextData.remove();
    }

    public static void putData(String key, Object data) {
        if(data != null) {
            Map<String, Object> dataMap = contextData.get();
            if(dataMap == null) {
                dataMap = new HashMap<>();
                dataMap.put(key, data);
                contextData.set(dataMap);
            }
        }
    }

    public static Object getData(String key) {
        Object data = null;
        if(key != null) {
            Map<String, Object> dataMap = contextData.get();
            if(dataMap != null) {
                data = dataMap.get(key);
            }
        }
        return data;
    }

}
