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
public abstract class Database {

    public abstract String database();

    public abstract String productName();

    public abstract String productVersion();

    public abstract String driverName();

    public abstract String driverVersion();

    public abstract String driverMajorVersion();

    public abstract String driverMinorVersion();

    public abstract boolean storeIdentifiersInUpperCase();


    @SerializedNames({ "database", "productName",
            "productVersion", "driverName",
            "driverVersion", "driverMajorVersion",
            "driverMinorVersion", "storeIdentifiersInUpperCase" })
    public static Database create(final String database,
                                  final String productName,
                                  final String productVersion,
                                  final String driverName,
                                  final String driverVersion,
                                  final String driverMajorVersion,
                                  final String driverMinorVersion,
                                  final boolean storeIdentifiersInUpperCase) {

        return new AutoValue_Database(database,
                productName,
                productVersion,
                driverName,
                driverVersion,
                driverMajorVersion,
                driverMinorVersion,
                storeIdentifiersInUpperCase);
    }
}
