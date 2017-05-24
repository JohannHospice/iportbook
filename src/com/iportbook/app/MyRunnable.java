package com.iportbook.app;

import java.util.logging.Logger;

public abstract class MyRunnable implements Runnable {
    private boolean run = true;
    protected final Logger LOGGER = Logger.getLogger(String.valueOf(getClass()));

    @Override
    public void run() {
        onStart();
        while (run) {
            onLoop();
        }
        onEnd();
    }


    public void stop() {
        run = false;
    }

    protected abstract void onLoop();

    protected abstract void onEnd();

    protected abstract void onStart();
}
