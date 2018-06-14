package io.skalogs.skaetl.generator.secuRules;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;


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
