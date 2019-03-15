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

package com.pega.rest.domain.agents;

import com.google.auto.value.AutoValue;
import org.jclouds.json.SerializedNames;

@AutoValue
public abstract class Instance {

    public abstract String queueClass();

    public abstract String lastRunEnd();

    public abstract String mode();

    public abstract Boolean isEnabled();

    public abstract String runDuration();

    public abstract String name();

    public abstract String lastRunStart();

    public abstract String scheduling();

    public abstract String ruleSet();

    public abstract String category();

    public abstract String status();

    public abstract String nextRunStart();

    public abstract String nodeId();

    @SerializedNames({ "queue_class", "last_run_end",
            "mode", "is_enabled",
            "run_duration", "name",
            "last_run_start", "scheduling",
            "rule_set", "category",
            "status", "next_run_start",
            "node_id" })
    public static Instance create(final String queueClass,
                                  final String lastRunEnd,
                                  final String mode,
                                  final Boolean isEnabled,
                                  final String runDuration,
                                  final String name,
                                  final String lastRunStart,
                                  final String scheduling,
                                  final String ruleSet,
                                  final String category,
                                  final String status,
                                  final String nextRunStart,
                                  final String nodeId) {

        return new AutoValue_Instance(queueClass,
                lastRunEnd,
                mode,
                isEnabled,
                runDuration,
                name,
                lastRunStart,
                scheduling,
                ruleSet,
                category,
                status,
                nextRunStart,
                nodeId);
    }
}
