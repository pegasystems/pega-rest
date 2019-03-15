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
import com.pega.rest.domain.reports.Database;
import com.pega.rest.domain.reports.Databases;
import com.pega.rest.domain.reports.Table;
import com.pega.rest.domain.reports.Tables;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "live", testName = "ReportsApiLiveTest", singleThreaded = true)
public class ReportsApiLiveTest extends BasePegaApiLiveTest {

    @Test
    public void testGetDatabases() {
        final Databases reference = api().databases();
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isEmpty();
        assertThat(reference.items()).isNotEmpty();
        boolean foundPegaRules = false;
        for (final Database database : reference.items()) {
            if (database.database().equalsIgnoreCase("pegarules")) {
                foundPegaRules = true;
                break;
            }
        }
        assertThat(foundPegaRules).isTrue();
    }

    @Test
    public void testGetTables() {
        final Tables reference = api().tables();
        assertThat(reference).isNotNull();
        assertThat(reference.errors()).isEmpty();
        assertThat(reference.items()).isNotEmpty();
        boolean foundPegaData = false;
        for (final Table database : reference.items()) {
            if (database.database().equalsIgnoreCase("PegaDATA")) {
                foundPegaData = true;
                break;
            }
        }
        assertThat(foundPegaData).isTrue();
    }

    private ReportsApi api() {
        return api.reportsApi();
    }
}
