package pers.zr.opensource.magic.kit.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

/**
 *
 * RedisCluster分布式锁
 *
 */

public class RedisClusterLock {

    private String key;

    private int expireSeconds; //key生命周期（过期时间）

    private boolean acquired = false;

    private String value = null;

    private int retryIntervalMs; //重试加锁间隔时间(毫秒）

    private int retryTimes = 0; //重试加锁次数

    private final Logger log = LoggerFactory.getLogger(getClass());

    public RedisClusterLock(String key, int expireSeconds) {
        this.key = key;
        this.expireSeconds = expireSeconds;
    }

    public RedisClusterLock(String key, int expireSeconds, int retryTimes, int retryIntervalMs) {
        this.key = key;
        this.expireSeconds = expireSeconds;
        this.retryTimes = retryTimes;
        this.retryIntervalMs = retryIntervalMs;
    }

    /**
     * 加锁
     * @param jedisCluster
     * @return
     */
    public boolean acquire(JedisCluster jedisCluster) {
        int count = this.retryTimes;
        do{
            String lockValue = "Thread" + Thread.currentThread().getId() + "|" + System.nanoTime();
            //如果setnx ok，则表示redis中没有针对当前key的锁
            if(jedisCluster.setnx(this.key, lockValue) == 1) {
                //取得锁后设置过期时间
                jedisCluster.expire(this.key, this.expireSeconds);
                this.acquired = true;
                this.value = lockValue;
                if(log.isDebugEnabled()) {
                    log.debug("key [" + this.key + "] locked!");
                }
                return true;
            }
            count--;
            if(count >= 0) {
                try {
                    Thread.sleep(this.retryIntervalMs);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        } while(count >= 0);

        return false;

    }

    /**
     * 释放锁
     * @param jedisCluster
     */
    public void release(JedisCluster jedisCluster) {
        //只允许释放自己加的锁
        //某些特定情况下，可能导致加锁成功后的业务处理耗时过长，这时候锁已经失效，可能被其它线程对该码再次加锁成功，
        //这时候便不能直接对其粗暴解锁，还需要根据value值进行判断当前锁是否为自己加上去的，以防影响其它线程的处理过程。
        //
        if(this.acquired && this.value!=null && this.value.equals(jedisCluster.get(key))) {
            jedisCluster.del(key);
            this.acquired = false;
            this.value = null;
        }

    }




}
