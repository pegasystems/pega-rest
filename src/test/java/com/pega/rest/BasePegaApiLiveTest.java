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

import com.pega.rest.config.PegaAuthenticationModule;
import java.util.Properties;

import org.jclouds.Constants;
import org.jclouds.apis.BaseApiLiveTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

@Test(groups = "live")
public class BasePegaApiLiveTest extends BaseApiLiveTest<PegaApi> {

    protected final PegaAuthentication pegaAuthentication;

    public BasePegaApiLiveTest() {
        provider = "pega";
        this.pegaAuthentication = TestUtilities.inferTestAuthentication();
    }

    @Override
    protected Iterable<Module> setupModules() {
        final PegaAuthenticationModule credsModule = new PegaAuthenticationModule(this.pegaAuthentication);
        return ImmutableSet.<Module> of(getLoggingModule(), credsModule);
    }

    @Override
    protected Properties setupProperties() {
        final Properties overrides = super.setupProperties();
        overrides.setProperty(Constants.PROPERTY_MAX_RETRIES, "5");
        return overrides;
    }
}
