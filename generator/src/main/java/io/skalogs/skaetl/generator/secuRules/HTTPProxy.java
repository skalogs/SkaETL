package io.skalogs.skaetl.generator.secuRules;

/*-
 * #%L
 * generator
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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HTTPProxy {

    // Timestamp Request
    public String timestamp;
    // Login or uuid
    public String user;
    // Domain user

    //Cookie session : JSESSION,
    public String cookieSession;
    // Remote IP
    public String remoteIp;
    // Host url ask
    public String urlHost;
    // Port url ask
    @JsonProperty("url_port_l")
    public Integer urlPort;
    // Status response : 200 300 etc..
    @JsonProperty("url_status_l")
    public Integer urlStatus;
    // Method GET POST PUT
    public String urlMethod;
    // URI
    public String uri;
    // user agent
    public String userAgent;
    // Size response from server target
    @JsonProperty("response_size_l")
    public Integer sizeResponse;
    // Size send by User
    @JsonProperty("request_size_l")
    public Integer sizeRequest;
    // Time global request + response
    @JsonProperty("global_request_time_l")
    public Integer timeGlobalRequest;
    // Time connexion request
    @JsonProperty("global_cnx_time_l")
    public Integer timeConnexionRequest;
    // Ip of proxy HTTP
    public String proxyIp;
    // Component of proxy HTTP
    public String componentProxy;
    // HTTP OR HTTPS
    public String typeHTTP;
    // hostname of httpproxy
    public String hostname;

}
