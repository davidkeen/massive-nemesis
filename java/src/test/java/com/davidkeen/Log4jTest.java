/*
 * From http://tanerdiler.blogspot.co.uk/2015/07/log4j2-appender-for-junit-testing.html
 */

package com.davidkeen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.junit.After;
import org.junit.Before;

public class Log4jTest {

    private TestLog4j2Appender appender;

    @Before
    public void tearUp()
    {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        appender = TestLog4j2Appender.createAppender("TestAppender", null, null, null);
        appender.start();
        config.addAppender(appender);

        // Use the specific logger you want to check
        Logger logger = LogManager.getLogger(Log4jTest.class);
        ((org.apache.logging.log4j.core.Logger) logger).addAppender(appender);

        ctx.updateLoggers();
    }

    @After
    public void tearDown()
    {
        appender.clearMessages();
    }
}
