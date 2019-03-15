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
import com.pega.rest.domain.nodes.Nodes;
import com.pega.rest.domain.nodes.SystemSettings;
import org.testng.annotations.Test;

import static com.pega.rest.TestUtilities.randomString;
import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "live", testName = "NodeApiLiveTest", singleThreaded = true)
public class NodeApiLiveTest extends BasePegaApiLiveTest {

    public String nodeId;

    @Test
    public void testGetNodes() {
        final Nodes reference = api().nodes();
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isEmpty();
        assertThat(reference.results()).isNotEmpty();
        assertThat(reference.results().get(0).runningState()).isEqualTo("Running");

        nodeId = reference.results().get(0).nodeId();
    }

    @Test (dependsOnMethods = "testGetNodes")
    public void testGetSettings() {
        final SystemSettings reference = api().systemSettings(nodeId);
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isEmpty();
        assertThat(reference.results()).isNotEmpty();
    }

    @Test
    public void testGetSettingsOnError() {
        final SystemSettings reference = api().systemSettings(randomString());
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isNotEmpty();
        assertThat(reference.errors().get(0).message()).contains("Specified NodeId is invalid");
        assertThat(reference.results()).isEmpty();
    }

    private NodeApi api() {
        return api.nodeApi();
    }
}
