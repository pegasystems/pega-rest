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

import static com.pega.rest.PegaConstants.PEGA_REST_PROPERTY_ID;
import static com.pega.rest.PegaConstants.PEGA_REST_VARIABLE_ID;
import static com.pega.rest.PegaConstants.CREDENTIALS_ENVIRONMENT_VARIABLE;
import static com.pega.rest.PegaConstants.CREDENTIALS_SYSTEM_PROPERTY;
import static com.pega.rest.PegaConstants.DEFAULT_ENDPOINT;
import static com.pega.rest.PegaConstants.ENDPOINT_ENVIRONMENT_VARIABLE;
import static com.pega.rest.PegaConstants.ENDPOINT_SYSTEM_PROPERTY;
import static com.pega.rest.PegaConstants.JCLOUDS_PROPERTY_ID;
import static com.pega.rest.PegaConstants.JCLOUDS_VARIABLE_ID;
import static com.pega.rest.PegaConstants.TOKEN_ENVIRONMENT_VARIABLE;
import static com.pega.rest.PegaConstants.TOKEN_SYSTEM_PROPERTY;

import com.google.common.base.Throwables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

import org.jclouds.javax.annotation.Nullable;

/**
 * Collection of static methods to be used globally.
 */
@SuppressWarnings("PMD.TooManyStaticImports")
public class PegaUtils {

    // global gson parser object
    public static final Gson GSON_PARSER = new Gson();
    public static final JsonParser JSON_PARSER = new JsonParser();

    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    /**
     * Convert passed Iterable into an ImmutableList.
     * 
     * @param <T> an arbitrary type.
     * @param input the Iterable to copy.
     * @return ImmutableList or empty ImmutableList if `input` is null.
     */
    public static <T> List<T> nullToEmpty(final Iterable<? extends T> input) {
        return (List<T>) (input == null ? ImmutableList.<T> of() : ImmutableList.copyOf(input));
    }

    /**
     * Convert passed Map into an ImmutableMap.
     * 
     * @param <K> an arbitrary type.
     * @param <V> an arbitrary type.
     * @param input the Map to copy.
     * @return ImmutableMap or empty ImmutableMap if `input` is null.
     */
    public static <K, V> Map<K, V> nullToEmpty(final Map<? extends K, ? extends V> input) {
        return (Map<K, V>) (input == null ? ImmutableMap.<K, V> of() : ImmutableMap.copyOf(input));
    }

    /**
     * Convert passed Map into a JsonElement.
     * 
     * @param input the Map to convert.
     * @return JsonElement or empty JsonElement if `input` is null.
     */
    public static JsonElement nullToJsonElement(final Map input) {
        return GSON_PARSER.toJsonTree(nullToEmpty(input));
    }

    /**
     * Convert passed Map into a JsonElement.
     * 
     * @param input the Map to convert.
     * @return JsonElement or empty JsonElement if `input` is null.
     */
    public static JsonElement nullToJsonElement(final JsonElement input) {
        return input != null ? input : GSON_PARSER.toJsonTree(ImmutableMap.of());
    }

    /**
     * Convert passed String into a JsonElement.
     * 
     * @param input the String to convert.
     * @return JsonElement or empty JsonElement if `input` is null.
     */
    public static JsonElement nullToJsonElement(final String input) {
        return JSON_PARSER.parse(input != null ? input : "{}");
    }

    /**
     * If the passed systemProperty is non-null we will attempt to query
     * the `System Properties` for a value and return it. If no value
     * was found, and environmentVariable is non-null, we will attempt to
     * query the `Environment Variables` for a value and return it. If
     * both are either null or can't be found than null will be returned.
     * 
     * @param systemProperty possibly existent System Property.
     * @param environmentVariable possibly existent Environment Variable.
     * @return found external value or null.
     */
    public static String retriveExternalValue(@Nullable final String systemProperty,
            @Nullable final String environmentVariable) {

        // 1.) Search for System Property
        if (systemProperty != null) {
            final String value = System.getProperty(systemProperty);
            if (value != null) {
                return value;
            }
        }

        if (environmentVariable != null) {
            final String value = System.getenv(environmentVariable);
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    /**
     * Find endpoint searching first within `System Properties` and
     * then within `Environment Variables` returning whichever has a
     * value first.
     *
     * @return endpoint or null if it can't be found.
     */
    public static String inferEndpoint() {
        final String possibleValue = PegaUtils
                .retriveExternalValue(ENDPOINT_SYSTEM_PROPERTY,
                        ENDPOINT_ENVIRONMENT_VARIABLE);
        return possibleValue != null ? possibleValue : DEFAULT_ENDPOINT;
    }

    /**
     * Find credentials (Basic, Bearer, or Anonymous) from system/environment.
     *
     * @return PegaAuthentication
     */
    public static PegaAuthentication inferAuthentication() {

        // 1.) Check for "Basic" auth credentials.
        final PegaAuthentication.Builder inferAuth = PegaAuthentication.builder();
        String authValue = PegaUtils
                .retriveExternalValue(CREDENTIALS_SYSTEM_PROPERTY,
                        CREDENTIALS_ENVIRONMENT_VARIABLE);
        if (authValue != null) {
            inferAuth.credentials(authValue);
        } else {

            // 2.) Check for "Bearer" auth token.
            authValue = PegaUtils
                    .retriveExternalValue(TOKEN_SYSTEM_PROPERTY,
                            TOKEN_ENVIRONMENT_VARIABLE);
            if (authValue != null) {
                inferAuth.token(authValue);
            }
        }

        // 3.) If neither #1 or #2 find anything "Anonymous" access is assumed.
        return inferAuth.build();
    }

    /**
     * Find jclouds overrides (e.g. Properties) first searching within System
     * Properties and then within Environment Variables (former takes precedance).
     * 
     * @return Properties object with populated jclouds properties.
     */
    public static Properties inferOverrides() {
        final Properties overrides = new Properties();

        // 1.) Iterate over system properties looking for relevant properties.
        final Properties systemProperties = System.getProperties();
        final Enumeration<String> enums = (Enumeration<String>) systemProperties.propertyNames();
        while (enums.hasMoreElements()) {
            final String key = enums.nextElement();
            if (key.startsWith(PEGA_REST_PROPERTY_ID)) {
                final int index = key.indexOf(JCLOUDS_PROPERTY_ID);
                final String trimmedKey = key.substring(index, key.length());
                overrides.put(trimmedKey, systemProperties.getProperty(key));
            }
        }

        // 2.) Iterate over environment variables looking for relevant variables. System
        //     Properties take precedence here so if the same property was already found
        //     there then we don't add it or attempt to override.
        for (final Map.Entry<String, String> entry : System.getenv().entrySet()) {
            if (entry.getKey().startsWith(PEGA_REST_VARIABLE_ID)) {
                final int index = entry.getKey().indexOf(JCLOUDS_VARIABLE_ID);
                final String trimmedKey = entry.getKey()
                        .substring(index, entry.getKey().length())
                        .toLowerCase()
                        .replaceAll("_", ".");
                if (!overrides.containsKey(trimmedKey)) {
                    overrides.put(trimmedKey, entry.getValue());
                }
            }
        }
        
        return overrides;
    }

    /**
     * Add the passed environment variables to the currently existing env-vars.
     * 
     * @param addEnvVars the env-vars to add.
     */
    public static void addEnvironmentVariables(final Map<String, String> addEnvVars) {
        Objects.requireNonNull(addEnvVars, "Must pass non-null Map");
        final Map<String, String> newenv = Maps.newHashMap(System.getenv());
        newenv.putAll(addEnvVars);
        setEnvironmentVariables(newenv);
    }

    /**
     * Remove the passed environment variables keys from the environment.
     * 
     * @param removeEnvVars the env-var keys to be removed.
     */
    public static void removeEnvironmentVariables(final Collection<String> removeEnvVars) {
        Objects.requireNonNull(removeEnvVars, "Must pass non-null Collection");
        final Map<String, String> newenv = Maps.newHashMap(System.getenv());
        newenv.keySet().removeAll(removeEnvVars);
        setEnvironmentVariables(newenv);
    }

    /**
     * Re-set the environment variables with passed map.
     * 
     * @param newEnvVars map to reset env-vars with.
     */
    public static void setEnvironmentVariables(final Map<String, String> newEnvVars) {
        Objects.requireNonNull(newEnvVars, "Must pass non-null Map");

        try {
            final Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            final Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            final Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newEnvVars);
            final Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            final Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newEnvVars);
        } catch (final ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            final Class[] classes = Collections.class.getDeclaredClasses();
            final Map<String, String> env = System.getenv();
            for (final Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    try {
                        final Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        final Object obj = field.get(env);
                        final Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(newEnvVars);
                    } catch (final NoSuchFieldException | IllegalAccessException e2) {
                        throw Throwables.propagate(e2);
                    }
                }
            }
        }
    }

    /**
     * Parse the content of a CSV file into a list of list-strings.
     *
     * @param csvContent csv file content to parse.
     * @return list of list-strings representing the CSV file content.
     */
    public static List<List<String>> parseCSV(final String csvContent) {

        final List<List<String>> parsedLines = Lists.newArrayList();
        try (final Scanner scanner = new Scanner(csvContent)) {
            boolean columnRow = true;
            while (scanner.hasNext()) {
                if (columnRow) {
                    scanner.nextLine();
                    columnRow = false;
                    continue;
                } else {
                    final List<String> parsedLine = parseLine(scanner.nextLine());
                    parsedLines.add(parsedLine);
                }
            }
        }

        return parsedLines;
    }

    private static List<String> parseLine(final String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    private static List<String> parseLine(final String cvsLine,
                                          char separators,
                                          char customQuote) {

        final List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null || cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        final char[] chars = cvsLine.toCharArray();

        for (final char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }
                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer(); //NOPMD
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

    protected PegaUtils() {
        throw new UnsupportedOperationException("Purposefully not implemented");
    }
}
