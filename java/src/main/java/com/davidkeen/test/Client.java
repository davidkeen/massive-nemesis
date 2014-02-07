package com.davidkeen.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Client {

    private HttpClient httpClient = new HttpClient();

    public long getRequest(int id) throws IOException {
        NameValuePair[] params = {new NameValuePair("id", String.valueOf(id))};
        GetMethod get = new GetMethod("http://example.com");
        get.setPath("/test");
        get.setQueryString(params);

        try {
            int status = httpClient.executeMethod(get);
            if (status == HttpStatus.SC_OK) {
                InputStream responseBody = get.getResponseBodyAsStream();
                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(new JsonReader(new InputStreamReader(responseBody))).getAsJsonObject();
                return json.get("value").getAsLong();
            } else  {
                throw new RuntimeException("Something went wrong. Response code " + status);
            }
        } finally {
            get.releaseConnection();
        }
    }
}
