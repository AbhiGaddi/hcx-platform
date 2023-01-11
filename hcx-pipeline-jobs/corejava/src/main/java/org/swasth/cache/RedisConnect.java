package org.swasth.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.swasth.job.BaseJobConfig;
import redis.clients.jedis.Jedis;

import java.io.Serializable;

public class RedisConnect implements Serializable {

    private final Long serialVersionUID = -396824011996012513L;
    public static String redisHost;
    public static Integer redisPort;
    public static BaseJobConfig jobConfig;

    public RedisConnect(String redisHost, Integer redisPort, BaseJobConfig jobConfig) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.jobConfig = jobConfig;
    }

    private final Logger logger = LogManager.getLogger(RedisConnect.class);

    private Jedis getConnection(long backoffTimeInMillis) {
        int defaultTimeOut = jobConfig.getRedisConnectionTimeout();
        if (backoffTimeInMillis > 0) {
            try {
                Thread.sleep(backoffTimeInMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("Obtaining new Redis connection...");
        return new Jedis(redisHost, redisPort, defaultTimeOut);
    }

    public Jedis getConnection(int db, Long backoffTimeInMillis) {
        Jedis jedis = getConnection(backoffTimeInMillis);
        jedis.select(db);
        return jedis;
    }

    public Jedis getConnection(int db){
        Jedis jedis = getConnection(db, 0L);
        jedis.select(db);
        return jedis;
    }

    public Jedis getConnection() {
        return  getConnection( 0);
    }


}



