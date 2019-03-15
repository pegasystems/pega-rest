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

package com.pega.rest.domain.nodes;

import com.google.auto.value.AutoValue;
import org.jclouds.json.SerializedNames;

@AutoValue
public abstract class Metrics {

    public abstract String requestors();

    public abstract String timeouts();

    public abstract String users();

    public abstract String agents();

    @SerializedNames({ "requestors", "timeouts", "users", "agents" })
    public static Metrics create(final String requestors,
                                 final String timeouts,
                                 final String users,
                                 final String agents) {

        return new AutoValue_Metrics(requestors,
            timeouts,
            users,
            agents);
    }
}
