package com.arismore.poste.storm.topologies;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

import com.arismore.poste.storm.bolts.JobStarterBolt;
import com.arismore.poste.storm.bolts.UriGetBolt;
import com.arismore.poste.storm.bolts.WriteToFileBolt;
import com.arismore.poste.storm.spouts.TickTimerSpout;

/**
 * REST to file Storm topology.
 */
public class RestToFile {

	public static void main(String[] args) throws Exception {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("slidingWindow", new TickTimerSpout(), 1);
		// --> 2/1= 2 tasks per executors
		builder.setBolt("jobStarter", new JobStarterBolt(), 1).shuffleGrouping(
				"slidingWindow");
		// --> 200/100= 2 tasks per executors
		builder.setBolt("uriGet", new UriGetBolt(), 5).setNumTasks(10)
				.shuffleGrouping("jobStarter");

		builder.setBolt("tofile", new WriteToFileBolt(), 2).setNumTasks(8)
				.shuffleGrouping("uriGet");

		Config conf = new Config();

		/*
		 * The maximum amount of time given* to the topology to fully process* a
		 * message emitted by a spout
		 */
		conf.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 60);

		conf.put(Config.TOPOLOGY_RECEIVER_BUFFER_SIZE, 8);
		conf.put(Config.TOPOLOGY_TRANSFER_BUFFER_SIZE, 32);
		conf.put(Config.TOPOLOGY_EXECUTOR_RECEIVE_BUFFER_SIZE, 16384);
		conf.put(Config.TOPOLOGY_EXECUTOR_SEND_BUFFER_SIZE, 16384);

		conf.setDebug(false);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(25);
			conf.setNumAckers(3);

			StormSubmitter.submitTopologyWithProgressBar(args[0], conf,
					builder.createTopology());
		}
	}
}
