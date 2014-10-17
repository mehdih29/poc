package fr.arismore.thunderstruck.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lukeskypator
 * Date: 11/28/13
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class RedisSpout extends BaseRichSpout {

    private static final Logger logger = LoggerFactory.getLogger(RedisSpout.class);
    private static JedisPool pool;
    private static String redisServerHost = "localhost";
    SpoutOutputCollector _collector;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector collector) {
        System.out.println("Opening RedisSpout");
        _collector = collector;
        mapConfig(map);
        pool = new JedisPool(new JedisPoolConfig(), redisServerHost);
    }

    @Override
    public void nextTuple() {
        System.out.println("Computing next Tuple for RedisSpout");
        Jedis jedis = pool.getJedis();
        try {
            List<String> futureTuple = jedis.blpop(0, StaticRedisPool.getListname());
            _collector.emit(new Values(futureTuple.get(1)));
            pool.returnResource(jedis);
        } catch (JedisException e) {
            pool.returnBrokenResource(jedis);
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        System.out.println("Declaring RedisSpout's output fields");
        declarer.declare(new Fields("message"));
    }

    private void mapConfig(Map map) {
        if (map.get("REDIS_SERVER_HOST") != null) {
            redisServerHost = map.get("REDIS_SERVER_HOST");
        }
    }

}
