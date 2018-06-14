package io.skalogs.skaetl.generator.secuRules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestGen {
    public String cookieSession;
    public String remoteIp;
    public String urlHost;
    public int urlPort;
    public int urlStatus;
    public String urlMethod;
    public String userAgent;
    public int sizeResponse;
    public int sizeRequest;
    public String typeHTTP;
    public String uri;

}
