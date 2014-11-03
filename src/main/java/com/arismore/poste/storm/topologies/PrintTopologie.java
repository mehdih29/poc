package com.arismore.poste.storm.topologies;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;

import com.arismore.poste.storm.bolts.JobStarterBolt;
import com.arismore.poste.storm.bolts.WriteToFileBolt;
import com.arismore.poste.storm.spouts.TickTimerSpout;

/**
 * This is a basic example of a Storm topology.
 */
public class PrintTopologie {

	public static class PrinterBolt extends BaseBasicBolt {

		public void execute(Tuple tuple, BasicOutputCollector collector) {
			Date date = (Date) tuple.getValue(0);
			SimpleDateFormat formater = null;
			formater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			System.out.println("here is the output");
			System.out.println(formater.format(date));
			try {
				PrintWriter writer = new PrintWriter("_file"
						+ formater.format(date), "UTF-8");
				writer.write(tuple.getSourceComponent());
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void declareOutputFields(OutputFieldsDeclarer ofd) {
		}

	}

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("word", new TickTimerSpout(), 1);

		builder.setBolt("jobStarter", new JobStarterBolt(), 3).shuffleGrouping(
				"word");
		builder.setBolt("tofile", new WriteToFileBolt(), 8).shuffleGrouping(
				"jobStarter");

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(4);

			StormSubmitter.submitTopologyWithProgressBar(args[0], conf,
					builder.createTopology());
		} else {

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());
			Utils.sleep(10000);
			cluster.killTopology("test");
			cluster.shutdown();
		}
	}
}