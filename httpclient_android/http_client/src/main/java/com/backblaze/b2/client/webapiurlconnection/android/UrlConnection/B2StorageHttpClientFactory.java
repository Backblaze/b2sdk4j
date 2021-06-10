/*
 * Copyright 2020, Backblaze Inc. All Rights Reserved.
 * License https://www.backblaze.com/using_b2_code.html
 */
package com.backblaze.b2.client.webApiUrlConnection.android.UrlConnection;

import com.backblaze.b2.client.B2ClientConfig;
import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientFactory;

/**
 * Simple factory for the HttpClient-based B2StorageClient.
 *
 * THREAD-SAFE.
 */

public class B2StorageHttpClientFactory implements B2StorageClientFactory {

    @Override
    public B2StorageClient create(B2ClientConfig config) {
        return B2StorageHttpClientBuilder.builder(config).build();
    }
}
