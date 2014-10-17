package fr.arismore.thunderstruck.storm.bolt;

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
 * Date: 12/4/13
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class BulkAccessAnalyserSpout extends BaseRichBolt {

    private static final Logger logger = LoggerFactory.getLogger(BulkAccessAnalyserSpout.class);
    OutputCollector _collector;

    @Override
    public void prepare(Map map, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }
}
