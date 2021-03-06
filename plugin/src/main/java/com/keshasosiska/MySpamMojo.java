package com.keshasosiska;

import lombok.Getter;
import lombok.Setter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Mojo(name = "spam")
@Setter
@Getter
public class MySpamMojo extends AbstractMojo {
    @Parameter(property = "durationInSeconds", defaultValue = "10")
    private Integer durationInSeconds;

    @Parameter(property = "pathToFile", defaultValue = "textToSpam.txt")
    private String pathToFile;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public void execute() {
        long endTimeInMs = System.currentTimeMillis() + durationInSeconds * 1000;
        Future future = executor.submit(new LogSpammer(pathToFile, endTimeInMs, getLog()));

        try {
            future.get(durationInSeconds, TimeUnit.SECONDS);
        } catch (Exception ignored) {
        } finally {
            future.cancel(true);
        }
    }
}
