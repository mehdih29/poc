package com.arismore.poste.storm.bolts;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class WriteToFileBolt extends BaseRichBolt {

	private static final long serialVersionUID = 111112L;
	private OutputCollector collector;
	static Logger LOG = Logger.getLogger(WriteToFileBolt.class);

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	public void execute(Tuple tuple) {

		String url = (String) tuple.getValue(0);
		String content = (String) tuple.getValue(1);
		// InputStream inputStream = (InputStream) tuple.getValue(1);
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(inputStream));
		Long currentTimestamp = (new Date()).getTime();

		try {
			PrintWriter writer = new PrintWriter("/dev/shm/_file_" + url,
					"UTF-8");

			/*
			 * while ((in = reader.readLine()) != null) { writer.println(in); }
			 */
			writer.println(content);

			writer.close();
			this.collector.ack(tuple);
			LOG.error("Write to file took: "
					+ ((new Date()).getTime() - currentTimestamp));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}
}