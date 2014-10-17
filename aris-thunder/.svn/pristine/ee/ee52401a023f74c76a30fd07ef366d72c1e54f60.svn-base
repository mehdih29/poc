package fr.arismore.thunderstruck.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: lukeskypator
 * Date: 11/28/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DummyLoggerBolt extends BaseRichBolt {

    private static final Logger logger = LoggerFactory.getLogger(DummyLoggerBolt.class);
    OutputCollector _collector;

    @Override
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        logger.info("Emitted tuple {}", new Object[] {tuple.getString(0)});
        _collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
