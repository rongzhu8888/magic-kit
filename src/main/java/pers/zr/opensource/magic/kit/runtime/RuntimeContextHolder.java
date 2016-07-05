package pers.zr.opensource.magic.kit.runtime;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class RuntimeContextHolder {

    private static ThreadLocal<Map<String, Object>> contextData = new ThreadLocal<>();

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
