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

import com.pega.rest.BasePegaApiLiveTest;
import com.pega.rest.TestUtilities;
import com.pega.rest.domain.agents.Agent;
import com.pega.rest.domain.agents.Agents;
import com.pega.rest.domain.nodes.ClusterMember;
import com.pega.rest.domain.nodes.Nodes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "live", testName = "AgentsApiLiveTest", singleThreaded = true)
public class AgentsApiLiveTest extends BasePegaApiLiveTest {

    public String nodeId;
    public String agentId;

    @BeforeClass
    public void testListNodes() {
        final Nodes reference = api.nodesApi().list();
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isEmpty();
        assertThat(reference.items()).isNotEmpty();
        final ClusterMember member = reference.items().get(0);
        assertThat(member.nodeType().isEmpty()).isFalse();
        if (member.runningState().equals("Quiesce Complete")) {
            api.nodesApi().unquiesce(member.nodeId());
        }

        nodeId = member.nodeId();
    }

    @Test
    public void testListAgents() {
        final Agents reference = api().list(nodeId);
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isEmpty();
        assertThat(reference.items()).isNotEmpty();

        final Agent agent = reference.items().get(0);
        assertThat(agent.agentInfo().instances()).isNotEmpty();

        agentId = agent.agentInfo().agentId();
    }

    @Test (dependsOnMethods = "testListAgents")
    public void testGetAgent() {
        final Agents reference = api().get(nodeId, agentId);
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isEmpty();
        assertThat(reference.items()).isNotEmpty();

        final Agent agent = reference.items().get(0);
        assertThat(agent.agentInfo().instances()).isNotEmpty();
    }

    @Test
    public void testGetAgentOnError() {
        final Agents reference = api().get(nodeId, TestUtilities.randomString());
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isNotEmpty();
        assertThat(reference.items()).isEmpty();
    }

    private AgentsApi api() {
        return api.agentsApi();
    }
}
