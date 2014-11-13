package com.arismore.poste.storm.topologies;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.arismore.poste.storm.bolts.JobStarterBolt;
import com.arismore.poste.storm.bolts.UriGetBolt;
import com.arismore.poste.storm.bolts.WriteToFileBolt;
import com.arismore.poste.storm.spouts.TickTimerSpout;

/**
 * REST to file Storm topology.
 */
public class RestToFileTopologie {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("slidingWindow", new TickTimerSpout(), 1);
		// --> 2/1= 2 tasks per executors
		builder.setBolt("jobStarter", new JobStarterBolt(), 1)// .setNumTasks(2)
				.shuffleGrouping("slidingWindow");
		// --> 200/100= 2 tasks per executors
		builder.setBolt("uriGet", new UriGetBolt(), 6).setNumTasks(1)
				.shuffleGrouping("jobStarter");
		
		builder.setBolt("tofile", new WriteToFileBolt(), 4).setNumTasks(8)
		.shuffleGrouping("uriGet");

		Config conf = new Config();
		// conf.setMessageTimeoutSecs(120);
		// Config.STORM_ZOOKEEPER_SESSION_TIMEOUT =
		

		// The maximum amount of time given to the topology to fully process a
		// message emitted by a spout
		conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 600);

		conf.setDebug(false);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(12);
			conf.setNumAckers(2);

			StormSubmitter.submitTopologyWithProgressBar(args[0], conf,
					builder.createTopology());
		}
	}
}
