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

package com.pega.rest;

import com.pega.rest.config.PegaHttpApiModule;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.rest.internal.BaseHttpApiMetadata;

import java.net.URI;
import java.util.Properties;

@AutoService(ApiMetadata.class)
public class PegaApiMetadata extends BaseHttpApiMetadata<PegaApi> {

    public static final String API_VERSION = "v1";
    public static final String BUILD_VERSION = "8.3";

    @Override
    public Builder toBuilder() {
        return new Builder().fromApiMetadata(this);
    }

    public PegaApiMetadata() {
        this(new Builder());
    }

    protected PegaApiMetadata(final Builder builder) {
        super(builder);
    }

    public static Properties defaultProperties() {
        final Properties properties = BaseHttpApiMetadata.defaultProperties();
        return properties;
    }

    public static class Builder extends BaseHttpApiMetadata.Builder<PegaApi, Builder> {

        protected Builder() {
            super(PegaApi.class);
            id("pega").name("Pega API")
                    .identityName("administrator@pega.com")
                    .credentialName("install")
                    .defaultIdentity("")
                    .defaultCredential("")
                    .documentation(URI.create("http://127.0.0.1:8080/prweb/api/v1/docs"))
                    .version(API_VERSION)
                    .buildVersion(BUILD_VERSION)
                    .defaultEndpoint("http://127.0.0.1:8080/prweb")
                    .defaultProperties(PegaApiMetadata.defaultProperties())
                    .defaultModules(ImmutableSet.<Class<? extends Module>> of(PegaHttpApiModule.class));
        }

        @Override
        public PegaApiMetadata build() {
            return new PegaApiMetadata(this);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Builder fromApiMetadata(final ApiMetadata in) {
            return this;
        }
    }
}
