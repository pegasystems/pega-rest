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
import java.io.InputStream;
import javax.inject.Singleton;

import com.pega.rest.domain.common.RawContent;
import org.jclouds.http.HttpResponse;
import org.jclouds.util.Strings2;

/**
 * Parse body of response into RawContent.
 */
@Singleton
public class RawContentParser implements Function<HttpResponse, RawContent> {

    @Override
    public RawContent apply(final HttpResponse input) {
        try (final InputStream inputStream = input.getPayload().openStream()) {
            final String value = Strings2.toStringAndClose(inputStream);
            return RawContent.create(value, null);
        } catch (final Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
