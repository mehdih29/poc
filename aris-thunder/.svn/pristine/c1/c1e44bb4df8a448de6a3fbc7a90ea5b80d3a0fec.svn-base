package org.lsp.thunderstruck.redislogvault4apache;

import org.lsp.thunderstruck.redislogvault4apache.LogListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: lukeskypator
 * Date: 11/25/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Thunderstruck {

    private static Logger logger = LoggerFactory.getLogger(Thunderstruck.class);
    private static Thread logListener = null;
    private static Thread logInjector = null;
    private static StreamBuffer streamBuffer;

    public static void main(String [] args) throws InterruptedException {

        streamBuffer = StreamBuffer.getInstance();

        logListener = new LogListener();
        logger.debug("Starting Listener");
        logListener.start();
        logListener.join();
        logger.debug("Listener died");
        logInjector = new LogInjector();
        logger.debug("Starting Injector");
        logInjector.start();
        logInjector.join();
        logger.debug("Injector died");

        System.exit(0);
    }
}
