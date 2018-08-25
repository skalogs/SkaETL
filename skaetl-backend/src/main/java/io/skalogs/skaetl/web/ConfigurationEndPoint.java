package io.skalogs.skaetl.web;

/*-
 * #%L
 * skaetl-backend
 * %%
 * Copyright (C) 2017 - 2018 SkaLogs
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import io.skalogs.skaetl.service.ConfSkalogsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/esConfiguration")
@AllArgsConstructor
public class ConfigurationEndPoint {

    private final ConfSkalogsService confSkalogsService;

    @ResponseStatus(OK)
    @GetMapping("fetch")
    public String fetch(@PathParam("env") String env,
                        @PathParam("category") String category,
                        @PathParam("apiKey") String apiKey,
                        @PathParam("hostname") String hostname) {
        return confSkalogsService.fetch(env,category,apiKey,hostname);
    }


}
