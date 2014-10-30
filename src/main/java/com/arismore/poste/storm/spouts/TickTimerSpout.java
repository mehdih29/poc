package com.arismore.poste.storm.spouts;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class TickTimerSpout extends BaseRichSpout {

	private static final long serialVersionUID = 1L;
	public static Logger LOG = LoggerFactory.getLogger(TickTimerSpout.class);
	boolean _isDistributed;
	SpoutOutputCollector _collector;
	private static ArrayList<Date> dates = null;
	private static Timer timer = null;

	public TickTimerSpout() {
		this(true);
	}

	public TickTimerSpout(boolean isDistributed) {
		_isDistributed = isDistributed;
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		dates = new ArrayList<Date>();
		timer = new Timer(); // At this line a new Thread will be created
		timer.schedule(new RemindTask(), 0, 60 * 1000); // delay in milliseconds
	}

	public void close() {

	}

	public void nextTuple() {
		if (TickTimerSpout.dates.isEmpty()) {
			Utils.sleep(1000);
		} else {
			_collector.emit(new Values(TickTimerSpout.dates.remove(0)));
		}
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
		Config conf = new Config();
		if (!_isDistributed) {
			conf.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
		} else {

			int tickFrequencyInSeconds = 60;
			conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS,
					tickFrequencyInSeconds);
		}
		return conf;
	}

	static class RemindTask extends TimerTask {

		@Override
		public void run() {
			TickTimerSpout.dates.add(new Date());

		}
	}

}
