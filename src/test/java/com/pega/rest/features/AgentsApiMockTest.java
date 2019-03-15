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
import com.pega.rest.domain.agents.AgentInfo;
import com.pega.rest.domain.agents.Agents;
import com.squareup.okhttp.mockwebserver.MockResponse;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "unit", testName = "AgentsApiMockTest")
public class AgentsApiMockTest extends BasePegaMockTest {

    private static final String nodesPath = "/nodes";
    private static final String agentsPath = "/agents";
    private static final String agentId = "SystemEventEvaluation|Pega-EventProcessing";
    private static final String agentIdEncoded = "SystemEventEvaluation%7CPega-EventProcessing";

    public void testListAgents() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource("/agents-list.json")).setResponseCode(200));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final AgentsApi api = baseApi.agentsApi();

                final Agents reference = api.list(nodeId);
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isEmpty();
                assertThat(reference.items()).isNotEmpty();
                final AgentInfo agent = reference.items().get(0).agentInfo();
                assertThat(agent.agentId()).isEqualTo(agentId);
                assertThat(agent.instances()).isNotEmpty();
                assertThat(agent.instances().get(0).isEnabled()).isTrue();

                assertSent(server.getMockWebServer(), getMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + agentsPath);
            }
        }
    }

    public void testListAgentsOnError() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource(errorsNodesFile)).setResponseCode(404));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final AgentsApi api = baseApi.agentsApi();

                final Agents reference = api.list(nodeId);
                assertThat(reference).isNotNull();
                assertThat(reference.items()).isEmpty();
                assertThat(reference.errors()).isNotEmpty();
                assertThat(reference.errors().get(0).message()).contains(errorMessage);
                assertThat(reference.errors().get(0).context()).isNull();

                assertSent(server.getMockWebServer(), getMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + agentsPath);
            }
        }
    }

    public void testGetAgent() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource("/agents-get.json")).setResponseCode(200));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final AgentsApi api = baseApi.agentsApi();

                final Agents reference = api.get(nodeId, agentId);
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isEmpty();
                assertThat(reference.items().size()).isEqualTo(1);
                final AgentInfo agent = reference.items().get(0).agentInfo();
                assertThat(agent.agentId()).isEqualTo(agentId);
                assertThat(agent.instances()).isNotEmpty();
                assertThat(agent.instances().get(0).isEnabled()).isTrue();

                assertSent(server.getMockWebServer(), getMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + agentsPath
                        + forwardSlash + agentIdEncoded);
            }
        }
    }

    public void testGetAgentOnError() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource(errorsNodesFile)).setResponseCode(404));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final AgentsApi api = baseApi.agentsApi();

                final Agents reference = api.get(nodeId, agentId);
                assertThat(reference).isNotNull();
                assertThat(reference.items()).isEmpty();
                assertThat(reference.errors()).isNotEmpty();
                assertThat(reference.errors().get(0).message()).contains(errorMessage);
                assertThat(reference.errors().get(0).context()).isNull();

                assertSent(server.getMockWebServer(), getMethod, restApiPath
                        + PegaApiMetadata.API_VERSION
                        + nodesPath
                        + forwardSlash + nodeId
                        + agentsPath
                        + forwardSlash + agentIdEncoded);
            }
        }
    }
}
