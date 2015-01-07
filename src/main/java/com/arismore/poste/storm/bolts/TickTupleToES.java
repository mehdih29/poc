package com.arismore.poste.storm.bolts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.arismore.poste.util.TupleHelpers;

public class TickTupleToES extends BaseRichBolt {

	private static final long serialVersionUID = 22211111566L;
	static Logger LOG = Logger.getLogger(TickTupleToES.class);
	private OutputCollector collector;
	// private Gson gson = null;
	private Client client = null;

	/** The queue holding tuples in a batch. */
	protected LinkedBlockingQueue<Tuple> queue = new LinkedBlockingQueue<Tuple>();

	/** The threshold after which the batch should be flushed out. */
	int batchSize = 100;

	/**
	 * The batch interval in sec. Minimum time between flushes if the batch
	 * sizes are not met. This should typically be equal to
	 * topology.tick.tuple.freq.secs and half of topology.message.timeout.secs
	 */

	int batchIntervalInSec = 10;
	/** The last batch process time seconds. Used for tracking purpose */

	long lastBatchProcessTimeSeconds = 0;

	public void execute(Tuple tuple) {
		// Check if the tuple is of type Tick Tuple
		if (TupleHelpers.isTickTuple(tuple)) {

			// If so, it is indication for batch flush. But don't flush if
			// previous
			// flush was done very recently (either due to batch size threshold
			// was
			// crossed or because of another tick tuple
			//
			if ((System.currentTimeMillis() / 1000 - lastBatchProcessTimeSeconds) >= batchIntervalInSec) {
				LOG.debug("Current queue size is " + this.queue.size()
						+ ". But received tick tuple so executing the batch");
				finishBatch();
			} else {
				LOG.debug("Current queue size is "
						+ this.queue.size()
						+ ". Received tick tuple but last batch was executed "
						+ (System.currentTimeMillis() / 1000 - lastBatchProcessTimeSeconds)
						+ " seconds back that is less than "
						+ batchIntervalInSec + " so ignoring the tick tuple");
			}
		} else {

			// Add the tuple to queue. But don't ack it yet.
			this.queue.add(tuple);
			int queueSize = this.queue.size();

			LOG.debug("current queue size is " + queueSize);
			if (queueSize >= batchSize) {
				LOG.debug("Current queue size is >= " + batchSize
						+ " executing the batch");
				finishBatch();
			}
		}
	}

	/**
	 * Finish batch.
	 */
	public void finishBatch() {
		LOG.debug("Finishing batch of size " + queue.size());
		lastBatchProcessTimeSeconds = System.currentTimeMillis() / 1000;
		List<Tuple> tuples = new ArrayList<Tuple>();
		queue.drainTo(tuples);
		if (tuples.size() == 0){
			return;
		}
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		BulkResponse bulkResponse = null;
		for (Tuple tuple : tuples) {

			String id = (String) tuple.getValue(0);
			String json = (String) tuple.getValue(1);
			bulkRequest.add(this.client.prepareIndex("parceldata",
					"traitement", id).setSource(json));
			/*
			 * QueryEntry entry = (QueryEntry) tuple.getValue(0); for (int j =
			 * 0; j < entry.getParcels().size(); j++) {
			 * 
			 * bulkRequest.add(this.client.prepareIndex( "parceldata",
			 * "traitement", entry.getParcels().get(j).getIsie() + "|" +
			 * entry.getParcels().get(j).getTraitement() .getId()).setSource(
			 * this.gson.toJson(entry.getParcels().get(j)))); }
			 */
		}
		try {
			// Execute bulk request and get individual tuple responses back.
			bulkResponse = bulkRequest.execute().actionGet();
			BulkItemResponse[] responses = bulkResponse.getItems();
			BulkItemResponse response = null;
			LOG.debug("Executed the batch. Processing responses.");
			for (int counter = 0; counter < responses.length; counter++) {
				response = responses[counter];
				if (response.isFailed()) {
					// ElasticSearchDocument failedEsDocument =
					// this.tupleMapper.mapToDocument(tuples.get(counter));
					LOG.error("Failed to process tuple # " + counter);
					this.collector.fail(tuples.get(counter));
				} else {
					LOG.debug("Successfully processed tuple # " + counter);
					this.collector.ack(tuples.get(counter));
				}
			}
		} catch (Exception e) {
			LOG.error("Unable to process " + tuples.size() + " tuples", e);
			// Fail entire batch
			for (Tuple tuple : tuples) {
				this.collector.fail(tuple);
			}
		}
	}

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(
						"127.0.0.1", 9300));
		// this.gson = new GsonBuilder().create();

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// configure how often a tick tuple will be sent to our bolt
		Config conf = new Config();
		conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 5);
		return conf;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}
}
