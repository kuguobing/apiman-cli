/*
 * Copyright 2016 Pete Cornish
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apiman.cli.core.api;

import io.apiman.cli.core.api.model.*;
import retrofit.client.Response;
import retrofit.mime.TypedString;

import java.util.List;

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public interface VersionAgnosticApi {
    Response create(String orgName, Api api);

    Response createVersion(String orgName, String apiName, ApiVersion apiVersion);

    List<Api> list(String orgName);

    Api fetch(String orgName, String apiName);

    Api fetchVersion(String orgName, String apiName, String version);

    Response configure(String orgName, String apiName,
                       String version, ApiConfig config);

    Response addPolicy(String orgName, String apiName,
                       String version, ApiPolicy policyConfig);

    Response setDefinition(String orgName, String apiName,
                       String version, String definitionType,  TypedString definition);

    List<ApiPolicy> fetchPolicies(String orgName, String serviceName,
                                  String version);

    Response configurePolicy(String orgName, String apiName,
                             String apiVersion, Long policyId, ApiPolicy policyConfig);
}
