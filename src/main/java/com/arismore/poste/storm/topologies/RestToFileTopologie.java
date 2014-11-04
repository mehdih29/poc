package com.arismore.poste.storm.topologies;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.arismore.poste.storm.bolts.JobStarterBolt;
import com.arismore.poste.storm.bolts.WriteToFileBolt;
import com.arismore.poste.storm.spouts.TickTimerSpout;

/**
 * This is a basic example of a Storm topology.
 */
public class RestToFileTopologie {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("word", new TickTimerSpout(), 1);

		builder.setBolt("jobStarter", new JobStarterBolt(), 2).shuffleGrouping(
				"word");
		builder.setBolt("tofile", new WriteToFileBolt(), 2).shuffleGrouping(
				"jobStarter");

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(6);
			conf.setNumAckers(2);

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
