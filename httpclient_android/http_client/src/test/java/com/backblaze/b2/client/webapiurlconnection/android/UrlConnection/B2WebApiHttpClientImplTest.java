package com.backblaze.b2.client.webApiUrlConnection.android.UrlConnection;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class B2WebApiHttpClientImplTest {

    @Test
    public void B2WebApiHttpClientImpl_build() {
        final B2StorageHttpClientBuilder factory = B2WebApiHttpClientImpl.build();
        assertNotNull(factory);
    }

    @Test
    public void B2WebApiHttpClientImpl_builder() {
        final B2WebApiHttpClientImpl.Builder builder = B2WebApiHttpClientImpl.builder();
        assertNotNull(builder);
    }

}
