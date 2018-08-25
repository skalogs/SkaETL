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
                .numberAllClientConfiguration(promService.fetchData("skaetl_fetch_skalogs_conf", null, null, 5))
                .numberErrorClientConfiguration(promService.fetchData("skaetl_fetch_skalogs_conf_error", null, null, 5))
                .numberProdClientConfiguration(promService.fetchData("skaetl_fetch_skalogs_conf", "env", "prod", 5))
                .build();
    }

    @ResponseStatus(OK)
    @PostMapping("login")
    public String login(@RequestBody LoginWeb loginWeb) {
       return mockLogin.login(loginWeb);
    }



}
