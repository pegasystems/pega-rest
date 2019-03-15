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

package com.pega.rest.domain.reports;

import com.google.auto.value.AutoValue;
import org.jclouds.json.SerializedNames;

@AutoValue
public abstract class Table {

    public abstract String database();

    public abstract String catalog();

    public abstract String schema();

    public abstract String table();

    public abstract String tableType();

    public abstract boolean hasPZPVStream();

    public abstract String typeOfPZPVStream();

    public abstract boolean caseInsensitiveColumnNames();

    public abstract boolean deletedColumn();

    public abstract String column();

    public abstract String columnJdbcType();

    public abstract String columnVendorType();

    public abstract Integer columnLength();

    public abstract boolean columnNullable();

    public abstract boolean columnKey();

    public abstract String columnDescription();

    @SerializedNames({ "database", "catalog",
            "schema", "table",
            "tableType", "hasPZPVStream",
            "typeOfPZPVStream", "caseInsensitiveColumnNames",
            "deletedColumn", "column",
            "columnJdbcType", "columnVendorType",
            "columnLength", "columnNullable",
            "columnKey", "storeIdentifiersInUpperCase" })
    public static Table create(final String database,
                               final String catalog,
                               final String schema,
                               final String table,
                               final String tableType,
                               final boolean hasPZPVStream,
                               final String typeOfPZPVStream,
                               final boolean caseInsensitiveColumnNames,
                               final boolean deletedColumn,
                               final String column,
                               final String columnJdbcType,
                               final String columnVendorType,
                               final Integer columnLength,
                               final boolean columnNullable,
                               final boolean columnKey,
                               final String storeIdentifiersInUpperCase) {

        return new AutoValue_Table(database,
                catalog,
                schema,
                table,
                tableType,
                hasPZPVStream,
                typeOfPZPVStream,
                caseInsensitiveColumnNames,
                deletedColumn,
                column,
                columnJdbcType,
                columnVendorType,
                columnLength,
                columnNullable,
                columnKey,
                storeIdentifiersInUpperCase);
    }
}
