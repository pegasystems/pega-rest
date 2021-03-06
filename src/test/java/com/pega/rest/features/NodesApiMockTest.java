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

package com.pega.rest.features;

import com.pega.rest.BasePegaMockTest;
import com.pega.rest.CloseableMockWebServer;
import com.pega.rest.PegaApi;
import com.pega.rest.PegaApiMetadata;
import com.pega.rest.domain.nodes.Nodes;
import com.pega.rest.domain.nodes.QuiesceStatus;
import com.pega.rest.domain.nodes.SystemSettings;
import com.squareup.okhttp.mockwebserver.MockResponse;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "unit", testName = "NodesApiMockTest")
public class NodesApiMockTest extends BasePegaMockTest {

    private static final String nodesPath = "/nodes";

    public void testGetNodes() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource("/nodes-get.json")).setResponseCode(200));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final NodesApi api = baseApi.nodesApi();

                final Nodes reference = api.list();
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isEmpty();
                assertThat(reference.items()).isNotEmpty();
                assertThat(reference.items().get(0).runningState()).isEqualTo("Running");

                assertSent(server.getMockWebServer(), getMethod, restApiPath + PegaApiMetadata.API_VERSION + nodesPath);
            }
        }
    }

    public void testGetNodesOnError() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource(errorsNodesFile)).setResponseCode(403));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final NodesApi api = baseApi.nodesApi();

                final Nodes reference = api.list();
                assertThat(reference).isNotNull();
                assertThat(reference.items()).isEmpty();
                assertThat(reference.errors()).isNotEmpty();
                assertThat(reference.errors().get(0).message()).contains(errorMessage);
                assertThat(reference.errors().get(0).context()).isNull();

                assertSent(server.getMockWebServer(), getMethod, restApiPath + PegaApiMetadata.API_VERSION + nodesPath);
            }
        }
    }

    public void testGetSettings() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource("/nodes-settings.json")).setResponseCode(200));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final NodesApi api = baseApi.nodesApi();

                final SystemSettings reference = api.systemSettings(nodeId);
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isEmpty();
                assertThat(reference.items()).isNotEmpty();
                assertThat(reference.items().get(0).name()).isEqualTo("agent/enable");

                assertSent(server.getMockWebServer(), getMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + "/settings/system");
            }
        }
    }

    public void testGetSettingsOnError() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource(errorsNodesFile)).setResponseCode(404));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final NodesApi api = baseApi.nodesApi();

                final SystemSettings reference = api.systemSettings(nodeId);
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isNotEmpty();
                assertThat(reference.items()).isEmpty();
                assertThat(reference.errors().get(0).message()).contains(errorMessage);
                assertThat(reference.errors().get(0).context()).isNull();

                assertSent(server.getMockWebServer(), getMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + "/settings/system");
            }
        }
    }

    public void testQuiesce() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource("/nodes-quiesce.json")).setResponseCode(200));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final NodesApi api = baseApi.nodesApi();

                final QuiesceStatus reference = api.quiesce(nodeId);
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isEmpty();
                assertThat(reference.node()).isNotNull();
                assertThat(reference.node().systemState()).isEqualTo("Quiesce Start");

                assertSent(server.getMockWebServer(), postMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + "/quiesce");
            }
        }
    }

    public void testQuiesceOnError() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource(errorsNodesFile)).setResponseCode(404));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final NodesApi api = baseApi.nodesApi();

                final QuiesceStatus reference = api.quiesce(nodeId);
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isNotEmpty();
                assertThat(reference.node()).isNull();
                assertThat(reference.errors().get(0).message()).contains(errorMessage);
                assertThat(reference.errors().get(0).context()).isNull();

                assertSent(server.getMockWebServer(), postMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + "/quiesce");
            }
        }
    }

    public void testUnQuiesce() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource("/nodes-unquiesce.json")).setResponseCode(200));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final NodesApi api = baseApi.nodesApi();

                final QuiesceStatus reference = api.unquiesce(nodeId);
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isEmpty();
                assertThat(reference.node()).isNotNull();
                assertThat(reference.node().systemState()).isEqualTo("Running");

                assertSent(server.getMockWebServer(), postMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + "/unquiesce");
            }
        }
    }

    public void testUnQuiesceOnError() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource(errorsNodesFile)).setResponseCode(404));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final NodesApi api = baseApi.nodesApi();

                final QuiesceStatus reference = api.unquiesce(nodeId);
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isNotEmpty();
                assertThat(reference.node()).isNull();
                assertThat(reference.errors().get(0).message()).contains(errorMessage);
                assertThat(reference.errors().get(0).context()).isNull();

                assertSent(server.getMockWebServer(), postMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + "/unquiesce");
            }
        }
    }
}
