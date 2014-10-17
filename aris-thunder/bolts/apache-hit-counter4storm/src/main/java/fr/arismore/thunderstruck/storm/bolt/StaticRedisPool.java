package fr.arismore.thunderstruck.storm.bolt;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * User: lukeskypator
 * Date: 11/28/13
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class StaticRedisPool {

//    public static StaticRedisPool _instance;

    public static final String LISTNAME = System.getProperty("redis.server.source.list","redishitcounter");
    private static final String REDIS_SERVER_HOST = System.getProperty("redis.server.host", "localhost");

    public static final JedisPool pool = new JedisPool(new JedisPoolConfig(), REDIS_SERVER_HOST);
/*
    private StaticRedisPool() {
    }

    public static synchronized StaticRedisPool getInstance() {
        if (!(_instance != null)) {
            _instance = new StaticRedisPool();
        }
        return _instance;
    }*/

    public static String getListname() {
        return LISTNAME;
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }
}
