package org.swasth.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.io.File;
import java.io.Serializable;

public final class DedupEngine implements Serializable {

    private final long serialVersionUID = 6089562751616425354L;

    public static  RedisConnect redisConnect;
    public static  int store;
    public static int  expirySeconds;

    private static Jedis redisConnection = redisConnect.getConnection();


    public DedupEngine(RedisConnect redisConnect,int store,int expirySeconds){
        DedupEngine.redisConnect =redisConnect;
        DedupEngine.store = store;
        DedupEngine.expirySeconds = expirySeconds;

    }

    public static Boolean isUniqueEvent(String checksum) throws JedisException{
        boolean unique;
        try {
            unique = !redisConnection.exists(checksum);
        } catch(JedisException ex) {
                ex.printStackTrace();
                redisConnection.close();
                redisConnection = redisConnect.getConnection(store, 10000L);
                unique = !redisConnection.exists(checksum);
        }
        return unique;
    }

    public static void storeChecksum(String checksum){
        try {
            redisConnection.setex(checksum, expirySeconds, "");
            }
          catch(JedisException ex) {
                ex.printStackTrace();
                redisConnection.close();
                redisConnection = redisConnect.getConnection(store, 10000L);
                redisConnection.select(store);
                redisConnection.setex(checksum, expirySeconds, "");
        }
    }
    public static Jedis getRedisConnection(){
        return  redisConnection ;}

    public static void  closeConnectionPool(){
        redisConnection.close();
    }
}
