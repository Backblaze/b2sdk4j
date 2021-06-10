/*
 * Copyright 2020, Backblaze Inc. All Rights Reserved.
 * License https://www.backblaze.com/using_b2_code.html
 */
package com.backblaze.b2.client.webApiUrlConnection.android.UrlConnection;

import com.backblaze.b2.client.exceptions.B2Exception;
import java.net.URLConnection;

public interface HttpClientFactory extends AutoCloseable {

    /**
     * This returns a CloseableHttpClient (instead of an HttpClient) because
     * the SDK needs to be able to close the responses to allow connections
     * to be reused.
     *
     * Note that even though this returns a CloseableHttpClient,
     * the SDK will *not* call close() on it, because doing so
     * would close the client's HttpClientConnectionManager.
     *
     * @return a new httpClient for use by the SDK.
     *         this will be called often.
     * @throws B2Exception if there's any trouble creating the client.
     */
    URLConnection create() throws B2Exception;

    /**
     * Called to release resources, such as an HttpClientConnectionManager.
     */
    @Override
    void close();
}
