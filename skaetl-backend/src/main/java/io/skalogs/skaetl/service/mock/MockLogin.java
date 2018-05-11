package io.skalogs.skaetl.service.mock;

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
