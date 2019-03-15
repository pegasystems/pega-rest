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

package com.pega.rest.features;

import com.pega.rest.domain.agents.AgentsStatus;
import com.pega.rest.domain.agents.Agents;
import com.pega.rest.fallbacks.PegaFallbacks;
import com.pega.rest.filters.PegaAuthenticationFilter;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SelectJson;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@RequestFilters(PegaAuthenticationFilter.class)
@Path("/api/{jclouds.api-version}/nodes")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public interface AgentsApi {

    @Named("agents:list")
    @Path("/{nodeID}/agents")
    @SelectJson("data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(PegaFallbacks.AgentsOnError.class)
    @GET
    Agents list(@PathParam("nodeID") String nodeID);

    @Named("agents:get")
    @Path("/{nodeID}/agents/{agentID}")
    @SelectJson("data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(PegaFallbacks.AgentsOnError.class)
    @GET
    Agents get(@PathParam("nodeID") String nodeID,
               @PathParam("agentID") String agentID);

    @Named("agents:stop")
    @Path("/{nodeID}/agents/{agentID}")
    @SelectJson("data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(PegaFallbacks.AgentsStatusOnError.class)
    @DELETE
    AgentsStatus stop(@PathParam("nodeID") String nodeID,
                      @PathParam("agentID") String agentID);

    @Named("agents:start")
    @Path("/{nodeID}/agents/{agentID}")
    @SelectJson("data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(PegaFallbacks.AgentsStatusOnError.class)
    @POST
    AgentsStatus start(@PathParam("nodeID") String nodeID,
                      @PathParam("agentID") String agentID);

    @Named("agents:restart")
    @Path("/{nodeID}/agents/{agentID}")
    @SelectJson("data")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(PegaFallbacks.AgentsStatusOnError.class)
    @PUT
    AgentsStatus restart(@PathParam("nodeID") String nodeID,
                       @PathParam("agentID") String agentID);
}

