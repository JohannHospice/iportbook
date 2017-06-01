package com.iportbook.core.tools;

import java.util.logging.Logger;

public abstract class ApplicationListener implements Runnable {
    private boolean run = true;
    protected final Logger LOGGER = Logger.getLogger(String.valueOf(getClass()));

    public void stop() {
        run = false;
    }

    @Override
    public void run() {
        try {
            onStart();
            while (run) {
                onLoop();
            }
            onEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("stop");
    }

    protected abstract void onStart() throws Exception;

    protected abstract void onLoop() throws Exception;

    protected abstract void onEnd() throws Exception;
}
