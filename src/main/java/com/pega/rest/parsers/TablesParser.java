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
import com.pega.rest.domain.reports.Table;
import com.pega.rest.domain.reports.Tables;
import org.jclouds.http.HttpResponse;
import org.jclouds.util.Strings2;

import javax.inject.Singleton;
import java.io.InputStream;
import java.util.List;

/**
 * Parse body of response into Tables.
 */
@Singleton
public class TablesParser implements Function<HttpResponse, Tables> {

    @Override
    public Tables apply(final HttpResponse input) {
        try (final InputStream inputStream = input.getPayload().openStream()) {

            final String possibleTables = Strings2.toStringAndClose(inputStream);
            final List<List<String>> parsedTables = PegaUtils.parseCSV(possibleTables);
            final List<Table> tables = Lists.newArrayList();
            for (final List<String> table : parsedTables) {
                tables.add(Table.create(table.get(0),
                        table.get(1),
                        table.get(2),
                        table.get(3),
                        table.get(4),
                        Boolean.valueOf(table.get(5)),
                        table.get(6),
                        Boolean.valueOf(table.get(7)),
                        Boolean.valueOf(table.get(8)),
                        table.get(9),
                        table.get(10),
                        table.get(11),
                        Integer.valueOf(table.get(12)),
                        Boolean.valueOf(table.get(13)),
                        Boolean.valueOf(table.get(14)),
                        table.get(15)));
            }

            return Tables.create(tables, null);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
