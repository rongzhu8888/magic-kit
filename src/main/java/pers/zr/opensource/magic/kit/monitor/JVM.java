package pers.zr.opensource.magic.kit.monitor;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by peter on 2016/3/29.
 *
 */

@Getter
@Setter
public class JVM {

    private String jdk;
    private String name;
    private String version;
    private List<String> libraries;
    private Map<String, String> properties; //属性
    private String uptime; //运行时间
    private Date startTime; //启动时间
    private Date currentTime; //当前时间
}
