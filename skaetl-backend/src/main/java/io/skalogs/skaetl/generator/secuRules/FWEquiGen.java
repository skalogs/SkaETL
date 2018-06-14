package io.skalogs.skaetl.generator.secuRules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FWEquiGen {

    public String equipment;
    public String equipmentIp;
    public String equipmentVersion;
}
