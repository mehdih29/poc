package org.lsp.thunderstruck.redislogvault4apache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: lukeskypator
 * Date: 11/25/13
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogListener extends Thread {

    private static Logger logger = LoggerFactory.getLogger(LogListener.class);

    private static final int WAITTIME = 1000;
    StreamBuffer reader = StreamBuffer.getInstance();

    @Override
    public void run() {
        while (!reader.hasContent()) {
            logger.debug("No content found. Waiting a bit");
            try {
                this.sleep(WAITTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
            }
        }
        logger.debug("Content found! Exiting!");
        Thread.currentThread().interrupt();
        return;
    }
}
