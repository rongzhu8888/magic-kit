package pers.zr.opensource.magic.kit.monitor;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;

/**
 * Created by peter on 2016/3/29.
 *
 */
public class JVMUtil {

    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;

    public static JVM getJVM() {
        RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
        JVM jvm = new JVM();
        jvm.setJdk(System.getProperty("java.runtime.name") + " " + System.getProperty("java.runtime.version"));
        jvm.setName(bean.getVmName());
        jvm.setVersion(bean.getVmVersion());
        List<String> libs = getLibraries(bean.getClassPath());
        jvm.setLibraries(libs);
        jvm.setCurrentTime(new Date());
        jvm.setStartTime(new Date(bean.getStartTime()));
        jvm.setUptime(formatUptime(bean.getUptime()));
        jvm.setProperties(bean.getSystemProperties());
        return jvm;
    }

    private static String formatUptime(long uptime) {
        StringBuilder buf = new StringBuilder();
        if (uptime > DAY) {
            long days = (uptime - uptime % DAY) / DAY;
            buf.append(days);
            buf.append(" Days");
            uptime = uptime % DAY;
        }
        if (uptime > HOUR) {
            long hours = (uptime - uptime % HOUR) / HOUR;
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(hours);
            buf.append(" Hours");
            uptime = uptime % HOUR;
        }
        if (uptime > MINUTE) {
            long minutes = (uptime - uptime % MINUTE) / MINUTE;
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(minutes);
            buf.append(" Minutes");
            uptime = uptime % MINUTE;
        }
        if (uptime > SECOND) {
            long seconds = (uptime - uptime % SECOND) / SECOND;
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(seconds);
            buf.append(" Seconds");
            uptime = uptime % SECOND;
        }
        if (uptime > 0) {
            if (buf.length() > 0) {
                buf.append(", ");
            }
            buf.append(uptime);
            buf.append(" Milliseconds");
        }
        return buf.toString();
    }

    private static List<String> getLibraries(String classpath) {
        List<String> list = new ArrayList<>();
        if(classpath != null) {
            String[] array = classpath.split(";");
            if(array.length >0) {
                for(int i=0; i<array.length; i++) {
                   list.add(array[i].substring(array[i].lastIndexOf(File.separator)+1));
                }
            }
        }
        return list;
    }

    public static void main(String []args) {
        long time1 = System.currentTimeMillis();
        System.out.println(JSON.toJSONString(getJVM()));
        long time2 = System.currentTimeMillis();
        System.out.println(time2-time1);

    }
}
