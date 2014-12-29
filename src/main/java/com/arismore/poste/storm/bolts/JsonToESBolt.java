package com.arismore.poste.storm.bolts;

import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.arismore.poste.data.QueryEntry;
import com.arismore.poste.data.Singleton;

public class JsonToESBolt extends BaseRichBolt {

	private static final long serialVersionUID = 222111115L;
	static Logger LOG = Logger.getLogger(JsonToESBolt.class);
	Singleton instance = null;
	BulkRequestBuilder bulkRequest = null;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	public void execute(Tuple tuple) {
		QueryEntry entry = (QueryEntry) tuple.getValue(0);
		for (int j = 0; j < entry.getParcels().size(); j++) {

			bulkRequest.add(instance
					.getClient()
					.prepareIndex(
							"parceldata",
							"traitement",
							entry.getParcels().get(j).getIsie()
									+ "|"
									+ entry.getParcels().get(j).getTraitement()
											.getId())
					.setSource(
							instance.getGson()
									.toJson(entry.getParcels().get(j))));
		}
		
	}

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.instance = Singleton.getInstance();
		this.bulkRequest = instance.getClient().prepareBulk();
	}
}