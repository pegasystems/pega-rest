
[![Build Status](https://travis-ci.org/pegasystems/pega-rest.svg?branch=master)](https://travis-ci.org/pegasystems/pega-rest)
[![codecov](https://codecov.io/gh/pegasystems/pega-rest/branch/master/graph/badge.svg)](https://codecov.io/gh/pegasystems/pega-rest)
[![Download](https://api.bintray.com/packages/pegasystems/java-libraries/pega-rest/images/download.svg) ](https://bintray.com/pegasystems/java-libraries/pega-rest/_latestVersion)
[![Stack Overflow](https://img.shields.io/badge/stack%20overflow-pega&#8211;rest-4183C4.svg)](https://stackoverflow.com/questions/tagged/pega+rest)

# pega-rest
![alt tag](https://www.pega.com/sites/default/files/styles/640/public/media/images/2018-06/infinity-logo-prevcard.png)

java client, based on jclouds, to interact with Pega's REST API.

## On jclouds, apis and endpoints
Being built on top of `jclouds` means things are broken up into [Apis](https://github.com/pegasystems/pega-rest/tree/master/src/main/java/com/pega/rest/features).
`Apis` are just Interfaces that are analagous to a resource provided by the server-side program (e.g. /api/branches, /api/pullrequest, /api/commits, etc..).
The methods within these Interfaces are analagous to an endpoint provided by these resources (e.g. GET /api/branches/my-branch, GET /api/pullrequest/123, DELETE /api/commits/456, etc..).
The user only needs to be concerned with which `Api` they need and then calling its various methods. These methods, much like any java library, return domain objects
(e.g. POJO's) modeled after the json returned by `pega`.

Interacting with the remote service becomes transparent and allows developers to focus on getting
things done rather than the internals of the API itself, or how to build a client, or how to parse the json.

## On new features

New Api's or endpoints are generally added as needed and/or requested. If there is something you want
to see just open an ISSUE and ask or send in a PullRequest. However, putting together a PullRequest
for a new feature is generally the faster route to go as it's much easier to review a PullRequest
than to create one ourselves. There is no problem doing so of course but if you need something done
now than a PullRequest is your best bet otherwise you may have to patiently wait for one of our
contributors to take up the work.

## Latest Release

Can be sourced from jcenter like so:

    <dependency>
      <groupId>com.pega</groupId>
      <artifactId>pega-rest</artifactId>
      <version>X.Y.Z</version>
      <classifier>sources|tests|javadoc|all</classifier> (Optional)
    </dependency>

## Documentation

javadocs can be found via [github pages here](http://pegasystems.github.io/pega-rest/docs/javadoc/)

## Examples on how to build a _PegaClient_

When using `Basic` (e.g. username and password) authentication:

    PegaClient client = PegaClient.builder()
    .endPoint("http://127.0.0.1:7990") // Optional and can be sourced from system/env. Falls back to http://127.0.0.1:7990
    .credentials("admin:password") // Optional and can be sourced from system/env and can be Base64 encoded.
    .build();

    Version version = client.api().systemApi().version();

When using `Bearer` (e.g. token) authentication:

    PegaClient client = PegaClient.builder()
    .endPoint("http://127.0.0.1:7990") // Optional and can be sourced from system/env. Falls back to http://127.0.0.1:8080/prweb
    .token("123456789abcdef") // Optional and can be sourced from system/env.
    .build();

    Version version = client.api().systemApi().version();

When using `Anonymous` authentication or sourcing from system/environment (as described below):

    PegaClient client = PegaClient.builder()
    .endPoint("http://127.0.0.1:7990") // Optional and can be sourced from system/env. Falls back to http://127.0.0.1:8080/prweb
    .build();

    Version version = client.api().systemApi().version();

## On `System Property` and `Environment Variable` setup

Client's do NOT need to supply the endPoint or authentication as part of instantiating the
_PegaClient_ object. Instead one can supply them through `System Properties`, `Environment
Variables`, or a combination of the 2. `System Properties` will be searched first and if not
found we will attempt to query the `Environment Variables`. If neither turns up anything
than anonymous access is assumed.

Setting the `endpoint` can be done like so (searched in order):

    `System.setProperty("pega.rest.endpoint", "http://my-pega-instance:8080/prweb")`
    `export PEGA_REST_ENDPOINT=http://my-pega-instance:8080/prweb`

Setting the `credentials`, which represents `Basic` authentication and is optionally Base64 encoded, can be done like so (searched in order):

    `System.setProperty("pega.rest.credentials", "username:password")`
    `export PEGA_REST_CREDENTIALS=username:password`

Setting the `token`, which represents `Bearer` authentication, can be done like so (searched in order):

    `System.setProperty("pega.rest.token", "abcdefg1234567")`
    `export PEGA_REST_TOKEN=abcdefg1234567`

## On Overrides

Because we are built on top of jclouds we can take advantage of overriding various internal _HTTP_ properties by
passing in a `Properties` object or, and in following with the spirit of this library, configuring them
through `System Properties` of `Environment Variables`. The properties a given client can configure can be
found [HERE](https://github.com/jclouds/jclouds/blob/master/core/src/main/java/org/jclouds/Constants.java).

When configuring through a `Properties` object you must pass in the keys exactly as they are named within jclouds:

    Properties props = new Properties();
    props.setProperty("jclouds.so-timeout", "60000");
    props.setProperty("jclouds.connection-timeout", "120000");

    PegaClient client = PegaClient.builder()
    .overrides(props)
    .build();

    Version version = client.api().systemApi().version();

When configuring through `System Properties` you must prepend the jclouds name with `pega.rest.`:

    System.setProperty("pega.rest.jclouds.so-timeout", "60000");
    System.setProperty("pega.rest.jclouds.connection-timeout", "120000");

    PegaClient client = PegaClient.builder()
    .build();

    Version version = client.api().systemApi().version();

When configuring through `Environment Variables` you must CAPITALIZE all characters,
replace any `.` with `_`, and prepend the jclouds name with `PEGA_REST_`:

    export PEGA_REST_JCLOUDS_SO-TIMEOUT=60000
    export PEGA_REST_JCLOUDS_CONNECTION-TIMEOUT=120000

    PegaClient client = PegaClient.builder()
    .build();

    Version version = client.api().systemApi().version();

It should be noted that when using this feature a merge happens behind the scenes between all
possible ways one can pass in _overrides_. Meaning if you pass in a `Properties` object, and
there are `System Properties` and `Environment Variables` set, then all 3 will be merged into
a single `Properties` object which in turn will be passed along to _jclouds_. When it comes to
precedence passed in `Properties` take precedence over `System Properties` which in turn
take precedence over `Environment Variables`.

## Understanding Error objects

When something pops server-side `pega` will hand us back a list of [Error](https://github.com/pegasystems/pega-rest/blob/master/src/main/java/com/pega/rest/domain/common/Error.java) objects. Instead of failing and/or throwing an exception at runtime we attach this List of `Error` objects
to most [domain](https://github.com/pegasystems/pega-rest/tree/master/src/main/java/com/pega/rest/domain) objects. Thus, it is up to the user to check the handed back domain object to see if the attached List is empty, and if not, iterate over the `Error` objects to see if it's something
truly warranting an exception. List of `Error` objects itself will always be non-null but in most cases empty (unless something has failed).

An example on how one might proceed:

    Nodes nodes = client.api().nodeApi().nodes();
    if (nodes.errors().size() > 0) {
        for(Error error : nodes.errors()) {
            if (error.message().matches(".*Something I REALLY care about*")) {
                throw new RuntimeException(error.message());
            }
        }
    }


## Examples

The [mock](https://github.com/pegasystems/pega-rest/tree/master/src/test/java/com/pega/rest/features) and [live](https://github.com/pegasystems/pega-rest/tree/master/src/test/java/com/pega/rest/features) tests provide many examples
that you can use in your own code. If there are any questions feel free to open an issue and ask.

## Components

- jclouds \- used as the backend for communicating with Pega's REST API
- AutoValue \- used to create immutable value types both to and from the Pega instance

## Testing

Running mock tests can be done like so:

    ./gradlew mockTest

Running integration tests can be done like so (requires Pega instance):

    ./gradlew integTest

Various [properties](https://github.com/pegasystems/pega-rest/tree/master/gradle.properties) exist for you to configure how the `integTest` task can be run should the defaults not suffice.

# Additional Resources

* [Apache jclouds](https://jclouds.apache.org/start/)
