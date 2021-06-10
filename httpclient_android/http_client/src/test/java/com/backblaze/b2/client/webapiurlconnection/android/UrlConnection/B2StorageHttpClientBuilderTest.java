package com.backblaze.b2.client.webApiUrlConnection.android.UrlConnection;

import com.backblaze.b2.client.B2RetryPolicy;
import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientImpl;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class B2StorageHttpClientBuilderTest {

    @Test
    public void B2StorageHttpClientBuilder_builder() {
        final B2StorageHttpClientBuilder builder = new B2StorageHttpClientBuilder.builder();
        assertNotNull(builder);
    }

    @Test
    public void B2StorageHttpClientBuilder_build() {
        final B2StorageClient factory = new B2StorageHttpClientBuilder.build();
        assertNotNull(factory);
    }

    @Test
    public void B2StorageHttpClientBuilder_setHttpClientFactory() {
        final HttpClientFactory clientFactory = HttpClientFactoryImpl.build();
        final B2StorageClient factory = new B2StorageHttpClientBuilder.build();
        final B2StorageHttpClientBuilder factoryTwo = factory.setHttpClientFactory(clientFactory);
        assertNotNull(factoryTwo);
    }

    @Test
    public void B2StorageHttpClientBuilder_setWebApiClient() {
        final B2ClientConfig config = B2ClientConfig.builder("api-key-id", "api-key", "user-agent").build();
        final B2StorageClient client = B2StorageHttpClientBuilder.builder(config).build()
        final B2StorageClientImpl factory = B2StorageHttpClientBuilder.build();
        final B2StorageHttpClientBuilder factoryTwo = factory.setWebApiClient(client);
        assertNotNull(factory);
    }

    @Test
    public void B2StorageHttpClientBuilder_setRetryPolicySupplier() {
        final Supplier<B2RetryPolicy> supplier = B2DefaultRetryPolicy.supplier();
        final B2StorageClient factory = new B2StorageHttpClientBuilder.build();
        final B2StorageHttpClientBuilder factoryTwo = factory.setRetryPolicySupplier(supplier);
        assertNotNull(factoryTwo);
    }
}
