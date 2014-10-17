package org.lsp.thunderstruck.redislogvault4apache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created with IntelliJ IDEA.
 * User: lukeskypator
 * Date: 11/20/13
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamBuffer {

    private static Logger logger = LoggerFactory.getLogger(StreamBuffer.class);

    private static StreamBuffer _instance = null;

    public static InputStreamReader isReader;
    public static BufferedReader bufReader;

    private StreamBuffer() {
        isReader = new InputStreamReader(System.in);
        bufReader = new BufferedReader(isReader);
    }

    public synchronized static StreamBuffer getInstance() {
        if (!(_instance != null)) {
            _instance = new StreamBuffer();
        }
        return _instance;
    }

    public BufferedReader getBufReader() {
        return bufReader;
    }

    public void setBufReader(BufferedReader bufReader) {
        this.bufReader = bufReader;
    }

    public boolean hasContent() {
        try {
            return bufReader.ready();
        } catch (IOException e) {
            return false;
        }
    }

}
