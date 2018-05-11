package io.skalogs.skaetl.web;


import io.skalogs.skaetl.service.ErrorImporter;
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

    private final ErrorImporter errorImporter;

    @ResponseStatus(OK)
    @GetMapping("/active")
    public void errorImporter() {
        errorImporter.enable();
    }

    @ResponseStatus(OK)
    @GetMapping("/disable")
    public void disable() {
        errorImporter.disable();
    }
}
