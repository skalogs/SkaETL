package io.skalogs.skaetl.web;

import io.skalogs.skaetl.service.RetryImporter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/manage")
@AllArgsConstructor
public class ManageController {

    private final RetryImporter retryImporter;

    @ResponseStatus(OK)
    @GetMapping("/activate")
    public void activate() {
        retryImporter.activate();
    }

    @ResponseStatus(OK)
    @GetMapping("/deactivate")
    public void deactivate() {
        retryImporter.deactivate();
    }

}
