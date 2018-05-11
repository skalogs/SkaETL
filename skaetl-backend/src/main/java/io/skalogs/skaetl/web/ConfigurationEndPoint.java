package io.skalogs.skaetl.web;

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
