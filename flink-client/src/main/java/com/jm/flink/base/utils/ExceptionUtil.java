package com.jm.flink.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author tasher @Description TODO
 * @ClassName ExceptionUtil.java
 * @createTime 2022/04/08
 */
public class ExceptionUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    public static String getErrorMessage(Throwable e) {
        if (null == e) {
            return null;
        }

        try (StringWriter stringWriter = new StringWriter();
             PrintWriter writer = new PrintWriter(stringWriter)) {
            e.printStackTrace(writer);
            writer.flush();
            stringWriter.flush();
            StringBuffer buffer = stringWriter.getBuffer();
            return buffer.toString();
        } catch (Throwable ee) {
            logger.error("", ee);
        }
        return null;
    }
}
