package io.skalogs.skaetl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Slf4j
@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
public class ReferentialApp {

    public static void main(String[] args) throws Exception {
        new SpringApplication(ReferentialApp.class).run(args);
    }
}

