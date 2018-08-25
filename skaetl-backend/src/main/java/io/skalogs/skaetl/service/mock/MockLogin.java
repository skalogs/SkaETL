package io.skalogs.skaetl.service.mock;

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

import io.skalogs.skaetl.web.domain.LoginWeb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MockLogin {

    private List<LoginWeb> list = new ArrayList<>();

    @PostConstruct
    public void init() {
        list.add(LoginWeb.builder().login("skalogs").password("demo").build());
        list.add(LoginWeb.builder().login("david").password("skalogs").build());
        list.add(LoginWeb.builder().login("jeanlouis").password("skalogs").build());
        list.add(LoginWeb.builder().login("nicolas").password("skalogs").build());
    }

    public String login(LoginWeb loginWeb) {
        if(list.contains(loginWeb)){
            return "OK";
        }else{
            return "Username or Password incorrect";
        }
    }
}
