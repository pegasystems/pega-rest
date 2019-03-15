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
import com.pega.rest.PegaUtils;
import com.pega.rest.domain.common.Error;
import com.pega.rest.domain.common.ErrorsHolder;
import com.pega.rest.domain.common.Items;
import org.jclouds.json.SerializedNames;

import java.util.List;

@AutoValue
public abstract class Tables implements Items<Table>, ErrorsHolder {

    @SerializedNames({ "tables", "error" })
    public static Tables create(final List<Table> tables,
                                final List<Error> errors) {

        return new AutoValue_Tables(PegaUtils.nullToEmpty(tables),
            PegaUtils.nullToEmpty(errors));
    }
}
