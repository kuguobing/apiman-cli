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

package io.apiman.cli.core.gateway.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.apiman.cli.core.common.command.ModelCreateCommand;
import io.apiman.cli.core.gateway.GatewayApi;
import io.apiman.cli.core.gateway.GatewayMixin;
import io.apiman.cli.exception.CommandException;
import io.apiman.cli.core.gateway.model.Gateway;
import io.apiman.cli.core.gateway.model.GatewayConfig;
import io.apiman.cli.core.gateway.model.GatewayType;
import io.apiman.cli.util.MappingUtil;
import org.kohsuke.args4j.Option;

/**
 * Shared functionality for commands requiring a {@link Gateway}.
 *
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public abstract class AbstractGatewayCreateCommand extends ModelCreateCommand<Gateway, GatewayApi>
        implements GatewayMixin {

    @Option(name = "--description", aliases = {"-d"}, usage = "Description")
    private String description;

    @Option(name = "--endpoint", aliases = {"-e"}, usage = "Endpoint", required = true)
    private String endpoint;

    @Option(name = "--username", aliases = {"-u"}, usage = "Username")
    private String username;

    @Option(name = "--password", aliases = {"-p"}, usage = "Password")
    private String password;

    @Option(name = "--type", aliases = {"-t"}, usage = "type")
    private GatewayType type = GatewayType.REST;

    @Override
    protected Gateway buildModelInstance() throws CommandException {
        final String config;
        try {
            config = MappingUtil.JSON_MAPPER.writeValueAsString(
                    new GatewayConfig(endpoint,
                            username,
                            password));

        } catch (JsonProcessingException e) {
            throw new CommandException(e);
        }

        return new Gateway(getGatewayName(),
                description,
                type,
                config);
    }

    protected abstract String getGatewayName();
}
