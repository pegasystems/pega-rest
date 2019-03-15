/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pega.rest;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

public class CloseableMockWebServer implements Closeable {

    final MockWebServer server;

    private CloseableMockWebServer() {
        server = new MockWebServer();
    }

    /**
     * @return nodes a started instance of CloseableMockWebServer.
     */
    public static CloseableMockWebServer start() {
        final CloseableMockWebServer server = new CloseableMockWebServer();
        try {
            server.getMockWebServer().start();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return server;
    }

    public URL getUrl(final String url) {
        return this.getMockWebServer().getUrl(url);
    }

    public MockWebServer getMockWebServer() {
        return server;
    }

    public void enqueue(final MockResponse response) {
        this.getMockWebServer().enqueue(response);
    }

    @Override
    public void close() throws IOException {
        server.shutdown();
    }
}
