package com.keshasosiska;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class LogJunker implements Runnable {
    private static final long DT = 500;
    private static final String DEBUG = "[DEBUG] ";
    private static final String INFO = "[INFO] ";
    private static final String WARN = "[WARN] ";
    private static final String ERROR = "[ERROR] ";

    private final String pathToFile;
    private final long endTimeInMs;
    private final Random r;
    private final Log log;

    LogJunker(String pathToFile, long endTimeInMs, Log log) {
        this.pathToFile = pathToFile;
        this.endTimeInMs = endTimeInMs;
        this.r = new Random();
        this.log = log;
    }

    public void run() {
        long curTime = System.currentTimeMillis();
        while (timeAllows(curTime)) {
            // Prepare reading
            Scanner scanner = getScanner();
            if (scanner == null) {
                return;
            }
            curTime = System.currentTimeMillis();

            // Process file
            while (timeAllows(curTime) && scanner.hasNextLine()) {
                printWithTimeout(scanner);
                curTime = System.currentTimeMillis();
            }

            scanner.close();
            curTime = System.currentTimeMillis();
        }
    }

    private void printWithTimeout(Scanner scanner) {
        String line = scanner.nextLine();
        long maxShowDuration = line.startsWith("[") ? 100 : 300;

        long opDur = Math.abs(r.nextLong() % maxShowDuration);
        long curTime = System.currentTimeMillis();
        long durationInMs = Math.min(curTime + opDur, endTimeInMs) - curTime;

        if (line.startsWith(DEBUG)) {
            log.debug(line.substring(DEBUG.length()));
        } else if (line.startsWith(INFO)) {
            log.info(line.substring(INFO.length()));
        } else if (line.startsWith(WARN)) {
            log.warn(line.substring(WARN.length()));
        } else if (line.startsWith(ERROR)) {
            log.error(line.substring(ERROR.length()));
        } else {
            System.out.println(line);
        }

        try {
            Thread.sleep(durationInMs);
        } catch (InterruptedException ignored) {
            scanner.close();
        }
    }

    private Scanner getScanner() {
        try {
            return new Scanner(new File(pathToFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean timeAllows(long curTimeMs) {
        return curTimeMs + DT < endTimeInMs;
    }
}
