package com.arismore.poste.storm.bolts;

import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonToESBolt extends BaseRichBolt {

	private static final long serialVersionUID = 222111115L;
	static Logger LOG = Logger.getLogger(JsonToESBolt.class);
	private OutputCollector collector;
	// Singleton instance = null;
	private Client client = null;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	public void execute(Tuple tuple) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		String id = (String) tuple.getValue(0);
		String json = (String) tuple.getValue(1);
		bulkRequest.add(this.client
				.prepareIndex("parceldata", "traitement", id).setSource(json));
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();

		if (bulkResponse.hasFailures()) {
			LOG.error("bulk failures");
			LOG.error(bulkResponse.getItems().toString());
		}
		this.collector.ack(tuple);

	}

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(
						"127.0.0.1", 9300));
	}
}