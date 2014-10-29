package com.arismore.poste.storm.topologies;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.Utils;

import com.arismore.poste.storm.spouts.TickTimerSpout;

/**
 * This is a basic example of a Storm topology.
 */
public class PrintTopologie {

	public static class PrinterBolt extends BaseBasicBolt {

		public void execute(Tuple tuple, BasicOutputCollector collector) {
			System.out.println(tuple);
		}

		public void declareOutputFields(OutputFieldsDeclarer ofd) {
		}

	}

  public static void main(String[] args) throws Exception {
    TopologyBuilder builder = new TopologyBuilder();

    builder.setSpout("word", new TickTimerSpout(), 10);
    builder.setBolt("print", new PrinterBolt(), 3).shuffleGrouping("word");
   

    Config conf = new Config();
    conf.setDebug(true);

    if (args != null && args.length > 0) {
      conf.setNumWorkers(3);

      StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
    }
    else {

      LocalCluster cluster = new LocalCluster();
      cluster.submitTopology("test", conf, builder.createTopology());
      Utils.sleep(10000);
      cluster.killTopology("test");
      cluster.shutdown();
    }
  }
}