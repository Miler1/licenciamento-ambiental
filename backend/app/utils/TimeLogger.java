package utils;

import play.Logger;

public class TimeLogger {

    private Long start;

    public TimeLogger() {
        this.start = System.nanoTime();
    }

    public void print(String msg) {
        Logger.info("\n\n\n\n");
        Logger.info(msg  + ":  " + (System.nanoTime() - this.start) / 1000000 + "ms\n");
        Logger.info("\n\n\n\n");
    }
}
