/*
 * Copyright 2020, Backblaze Inc. All Rights Reserved.
 * License https://www.backblaze.com/using_b2_code.html
 */
package com.backblaze.b2.client.webApiUrlConnection.android.UrlConnection;

import android.util.Log;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.util.B2Preconditions;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

/**
 * This is the default HttpClientFactory implementation.
 *
 * Created HttpClients always support 'https' because that's what the
 * production B2 servers require.  By default, the created HttpClients
 * do not support 'http' to ensure we don't send data over http by accident.
 *
 * If you have a non-https implementation of B2 that you test against,
 * you *may* choose to enable 'http' support when creating the factory.
 * We really do *not* recommend that in production.
 */
public class HttpClientFactoryImpl implements HttpClientFactory {
    private URLConnection connection;
    private boolean supportInsecureHttp;
    private int connectTimeoutSeconds;
    private String url;
    private final String TAG = "HTTP_CLIENT";

    private HttpClientFactoryImpl(boolean supportInsecureHttp, int connectTimeoutSeconds) {
        this.supportInsecureHttp = supportInsecureHttp;
        this.connectTimeoutSeconds = connectTimeoutSeconds;
    }

    @SuppressWarnings("WeakerAccess")

    public static HttpClientFactoryImpl build() {
        return builder().build();
    }

    public static Builder builder() {
        return new Builder();
    }

    /*
    * Returns new HTTPClient instance
    * */
    @Override
    public URLConnection create() throws B2Exception {
        try {
            final URL url = new URL(this.url);
            if (this.supportInsecureHttp) {
                connection = (HttpsURLConnection) url.openConnection();
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setInstanceFollowRedirects(false);
            connection.setConnectTimeout(this.connectTimeoutSeconds);
            return connection;
        } catch (Error e) {
            // Log
        }
    }

    @Override
    public void close() {
        try {
            connection.disconnect();
        } catch (Error e) {
            // restore the interrupt because we're not acting on it here.
            if (e.getMessage() != null) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * The factory we're building will have close() called on it and when it
     * does, it will close its connection manager.  Since we don't want to
     * close a connection manager out from under another factory, each Builder
     * is only allowed to execute build() once.
     */
    public static class Builder {
        // Defaults
        private static final int DEFAULT_CONNECT_TIMEOUT_SECONDS = 5;
        private static final boolean DEFAULT_SUPPORT_INSECURE_HTTP = false;


        // Builder values
        private boolean builtOneAlready;

        // should the clients support 'http'?  (they always support 'https'.)
        // this is off by default, and that's a good way to leave it.
        // http is only supported for use with some test environments.
        private boolean supportInsecureHttp = DEFAULT_SUPPORT_INSECURE_HTTP;
        private int connectTimeoutSeconds = DEFAULT_CONNECT_TIMEOUT_SECONDS;

        public Builder setSupportInsecureHttp(boolean supportInsecureHttp) {
            this.supportInsecureHttp = supportInsecureHttp;
            return this;
        }

        public Builder setConnectionTimeout(int connectTimeoutSeconds) {
            this.connectTimeoutSeconds = connectTimeoutSeconds;
            return this;
        }

        public HttpClientFactoryImpl build() {
            B2Preconditions.checkState(!builtOneAlready, "called build() more than once?!");
            builtOneAlready = true;

            return new HttpClientFactoryImpl(
                    this.supportInsecureHttp,
                    this.connectTimeoutSeconds
            );
        }
    }
}