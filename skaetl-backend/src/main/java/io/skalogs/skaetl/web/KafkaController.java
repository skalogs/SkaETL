package io.skalogs.skaetl.web;

import io.skalogs.skaetl.service.KafkaService;
import io.skalogs.skaetl.web.domain.GrokInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin/kafka")
@AllArgsConstructor
public class KafkaController {

    private final KafkaService kafkaService;

    @ResponseStatus(OK)
    @PostMapping("/capture")
    public List<String> capture(@RequestBody GrokInfo grokInfo) {
        return kafkaService.catpureData(grokInfo.getTopic(), grokInfo.getDuration(), grokInfo.getTimeUnit());
    }

}
