package com.arismore.poste.storm.spouts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static ArrayList<ArrayList<String>> slidingWindow = null;
	private static Timer timer = null;
	private static SimpleDateFormat formater = null;
	private static Calendar cal = null;

	public TickTimerSpout() {
		this(true);
	}

	public TickTimerSpout(boolean isDistributed) {
		_isDistributed = isDistributed;
	}

	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		_collector = collector;
		// TODO see if i have to put this things here

		TickTimerSpout.formater = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm':00Z'");
		TickTimerSpout.formater.setTimeZone(TimeZone.getTimeZone("UTC"));
		TickTimerSpout.cal = Calendar.getInstance();
		slidingWindow = new ArrayList<ArrayList<String>>();
		timer = new Timer(); // At this line a new Thread will be created
		timer.schedule(new RemindTask(), 0, 60 * 1000); // delay in milliseconds
	}

	public void close() {

	}

	public void nextTuple() {
		if (TickTimerSpout.slidingWindow.isEmpty()) {
			Utils.sleep(1000);
		} else {
			ArrayList<String> window = TickTimerSpout.slidingWindow.remove(0);
			// String window = TickTimerSpout.slidingWindow.remove(0);

			_collector.emit(new Values(window.get(0), window.get(1)), window);
		}
	}

	@Override
	public void ack(Object msgId) {
		LOG.debug("Acked message with msgId: {}", new Object[] { msgId });
	}

	@Override
	public void fail(Object msgId) {
		LOG.debug("Failed message with msgId: {}", new Object[] { msgId });
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("dateDebut", "dateFin"));
	}

	/*
	 * @Override public Map<String, Object> getComponentConfiguration() { Config
	 * conf = new Config(); if (!_isDistributed) {
	 * conf.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1); } else {
	 * 
	 * int tickFrequencyInSeconds = 60;
	 * conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, tickFrequencyInSeconds); }
	 * return conf; }
	 */

	static class RemindTask extends TimerTask {

		@Override
		public void run() {

			Date date = new Date();

			cal.setTime(date);
			cal.add(Calendar.MINUTE, -6);
			Date start = cal.getTime();

			cal.setTime(date);
			cal.add(Calendar.MINUTE, -5);
			Date end = cal.getTime();
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(TickTimerSpout.formater.format(start));
			temp.add(TickTimerSpout.formater.format(end));
			TickTimerSpout.slidingWindow.add(temp);

		}
	}

}
