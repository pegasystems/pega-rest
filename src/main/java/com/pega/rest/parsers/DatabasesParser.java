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

package com.pega.rest.parsers;

import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.pega.rest.PegaUtils;
import com.pega.rest.domain.reports.Database;
import com.pega.rest.domain.reports.Databases;
import org.jclouds.http.HttpResponse;
import org.jclouds.util.Strings2;

import javax.inject.Singleton;
import java.io.InputStream;
import java.util.List;

/**
 * Parse body of response into Databases.
 */
@Singleton
public class DatabasesParser implements Function<HttpResponse, Databases> {

    @Override
    public Databases apply(final HttpResponse input) {
        try (final InputStream inputStream = input.getPayload().openStream()) {

            final String possibleDatabases = Strings2.toStringAndClose(inputStream);
            final List<List<String>> parsedDatabases = PegaUtils.parseCSV(possibleDatabases);
            final List<Database> databases = Lists.newArrayList();
            for (final List<String> database : parsedDatabases) {
                databases.add(Database.create(database.get(0),
                        database.get(1),
                        database.get(2),
                        database.get(3),
                        database.get(4),
                        database.get(5),
                        database.get(6),
                        Boolean.valueOf(database.get(7))));
            }

            return Databases.create(databases, null);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
