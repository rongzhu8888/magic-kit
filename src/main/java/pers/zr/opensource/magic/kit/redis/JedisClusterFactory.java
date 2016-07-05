
package pers.zr.opensource.magic.kit.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <p>
 * JedisCluster工厂类<br>
 * 通过Spring FactoryBean创建JedisCluster
 * </p>
 *
 * <p>
 * 使用方法：<br>
 * 第一步：在applicationContext.xml中加载redis.properties，并注入该类<br>
 * 第二步：在相关使用到redis的类中直接依赖注入JedisCluster:
 * <code>@Autowired private JedisCluster jedisCluster;</code>
 *</p>
 *
 * @author zhurong
 *
 *
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {

    private Resource addressConfig;
    private String addressKeyPrefix;

    private JedisCluster jedisCluster;
    private int connectionTimeout;
    private int socketTimeout;
    private int maxRedirections;
    private GenericObjectPoolConfig genericObjectPoolConfig;

    private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    public JedisCluster getObject() throws Exception {
        return jedisCluster;
    }

    public Class<? extends JedisCluster> getObjectType() {
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    public boolean isSingleton() {
        return true;
    }

    private Set<HostAndPort> parseHostAndPort() throws Exception {
        try {
            Properties prop = new Properties();
            prop.load(this.addressConfig.getInputStream());

            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            for (Object key : prop.keySet()) {

                if (!((String) key).startsWith(addressKeyPrefix)) {
                    continue;
                }

                String val = (String) prop.get(key);

                boolean isIpPort = p.matcher(val).matches();

                if (!isIpPort) {
                    throw new IllegalArgumentException("ip 或 port 不合法");
                }
                String[] ipAndPort = val.split(":");

                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }

            return haps;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new Exception("Failed to load redis cluster config file", ex);
        }
    }

    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> hostAndPorts = this.parseHostAndPort();
        jedisCluster = new JedisCluster(hostAndPorts, connectionTimeout, socketTimeout,
                maxRedirections, genericObjectPoolConfig);

    }

    public void setAddressConfig(Resource addressConfig) {
        this.addressConfig = addressConfig;
    }


    public void setMaxRedirections(int maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public void setAddressKeyPrefix(String addressKeyPrefix) {
        this.addressKeyPrefix = addressKeyPrefix;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}
