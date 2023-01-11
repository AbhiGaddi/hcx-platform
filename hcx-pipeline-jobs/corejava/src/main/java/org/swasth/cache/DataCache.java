package org.swasth.cache;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.swasth.job.BaseJobConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

import static java.sql.DriverManager.println;

class DataCache {


    private final BaseJobConfig config;
    private static RedisConnect redisConnect;
    private final int dbIndex;
    private static List<String> fields = null;
    private static Jedis redisConnection;
    private static final Gson gson = new Gson();

    public static final Logger logger = LogManager.getLogger(DataCache.class);


    public DataCache(BaseJobConfig config, RedisConnect redisConnect, int dbIndex, List<String> fields) {
        this.config = config;
        this.redisConnect = redisConnect;
        this.dbIndex = dbIndex;
        this.fields = fields;
    }

    public void init() {
        this.redisConnection = redisConnect.getConnection(dbIndex);
    }

    public  void close() {
        this.redisConnection.close();
    }

    public Map<String,Object> hgetAllWithRetry( String key){
        try {
            return  convertToComplexDataTypes(hgetAll(key));
        } catch(JedisException ex){
                logger.error("Exception when retrieving data from redis cache", ex);
                this.redisConnection.close();
                this.redisConnection = redisConnect.getConnection(dbIndex);
                return convertToComplexDataTypes(hgetAll(key));
        }
    }

    public Boolean isArray(String value){
         String redisValue = value.trim();
         return redisValue.length() > 0 && redisValue.startsWith("[");
    }

    public void  isObject(String value)  {
        String redisValue = value.trim();
        redisValue.length() > 0 && redisValue.startsWith("{")
    }

    public Map<String,Object> convertToComplexDataTypes(Map<String,String> data) {
        Map<String,Object> result = new HashMap<>();
        data.keys.map {
            redisKey =>
            String redisValue = data(redisKey);
            if(isArray(redisValue)) {
                result += redisKey -> gson.fromJson(redisValue, new ArrayList.getClass());
            } else if (isObject(redisValue)) {
                result += redisKey -> gson.fromJson(redisValue, new util.HashMap[String, AnyRef]().getClass)
            } else {
                result += redisKey -> redisValue
            }
        }
        return result;
    }
    private static Map<String,String> hgetAll(String key) {
        Map<String, String> dataMap = redisConnection.hgetAll(key);
        if (dataMap.size() > 0) {
            if(!fields.isEmpty()) dataMap.keySet().retainAll(fields);
             dataMap.values().removeAll(Collections.singleton(""));
             return  dataMap;
        } else {
            return new HashMap<>();
        }
    }
    public static Map<String,Object> getWithRetry(String key){
        try {
             return  get(key);
        } catch(JedisException ex) {
                logger.error("Exception when retrieving data from redis cache", ex);
                redisConnection.close();
                redisConnect.getConnection();
                return  get(key);
        }

    }

    private static HashMap<String,Object> get(String key){
        String data = redisConnection.get(key);
        if (data != null && !data.isEmpty()) {
            HashMap<String,Object> dataMap = gson.fromJson(data, HashMap.class);
            if(!fields.isEmpty()) dataMap.keySet().retainAll(fields);
            dataMap.values().removeAll(Collections.singleton(""));
            return  dataMap;
        } else {
            return new HashMap<>();
        }
    }
    public static List<Map<String,Object>> getMultipleWithRetry(List<String> keys){
        for (key  keys) yield {
          return   getWithRetry(key);
        }
    }

    public static Boolean isExists(String key){
         return redisConnection.exists(key);
    }


    public static void hmSet(String key,Map<String,Object> value) {
        try {
             redisConnection.hmset(key,value);
        } catch(JedisException ex) {
            // Write testcase for catch block
            // $COVERAGE-OFF$ Disabling scoverage
                println("dataCache");
                logger.error("Exception when inserting data to redis cache", ex)
                redisConnection.close();
                redisConnection = redisConnect.getConnection(dbIndex);
                redisConnection.hmget(key,value);
            }
            // $COVERAGE-ON$
        }
    public void hmSet( String key,String value, int ttl){
        try {
             redisConnection.setex(key, ttl, value);
        } catch(JedisException ex) {
                logger.error("Exception when inserting data to redis cache", ex);
                redisConnection.close();
               redisConnection = redisConnect.getConnection(dbIndex);
                redisConnection.setnx(key, value);
            }
        }


    public void setWithRetry(String key, String value) throws JedisConnectionException{
        try {
            set(key, value);
        } catch(JedisException ex ) {
        // Write testcase for catch block
        // $COVERAGE-OFF$ Disabling scoverage
        logger.error("Exception when update data to redis cache", ex);
        redisConnection.close();
        redisConnection = redisConnect.getConnection(dbIndex);
        set(key, value);
        // $COVERAGE-ON$
        }
        }

    public void  set(String key,String value){
               redisConnection.setex(key, config.redisExpires, value);
        }

    }




