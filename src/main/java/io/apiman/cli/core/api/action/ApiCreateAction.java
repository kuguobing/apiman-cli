/*
 * Copyright 2016 Pete Cornish
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apiman.cli.core.api.action;

import com.google.common.collect.Lists;
import io.apiman.cli.exception.ActionException;
import io.apiman.cli.core.api.ApiApi;
import io.apiman.cli.core.api.ApiMixin;
import io.apiman.cli.core.api.ServiceApi;
import io.apiman.cli.core.api.model.Api;
import io.apiman.cli.core.api.model.ApiConfig;
import io.apiman.cli.core.api.model.ApiGateway;
import io.apiman.cli.core.api.model.ServiceConfig;
import io.apiman.cli.util.ApiUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.text.MessageFormat;

/**
 * Create an API.
 *
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public class ApiCreateAction extends AbstractApiAction implements ApiMixin {
    private static final Logger LOGGER = LogManager.getLogger(ApiCreateAction.class);

    @Option(name = "--name", aliases = {"-n"}, usage = "API name", required = true)
    private String name;

    @Option(name = "--description", aliases = {"-d"}, usage = "Description")
    private String description;

    @Option(name = "--initialVersion", aliases = {"-v"}, usage = "Initial version", required = true)
    private String initialVersion;

    @Option(name = "--endpoint", aliases = {"-e"}, usage = "Endpoint", required = true)
    private String endpoint;

    @Option(name = "--endpointType", aliases = {"-t"}, usage = "Endpoint type")
    private String endpointType = "rest";

    @Option(name = "--public", aliases = {"-p"}, usage = "Public API", required = true)
    private boolean publicApi;

    @Option(name = "--gateway", aliases = {"-g"}, usage = "Gateway")
    private String gateway = "TheGateway";

    @Override
    protected String getActionName() {
        return MessageFormat.format("Create {0}", getModelName());
    }

    @Override
    protected void performAction(CmdLineParser parser) throws ActionException {
        LOGGER.debug("Creating {}", this::getModelName);

        final Api api = new Api(
                name,
                description,
                initialVersion);

        switch (serverVersion) {
            case v119:
                // legacy apiman 1.1.9 support
                createLegacyConfiguredApi(api);
                break;

            default:
                createConfiguredApi(api);
                break;
        }
    }

    /**
     * Apiman 1.2.x support
     *
     * @param api
     */
    private void createConfiguredApi(Api api) {
        final ApiConfig config = new ApiConfig(
                endpoint,
                endpointType,
                publicApi,
                Lists.newArrayList(new ApiGateway(gateway)));

        // create
        final ApiApi apiClient = buildApiClient(ApiApi.class);
        ApiUtil.invokeAndCheckResponse(() -> apiClient.create(orgName, api));

        // configure
        ApiUtil.invokeAndCheckResponse(() -> apiClient.configure(orgName, name, initialVersion, config));
    }

    /**
     * Legacy apiman 1.1.9 support.
     *
     * @param api
     */
    private void createLegacyConfiguredApi(Api api) {
        final ServiceConfig config = new ServiceConfig(
                endpoint,
                endpointType,
                publicApi,
                Lists.newArrayList(new ApiGateway(gateway)));

        // create
        final ServiceApi apiClient = buildApiClient(ServiceApi.class);
        ApiUtil.invokeAndCheckResponse(() -> apiClient.create(orgName, api));

        // configure
        ApiUtil.invokeAndCheckResponse(() -> apiClient.configure(orgName, name, initialVersion, config));
    }
}
