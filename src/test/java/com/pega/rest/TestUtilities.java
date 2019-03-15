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

import org.jclouds.util.Strings2;

import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Static methods for generating test data.
 */
public class TestUtilities extends PegaUtils {

    public static final String TEST_CREDENTIALS_SYSTEM_PROPERTY = "test.pega.rest.credentials";
    public static final String TEST_CREDENTIALS_ENVIRONMENT_VARIABLE = TEST_CREDENTIALS_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    public static final String TEST_TOKEN_SYSTEM_PROPERTY = "test.pega.rest.token";
    public static final String TEST_TOKEN_ENVIRONMENT_VARIABLE = TEST_TOKEN_SYSTEM_PROPERTY.replaceAll("\\.", "_").toUpperCase();

    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    /**
     * Execute `args` at the `workingDir`.
     *
     * @param args list of arguments to pass to Process.
     * @param workingDir directory to execute Process within.
     * @return possible output of Process.
     * @throws Exception if Process could not be successfully executed.
     */
    public static String executionToString(final List<String> args, final Path workingDir) throws Exception {
        assertThat(args).isNotNull().isNotEmpty();
        assertThat(workingDir).isNotNull();
        assertThat(workingDir.toFile().isDirectory()).isTrue();

        final Process process = new ProcessBuilder(args)
                .directory(workingDir.toFile())
                .start();

        return Strings2.toStringAndClose(process.getInputStream());
    }

    /**
     * Generate a random String with letters only.
     *
     * @return random String.
     */
    public static String randomStringLettersOnly() {
        final StringBuilder sb = new StringBuilder();
        final Random random = new Random();
        for (int i = 0; i < 10; i++) {
            final char randomChar = CHARS[random.nextInt(CHARS.length)];
            sb.append(randomChar);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * Generate a random String with numbers and letters.
     *
     * @return random String.
     */
    public static String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * Find credentials (Basic, Bearer, or Anonymous) from system/environment.
     *
     * @return PegaAuthentication
     */
    public static PegaAuthentication inferTestAuthentication() {

        // 1.) Check for "Basic" auth credentials.
        final PegaAuthentication.Builder inferAuth = PegaAuthentication.builder();
        String authValue = retriveExternalValue(TEST_CREDENTIALS_SYSTEM_PROPERTY,
                        TEST_CREDENTIALS_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.credentials(authValue);
        } else {

            // 2.) Check for "Bearer" auth token.
            authValue = retriveExternalValue(TEST_TOKEN_SYSTEM_PROPERTY,
                            TEST_TOKEN_ENVIRONMENT_VARIABLE);
            if (authValue != null) {
                inferAuth.token(authValue);
            }
        }

        // 3.) If neither #1 or #2 find anything "Anonymous" access is assumed.
        return inferAuth.build();
    }

    private TestUtilities() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }
}
