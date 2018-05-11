package io.skalogs.skaetl.web.domain;

import io.skalogs.skaetl.domain.ProcessReferential;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReferentialOutputWeb {

    public String idProcess;
    public ProcessReferential referentialOutput;
}
