package com.jm.flink.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class ProcessUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessUtils.class);

    public static long getProcessId(Process process) {
        long processId = 0;
        try {
            String fieldName = OsUtils.isWindows() ? "handle" : "pid";
            Field f = process.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);

            processId = f.getLong(process);
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
        }

        return processId;
    }

    public static void printStdLog(String taskId, Process process) {
        printStdinLog(taskId, process.getInputStream());
        printStderrLog(taskId, process.getErrorStream());
    }

    public static void printStdinLog(String taskId, InputStream stdin) {
        try (BufferedReader inReader = new BufferedReader(new InputStreamReader(stdin))) {
            String line;
            while ((line = inReader.readLine()) != null) {
                LOG.info(line);
            }
        } catch (Exception e) {
            LOG.error("error:{}", e.getMessage());
        }
    }

    public static void printStderrLog(String taskId, InputStream stderr) {
        try (BufferedReader inReader = new BufferedReader(new InputStreamReader(stderr))) {
            String line;
            while ((line = inReader.readLine()) != null) {
                LOG.info(line);
            }
        } catch (Exception e) {
            LOG.error("error:{}", e.getMessage());
        }
    }
}
