package com.arismore.poste.storm;

import backtype.storm.Config;
import backtype.storm.topology.OutputFieldsDeclarer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SpoutREST extends BaseRichSpout {
   public static Logger LOG = LoggerFactory.getLogger(XMLSoput.class);
   boolean _isDistributed;
   SpoutOutputCollector _collector;
   private static final String FILE_LOCATION = "/tmp/myfile";
   private static BufferedReader buffer;
   
   public SpoutREST() {
       this(true);
   }

   public SpoutREST(boolean isDistributed) {
       _isDistributed = isDistributed;
   }
       
   public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
       _collector = collector;
       
       try {
    	   _collector = collector;
    	   InputStream in = new FileInputStream(FILE_LOCATION);
    	   InputStreamReader inreader = new InputStreamReader(in);
    	   buffer = new BufferedReader(inreader);
    	   } catch (FileNotFoundException e) {
    	      LOG.error(e.getMessage(), e);
    	   }

   }
   
   public void close() {
       
   }
       
   public void nextTuple() {
       Utils.sleep(100);
       final String[] words = new String[] {"nathan", "mike", "jackson", "golda", "bertels"};
       final Random rand = new Random();
       final String word = words[rand.nextInt(words.length)];
       _collector.emit(new Values(word));
   }
   
   public void ack(Object msgId) {

   }

   public void fail(Object msgId) {
       
   }
   
   public void declareOutputFields(OutputFieldsDeclarer declarer) {
       declarer.declare(new Fields("word"));
   }

   @Override
   public Map<String, Object> getComponentConfiguration() {
       if(!_isDistributed) {
           Map<String, Object> ret = new HashMap<String, Object>();
           ret.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
           return ret;
       } else {
           return null;
       }
   }    
}
