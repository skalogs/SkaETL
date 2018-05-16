package io.skalogs.skaetl.web;

import io.skalogs.skaetl.service.HomeService;
import io.skalogs.skaetl.service.PromService;
import io.skalogs.skaetl.service.mock.MockLogin;
import io.skalogs.skaetl.web.domain.DataChartsWeb;
import io.skalogs.skaetl.web.domain.HomeWeb;
import io.skalogs.skaetl.web.domain.LoginWeb;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final PromService promService;
    private final MockLogin mockLogin;
    @ResponseStatus(OK)
    @GetMapping("fetch")
    public HomeWeb home() {
        return homeService.getHome();
    }

    @ResponseStatus(OK)
    @GetMapping("dataCapture")
    public DataChartsWeb dataCapture() {
        return DataChartsWeb.builder()
                .dataProcess(homeService.chartsForProcess())
                .dataMetric(homeService.chartsForMetrics())
                .numberAllClientConfiguration(promService.fetchData("skaetl_fetch_skalogs_conf", null, null, 5))
                .numberErrorClientConfiguration(promService.fetchData("skaetl_fetch_skalogs_conf_error", null, null, 5))
                .numberProdClientConfiguration(promService.fetchData("skaetl_fetch_skalogs_conf", "env", "prod", 5))
                .dataClient(homeService.chartsForClients())
                .build();
    }

    @ResponseStatus(OK)
    @PostMapping("login")
    public String login(@RequestBody LoginWeb loginWeb) {
       return mockLogin.login(loginWeb);
    }



}
