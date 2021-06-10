package com.backblaze.b2.client.webApiUrlConnection.android.http_client;

import android.util.Log;

import androidx.annotation.Nullable;

import com.backblaze.b2.client.exceptions.B2Exception;

import org.junit.Test;

import java.io.IOException;
import java.net.URLConnection;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

/**
 *
 */
public class HttpClientFactoryImplTest {

    @Test
    public void HttpClientFactoryImpl_build() {
        final HttpClientFactoryImpl factory = HttpClientFactoryImpl.build();
        assertNotNull(factory);
    }

    @Test
    public void HttpClientFactoryImpl_builder() {
        final HttpClientFactoryImpl.Builder builder = HttpClientFactoryImpl.builder();
        assertNotNull(builder);
    }

    @Test
    public void HttpClientFactoryImpl_create() throws B2Exception, IOException {
        final HttpClientFactoryImpl factory = HttpClientFactoryImpl.build();
        final URLConnection cxn = factory.create();
        assertNotNull(cxn);
    }

    @Test
    public void HttpClientFactoryImpl_close() {
        final HttpClientFactoryImpl factory = HttpClientFactoryImpl.build();
        final URLConnection cxn = factory.create();
        cxn.close();
    }
}
