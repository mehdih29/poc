package org.lsp.thunderstruck.redislogvault4apache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lukeskypator
 * Date: 11/25/13
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogInjector extends Thread {

    private static final String LISTNAME = System.getProperty("redis.server.destination.list","redislogvault4apache");
    private static final String REDIS_SERVER_HOST = "localhost";

    private static final JedisPool pool = new JedisPool(new JedisPoolConfig(), System.getProperty("redis.server.host", REDIS_SERVER_HOST));
    private static Jedis jedis;

    private static Logger logger = LoggerFactory.getLogger(LogInjector.class);

    @Override
    public void run() {
        logger.debug("Beginning injection");
        try {
            jedis = pool.getResource();
            String inputStr = null;
            StreamBuffer stream = StreamBuffer.getInstance();
            BufferedReader bufReader = stream.getBufReader();
            if (jedis.ping() != null) {
                logger.debug("Redis is alive. Reading the buffer");

                // Empty Stringbuffer
                while ((inputStr=bufReader.readLine()) != null) {
                    logger.info("Input is {}", new Object[] {inputStr});
                    jedis.lpush(LISTNAME, inputStr);
                }
            }
        } catch (JedisConnectionException e) {
            logger.error(e.getMessage(), e);
            pool.returnBrokenResource(jedis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread.currentThread().interrupt();
        return;
    }

}
