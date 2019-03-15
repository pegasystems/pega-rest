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
import com.pega.rest.domain.reports.Databases;
import com.squareup.okhttp.mockwebserver.MockResponse;
import org.testng.annotations.Test;

import static com.pega.rest.PegaApiMetadata.API_VERSION;
import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "unit", testName = "ReportsApiMockTest")
public class ReportsApiMockTest extends BasePegaMockTest {

    public void testGetDatabases() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource("/databases.txt")).setResponseCode(200));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final ReportsApi api = baseApi.reportsApi();

                final Databases reference = api.databases();
                assertThat(reference).isNotNull();
                assertThat(reference.errors()).isEmpty();
                assertThat(reference.items()).isNotEmpty();
                assertThat(reference.items().get(0).database()).isEqualTo("pegarules");

                assertSent(server.getMockWebServer(), getMethod, restApiPath + API_VERSION + "/databases");
            }
        }
    }

    public void testGetDatabasesOnError() throws Exception {
        try (final CloseableMockWebServer server = CloseableMockWebServer.start()) {
            server.enqueue(new MockResponse().setBody(payloadFromResource(errorsNodesFile)).setResponseCode(403));

            try (final PegaApi baseApi = api(server.getUrl("/"))) {
                final ReportsApi api = baseApi.reportsApi();

                final Databases reference = api.databases();
                assertThat(reference).isNotNull();
                assertThat(reference.items()).isEmpty();
                assertThat(reference.errors()).isNotEmpty();
                assertThat(reference.errors().get(0).message()).contains(errorMessage);
                assertThat(reference.errors().get(0).context()).isNull();

                assertSent(server.getMockWebServer(), getMethod, restApiPath + API_VERSION + "/databases");
            }
        }
    }
}
