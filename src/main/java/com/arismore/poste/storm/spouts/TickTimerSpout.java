package com.arismore.poste.storm.spouts;

import backtype.storm.Config;
import backtype.storm.topology.OutputFieldsDeclarer;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TickTimerSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	public static Logger LOG = LoggerFactory.getLogger(XMLSoput.class);
	boolean _isDistributed;
	SpoutOutputCollector _collector;

	public TickTimerSpout() {
		this(true);
	}

	public TickTimerSpout(boolean isDistributed) {
		_isDistributed = isDistributed;
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
	}

	public void close() {

	}

	public void nextTuple() {
		Utils.sleep(60000);
		Date date = new Date();
		_collector.emit(new Values(date));
	}

	public void ack(Object msgId) {

	}

	public void fail(Object msgId) {

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("date"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		if (!_isDistributed) {
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
			return ret;
		} else {
			return null;
		}
	}
}
