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

package com.pega.rest.fallbacks;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pega.rest.PegaUtils;
import com.pega.rest.domain.agents.AgentsStatus;
import com.pega.rest.domain.agents.Agents;
import com.pega.rest.domain.common.Error;
import com.pega.rest.domain.nodes.Nodes;
import com.pega.rest.domain.nodes.QuiesceStatus;
import com.pega.rest.domain.nodes.SystemSettings;
import com.pega.rest.domain.reports.Databases;
import org.jclouds.Fallback;

import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Throwables.propagate;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class PegaFallbacks {

    public static final String ERROR = "error";
    public static final String ERRORS = ERROR + "s";

    public static final String ERROR_MESSAGE_PROPERTY = "errorMessage";

    public static final class NodesOnError implements Fallback<Object> {
        @Override
        public Object createOrPropagate(final Throwable thr) {
            if (checkNotNull(thr, "throwable") != null) {
                return createNodesFromErrors(getErrors(thr.getMessage()));
            }
            throw propagate(thr);
        }
    }

    public static Nodes createNodesFromErrors(final List<Error> errors) {
        return Nodes.create(null, errors);
    }

    public static final class SystemSettingsOnError implements Fallback<Object> {
        @Override
        public Object createOrPropagate(final Throwable thr) {
            if (checkNotNull(thr, "throwable") != null) {
                return createSystemSettingsOnError(getErrors(thr.getMessage()));
            }
            throw propagate(thr);
        }
    }

    public static SystemSettings createSystemSettingsOnError(final List<Error> errors) {
        return SystemSettings.create(null, errors);
    }

    public static final class QuiesceStatusOnError implements Fallback<Object> {
        @Override
        public Object createOrPropagate(final Throwable thr) {
            if (checkNotNull(thr, "throwable") != null) {
                return createQuiesceStatusOnError(getErrors(thr.getMessage()));
            }
            throw propagate(thr);
        }
    }

    public static QuiesceStatus createQuiesceStatusOnError(final List<Error> errors) {
        return QuiesceStatus.create(null, null, errors);
    }

    public static final class AgentsOnError implements Fallback<Object> {
        @Override
        public Object createOrPropagate(final Throwable thr) {
            if (checkNotNull(thr, "throwable") != null) {
                return createAgentsOnError(getErrors(thr.getMessage()));
            }
            throw propagate(thr);
        }
    }

    public static Agents createAgentsOnError(final List<Error> errors) {
        return Agents.create(null, errors);
    }

    public static final class AgentsStatusOnError implements Fallback<Object> {
        @Override
        public Object createOrPropagate(final Throwable thr) {
            if (checkNotNull(thr, "throwable") != null) {
                return createAgentsStatusOnError(getErrors(thr.getMessage()));
            }
            throw propagate(thr);
        }
    }

    public static AgentsStatus createAgentsStatusOnError(final List<Error> errors) {
        return AgentsStatus.create(null, errors);
    }

    public static final class DatabasesOnError implements Fallback<Object> {
        @Override
        public Object createOrPropagate(final Throwable thr) {
            if (checkNotNull(thr, "throwable") != null) {
                return createDatabasesOnError(getErrors(thr.getMessage()));
            }
            throw propagate(thr);
        }
    }

    public static Databases createDatabasesOnError(final List<Error> errors) {
        return Databases.create(null, errors);
    }

    /**
     * Parse list of Error(s) from output.
     *
     * @param output json containing errors hash
     * @return List of Error's or empty list if none could be found
     */
    public static List<Error> getErrors(final String output) {

        final List<Error> errors = Lists.newArrayList();

        try {

            final JsonElement element = PegaUtils.JSON_PARSER.parse(output.trim());
            final JsonObject object = element.getAsJsonObject();

            // Pega API hands back different types of error list objects
            // so account for them here.
            if (object.has(ERROR)) {
                errors.addAll(parseErrors(object, ERROR));
            } else if (object.has(ERRORS)) {
                errors.addAll(parseErrors(object, ERRORS));
            } else {
                throw new RuntimeException(output);
            }
        } catch (final Exception e) {
            final Error error = Error.create("Failed to parse output: message=" + e.getMessage(),
                    output);
            errors.add(error);
        }

        return errors;
    }

    /**
     * Parse Error(s) from a given JsonObject given its proper name.
     *
     * @param jsonObject the JsonObject to parse Errors from.
     * @param name the name of the property on the jsonObject to parse Errors from.
     * @return list of Error objects.
     */
    private static List<Error> parseErrors(final JsonObject jsonObject,
                                          final String name) {

        final List<Error> errors = Lists.newArrayList();
        final JsonArray errorsArray = jsonObject.get(name).getAsJsonArray();
        final Iterator<JsonElement> it = errorsArray.iterator();
        while (it.hasNext()) {
            final JsonObject obj = it.next().getAsJsonObject();
            errors.add(parseErrorsContent(obj));
        }
        return errors;
    }

    /**
     * Parse a single Error object from a given JsonObject.
     *
     * @param errorObject the JsonObject to parse an Error from.
     * @return Error object derived from parsing the passed JsonObject.
     */
    private static Error parseErrorsContent(final JsonObject errorObject) {
        if (errorObject.has(ERROR_MESSAGE_PROPERTY)) {
            return Error.create(errorObject.get(ERROR_MESSAGE_PROPERTY).getAsString(), null); //NOPMD
        } else {
            throw new RuntimeException("JsonObject contained no 'errorMessage' property: obj=" + errorObject.getAsString());
        }
    }
}
