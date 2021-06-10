/*
 * Copyright 2020, Backblaze Inc. All Rights Reserved.
 * License https://www.backblaze.com/using_b2_code.html
 */
package com.backblaze.b2.client.webApiUrlConnection.android.UrlConnection;

import com.backblaze.b2.client.B2AccountAuthorizer;
import com.backblaze.b2.client.B2AccountAuthorizerSimpleImpl;
import com.backblaze.b2.client.B2ClientConfig;
import com.backblaze.b2.client.B2DefaultRetryPolicy;
import com.backblaze.b2.client.B2RetryPolicy;
import com.backblaze.b2.client.B2Sdk;
import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientImpl;
import com.backblaze.b2.client.B2StorageClientWebifier;
import com.backblaze.b2.client.B2StorageClientWebifierImpl;
import com.backblaze.b2.client.credentialsSources.B2Credentials;
import com.backblaze.b2.client.credentialsSources.B2CredentialsFromEnvironmentSource;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.webApiClients.B2WebApiClient;
import com.backblaze.b2.util.B2Preconditions;

import java.util.function.Supplier;

public class B2StorageHttpClientBuilder {

    private static final String DEFAULT_MASTER_URL = "https://api.backblazeb2.com/";
    private final B2ClientConfig config;
    private B2WebApiClient webApiClient;
    private HttpClientFactory httpClientFactory;
    private Supplier<B2RetryPolicy> retryPolicySupplier;

    @SuppressWarnings("WeakerAccess")
    public static B2StorageHttpClientBuilder builder(B2ClientConfig config) {
        return new B2StorageHttpClientBuilder(config);
    }

    // We don't usually have several builder() methods, but this builder
    // is used by *everyone*, so i want to make it so that most users don't
    // need to worry about making an authorizer.
    @SuppressWarnings("WeakerAccess")
    public static B2StorageHttpClientBuilder builder(String applicationKeyId, String applicationKey, String userAgent) {
        final B2AccountAuthorizer accountAuthorizer = B2AccountAuthorizerSimpleImpl
                .builder(applicationKeyId, applicationKey)
                .build();
        final B2ClientConfig config = B2ClientConfig
                .builder(accountAuthorizer, userAgent)
                .build();
        return builder(config);
    }

    /**
     * @param userAgent the user agent to use when performing http requests.
     * @return a storage builder.
     */
    public static B2StorageHttpClientBuilder builder(String userAgent) {
        final B2Credentials credentials = B2CredentialsFromEnvironmentSource.build().getCredentials();
        return builder(credentials.getApplicationKeyId(), credentials.getApplicationKey(), userAgent);
    }

    private B2StorageHttpClientBuilder(B2ClientConfig config) {
        this.config = config;
    }

    public B2StorageClient build() {
        final B2WebApiClient webApiClient = (this.webApiClient != null) ?
                this.webApiClient :
                B2WebApiHttpClientImpl.builder().setHttpClientFactory(httpClientFactory).build();
        final B2StorageClientWebifier webifier = new B2StorageClientWebifierImpl(
                webApiClient,
                config.getUserAgent() + " " + B2Sdk.getName() + "/" + B2Sdk.getVersion(),
                (config.getMasterUrl() == null) ? DEFAULT_MASTER_URL : config.getMasterUrl(),
                config.getTestModeOrNull());
        final Supplier<B2RetryPolicy> retryPolicySupplier = (this.retryPolicySupplier != null) ?
                this.retryPolicySupplier :
                B2DefaultRetryPolicy.supplier();
        return new B2StorageClientImpl(
                webifier,
                config,
                retryPolicySupplier);
    }

    public B2StorageHttpClientBuilder setHttpClientFactory(HttpClientFactory httpClientFactory) {
        B2Preconditions.checkState(webApiClient == null, "httpClientFactory is only used if webApiClient isn't specified, so at most one of them can be non-null!");
        this.httpClientFactory = httpClientFactory;
        return this;
    }

    @SuppressWarnings("unused")
    public B2StorageHttpClientBuilder setWebApiClient(B2WebApiClient webApiClient) {
        B2Preconditions.checkState(httpClientFactory == null, "httpClientFactory is only used if webApiClient isn't specified, so at most one of them can be non-null!");
        this.webApiClient = webApiClient;
        return this;
    }

    @SuppressWarnings("unused")
    public B2StorageHttpClientBuilder setRetryPolicySupplier(Supplier<B2RetryPolicy> retryPolicySupplier) {
        this.retryPolicySupplier = retryPolicySupplier;
        return this;
    }
}
