package com.davidkeen.test;

import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

/**
 * The application/x-www-form-urlencoded media type uses an encoding based on a very early version of the
 * general URI percent-encoding rules, with a number of modifications such as newline normalization and
 * replacing spaces with "+" instead of "%20".
 */
public class UrlEncodingTest {

    public static final String RAW_STRING = "string with spaces";
    public static final String PERCENT_ENCODED_STRING = "string%20with%20spaces";
    public static final String FORM_ENCODED_STRING = "string+with+spaces";

    @Test
    public void jsEncodeUriComponent() throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        String encoded = String.valueOf(engine.eval("encodeURIComponent('" + RAW_STRING + "')"));
        assertEquals(PERCENT_ENCODED_STRING, encoded);
    }

    @Test
    public void UrlEncoder() throws Exception {
        String encoded = URLEncoder.encode(RAW_STRING, "UTF-8");
        assertEquals(FORM_ENCODED_STRING, encoded);
    }

    @Test
    public void percentEncode() throws Exception {
        String encoded = URLEncoder.encode(RAW_STRING, "UTF-8").replaceAll("\\+", "%20");
        assertEquals(PERCENT_ENCODED_STRING, encoded);
    }
}
