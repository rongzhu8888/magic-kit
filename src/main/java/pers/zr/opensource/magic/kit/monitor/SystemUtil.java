package pers.zr.opensource.magic.kit.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.zr.opensource.magic.kit.cipher.Coder;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by peter on 2016/3/29.
 *
 */
public class SystemUtil {

    private static Map<String, String> sysAttrMap = new HashMap<>();

    private static final String SYS_HOST = "Host";
    private static final String SYS_OS = "OS";
    private static final String SYS_CPU = "CPU";
    private static final String SYS_LOCALE = "Locale";
    private static final String SYS_MAC = "MAC";

    private static Logger log = LoggerFactory.getLogger(SystemUtil.class);

    public static String getHost() {
        String host = sysAttrMap.get(SYS_HOST);
        if(host == null) {
            try {
                InetAddress localHost = InetAddress.getLocalHost();
                host = localHost.getHostName() + "/" + localHost.getHostAddress();
                sysAttrMap.put(SYS_HOST, host);
            } catch (UnknownHostException e) {
                log.error("Failed to retrive host address", e);
            }
        }
        return host;
    }

    public static String getMac() {
        String mac = sysAttrMap.get(SYS_MAC);
        if(mac == null) {
            try{
                InetAddress localHost = InetAddress.getLocalHost();
                NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
                byte[] macAddr = networkInterface.getHardwareAddress();
                mac = formatMacAddr(Coder.parseBytes2HexStr(macAddr, true));
                sysAttrMap.put(SYS_MAC, mac);
            }catch (Exception e) {
                log.error("Failed to retrive mac address", e);
            }
        }
        return mac;
    }

    public static String getOS() {
        String os = sysAttrMap.get(SYS_OS);
        if(os == null) {
            os = System.getProperty("os.name") + " " + System.getProperty("os.version");
            sysAttrMap.put(SYS_OS, os);
        }
        return os;
    }

    public static String getCPU() {
        String cpu = sysAttrMap.get(SYS_CPU);
        if(cpu == null) {
            cpu = System.getProperty("os.arch", "") + ", " + String.valueOf(Runtime.getRuntime().availableProcessors()) + " cores";
            sysAttrMap.put(SYS_CPU, cpu);
        }
        return cpu;
    }

    public static String getLocale() {
        String locale = sysAttrMap.get(SYS_LOCALE);
        if(locale == null) {
            locale = Locale.getDefault().toString() + "/" + System.getProperty("file.encoding");
            sysAttrMap.put(SYS_LOCALE, locale);
        }
        return locale;
    }

    private static String formatMacAddr(String mac) {
        if(mac != null && !mac.contains("-")) {
            StringBuffer formatMac = new StringBuffer();
            for(int i=0; i<mac.length(); i++) {
                if(i>0 && i%2==0) {
                    formatMac.append("-");
                }
                formatMac.append(mac.charAt(i));
            }
            return formatMac.toString();
        }else {
            return mac;
        }
    }


    public static void main(String []rags) throws Exception{
        System.out.println(getHost());
        System.out.println(getMac());
        System.out.println(getOS());
        System.out.println(getCPU());
        System.out.println(getLocale());

    }
}
