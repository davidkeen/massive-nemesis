package com.davidkeen.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {

    @Mock
    private HttpClient mockHttpClient;

    @InjectMocks
    private Client client;

    @Test
    public void testGetRequest() throws Exception {

        // Using reflection to set the response on the passed object. See: http://stackoverflow.com/q/3620796/1923852
        when(mockHttpClient.executeMethod(any(GetMethod.class))).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws NoSuchFieldException, UnsupportedEncodingException, IllegalAccessException {
                GetMethod getMethod = (GetMethod) invocation.getArguments()[0];

                Field respStream = HttpMethodBase.class.getDeclaredField("responseStream");
                respStream.setAccessible(true);
                respStream.set(getMethod, new ByteArrayInputStream("{\"id\":1,\"value\":100}".getBytes("UTF-8")));

                return HttpStatus.SC_OK;
            }
        });

        assertEquals(100, client.getRequest(1));

        verify(mockHttpClient).executeMethod(any(GetMethod.class));
    }

    @Test(expected = RuntimeException.class)
    public void testBadRequest() throws Exception {
        when(mockHttpClient.executeMethod(any(GetMethod.class))).thenReturn(HttpStatus.SC_BAD_REQUEST);
        client.getRequest(1);
    }
}
