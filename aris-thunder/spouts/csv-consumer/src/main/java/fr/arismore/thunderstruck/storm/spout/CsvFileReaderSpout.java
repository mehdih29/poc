package fr.arismore.thunderstruck.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.util.Map;

public class CsvFileReaderSpout extends BaseRichSpout {

    private static final Logger logger = LoggerFactory.getLogger(CsvFileReaderSpout.class);

    private static final String CELLSPLITTER = ";";
    private static final String FILE_LOCATION = "/tmp/myfile";

    private static BufferedReader buffer;

    SpoutOutputCollector _collector;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector collector) {
        try {
            _collector = collector;
            InputStream in = new FileInputStream(FILE_LOCATION);
            InputStreamReader inreader = new InputStreamReader(in);
            buffer = new BufferedReader(inreader);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void nextTuple() {
        try {
            long startTime = System.currentTimeMillis();

            String line;
            if ((line = buffer.readLine()) != null) {
                logger.debug(line);
                String[] data = line.split(CELLSPLITTER);

                // Emitting tuple. Don't forget to remove double quotes
                // The example given uses a two column CSV file
                _collector.emit(new Values(data[0].substring(1, data[0].length()-1)
                        ,data[1].substring(1, data[1].length()-1)
                        ,startTime
                ),
                        data[0].substring(1, data[0].length()-1)
                );
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // Startime is only used for timing statistics
        declarer.declare(new Fields("column1", "column2", "starttime"));
    }

    @Override
    public void ack(Object msgId) {
        logger.debug("Acked message with msgId: {}", new Object[] {msgId});
    }

    @Override
    public void fail(Object msgId) {
        logger.debug("Failed message with msgId: {}", new Object[] {msgId});
    }
}
